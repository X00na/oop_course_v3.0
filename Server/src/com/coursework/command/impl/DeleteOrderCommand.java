package com.coursework.command.impl;

import com.coursework.bean.Order;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.Command;
import com.coursework.dao.OrderDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

public class DeleteOrderCommand implements Command {
    @Override
    public Response execute(Request request) {
        Order order = (Order) request.getParameter("order");

        OrderDAO<Order, Integer> dao = DAOFactory.getInstance().getOderDao();
        Response response = new Response();

        try {
            dao.delete(order.getId());
        } catch(DAOException e){
            response.setError(true);
        }
        return response;
    }
}
