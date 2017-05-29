package com.coursework.server;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.CommandManager;
import com.coursework.dao.connectionpool.ConnectionPool;
import com.coursework.dao.exception.DAOException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final ExecutorService threadPool;
    private final ServerSocket serverSocket;
    private final StopServerMonitor monitor;
    private final ResourceBundle svrRes;
    private static boolean run = false;

    public Server() throws IOException {
        svrRes = ResourceBundle.getBundle("server");
        int serverPort = Integer.parseInt(svrRes.getString("port"));

        threadPool = Executors.newCachedThreadPool();

        serverSocket = new ServerSocket(serverPort);
        monitor = new StopServerMonitor();

        System.out.println("Сервер запущен");
        System.out.println("Порт сервера " + serverPort);
    }

    public void startupServer() throws IOException, DAOException {
        if(run){
            throw new IllegalStateException("Сервер уже запущен");
        }
        run = true;

        //запускаем монитор слушать прекращение работы сервера
        new Thread(monitor).start();

        ConnectionPool.getInstance();

        while(run){
            final Socket socket = serverSocket.accept();
            threadPool.submit(new RequestHandler(socket));
        }

        threadPool.shutdown();
        while(!threadPool.isTerminated()){
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        ConnectionPool.getInstance().closeAll();

        serverSocket.close();
        System.out.println("Сервер прекратил работу");
    }

    private class RequestHandler implements Callable<Void> {
        private final Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }


        @Override
        public Void call() throws IOException, ClassNotFoundException {
            try(ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

                Request request = (Request) in.readObject();
                Response response = CommandManager.getInstance().execute(request);
                out.writeObject(response);
            } catch(IOException e){
                e.printStackTrace();
            } finally {
                socket.close();
            }

            return null;
        }
    }

    private class StopServerMonitor extends Thread {
        private ServerSocket stopSocket;

        public StopServerMonitor(){
            setDaemon(true);
            setName("stop monitor");
            ResourceBundle bundle = ResourceBundle.getBundle("stopserver");
            try {
                stopSocket = new ServerSocket(Integer.parseInt(bundle.getString("port")));
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }

        @Override
        public void run(){
            try {
                Socket socket = stopSocket.accept();
                run = false;
                socket.close();
                stopSocket.close();
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }
    }
}
