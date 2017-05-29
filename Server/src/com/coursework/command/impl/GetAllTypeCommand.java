package com.coursework.command.impl;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.bean.Type;
import com.coursework.command.Command;
import com.coursework.dao.ShopDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

public class GetAllTypeCommand implements Command {
    @Override
    public Response execute(Request request) {
        ShopDAO<Type, Integer> dao = DAOFactory.getInstance().getTypeDao();
        Response response = new Response();

        try {
            response.setAttribute("types", dao.select());
        } catch(DAOException e){
            response.setError(true);
        }
        return response;
    }
}
