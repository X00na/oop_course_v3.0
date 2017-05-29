package com.coursework.command.impl;

import com.coursework.bean.Product;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.Command;
import com.coursework.dao.ProductDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

import java.util.Collections;
import java.util.List;

public class SearchProductCommand implements Command {
    @Override
    public Response execute(Request request) {
        String searchType = (String) request.getParameter("searchType");
        String searchValue = (String) request.getParameter("searchValue");

        ProductDAO<Product, Integer> dao = DAOFactory.getInstance().getProductDao();
        Response response = new Response();

        try {
            List<Product> products = Collections.emptyList();
            switch(searchType){
                case "Названию":
                    products = dao.searchByName(searchValue);
                    break;
                case "Цене":
                    try {
                        products = dao.searchByPrice(Integer.parseInt(searchValue));
                    } catch(NumberFormatException e){
                        products = Collections.emptyList();
                    }
                    break;
                case "Типу":
                    products = dao.searchByType(searchValue);
            }
            response.setAttribute("products", products);
        } catch(DAOException e){
            response.setError(true);
        }

        return response;
    }
}
