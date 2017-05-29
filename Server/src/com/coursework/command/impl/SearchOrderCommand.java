package com.coursework.command.impl;

import com.coursework.bean.Order;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.Command;
import com.coursework.dao.OrderDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SearchOrderCommand implements Command {
    private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Response execute(Request request) {
        String searchType = (String) request.getParameter("searchType");
        String searchValue = (String) request.getParameter("searchValue");

        OrderDAO<Order, Integer> dao = DAOFactory.getInstance().getOderDao();
        Response response = new Response();

        try {
            List<Order> products = Collections.emptyList();
            switch(searchType){
                case "Продукту":
                    products = dao.searchByProduct(searchValue);
                    break;
                case "Дате":
                    try {
                        Date date = df.parse(searchValue);
                        products = dao.searchByDate(date);
                    } catch(ParseException e){
                        products = Collections.emptyList();
                    }
            }
            response.setAttribute("orders", products);
        } catch(DAOException e){
            response.setError(true);
        }

        return response;
    }
}
