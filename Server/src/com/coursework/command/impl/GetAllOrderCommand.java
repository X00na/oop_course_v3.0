package com.coursework.command.impl;

import com.coursework.bean.Order;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.Command;
import com.coursework.dao.OrderDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

public class GetAllOrderCommand implements Command {
    @Override
    public Response execute(Request request) {
        OrderDAO<Order, Integer> dao = DAOFactory.getInstance().getOderDao();
        Response response = new Response();

        try {
            response.setAttribute("orders", dao.select());
        } catch(DAOException e){
            response.setError(true);
        }
        return response;
    }
}
