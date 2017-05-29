package com.coursework.command.impl;

import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.bean.Type;
import com.coursework.command.Command;
import com.coursework.dao.ShopDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

public class DeleteTypeCommand implements Command {
    @Override
    public Response execute(Request request) {
        Type type = (Type) request.getParameter("type");

        ShopDAO<Type, Integer> dao = DAOFactory.getInstance().getTypeDao();
        Response response = new Response();

        try {
            dao.delete(type.getId());
        } catch(DAOException e){
            response.setError(true);
        }
        return response;
    }
}
