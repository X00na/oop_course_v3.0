package com.coursework.command.impl;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.bean.User;
import com.coursework.command.Command;
import com.coursework.dao.UserDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

public class LogInCommand implements Command {

    @Override
    public Response execute(Request request) {
        User user = (User) request.getParameter("user");
        String login = user.getLogin();
        String password = user.getPassword();

        UserDAO<User, Integer> dao = DAOFactory.getInstance().getUserDao();
        Response response = new Response();

        try {
            user = dao.selectByLogin(login);
            if(user == null || !user.getPassword().equals(password)){
                response.setError(true);
            } else {
                response.setAttribute("user", user);
            }
        } catch(DAOException e){
            response.setError(true);
        }

        return response;
    }
}
