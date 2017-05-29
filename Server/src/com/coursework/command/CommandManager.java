package com.coursework.command;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.impl.*;

import java.util.HashMap;

public class CommandManager {
    private static CommandManager instance = new CommandManager();
    private HashMap<String, Command> commands;

    public static CommandManager getInstance(){
        return instance;
    }

    private CommandManager(){
        commands = new HashMap<>();
        commands.put("login", new LogInCommand());
        commands.put("logout", new LogOutCommand());
        commands.put("allProducts", new GetAllProductCommand());
        commands.put("searchProduct", new SearchProductCommand());
        commands.put("allTypes", new GetAllTypeCommand());
        commands.put("addProduct", new AddProductCommand());
        commands.put("deleteProduct", new DeleteProductCommand());
        commands.put("editProduct", new EditProductCommand());
        commands.put("addType", new AddTypeCommand());
        commands.put("editType", new EditTypeCommand());
        commands.put("deleteType", new DeleteTypeCommand());
        commands.put("allOrders", new GetAllOrderCommand());
        commands.put("addOrder", new AddOrderCommand());
        commands.put("editOrder", new EditOrderCommand());
        commands.put("deleteOrder", new DeleteOrderCommand());
        commands.put("searchOrder", new SearchOrderCommand());
        commands.put("statistic", new GetStatisticsCommand());
    }

    public Response execute(Request request){
        String command = request.getCommand();
        return commands.get(command).execute(request);
    }
}
