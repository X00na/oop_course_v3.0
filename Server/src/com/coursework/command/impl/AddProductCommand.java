package com.coursework.command.impl;

import com.coursework.bean.Product;
import com.coursework.bean.Request;
import com.coursework.bean.Response;
import com.coursework.command.Command;
import com.coursework.dao.ProductDAO;
import com.coursework.dao.exception.DAOException;
import com.coursework.dao.factory.DAOFactory;

public class AddProductCommand implements Command {
    @Override
    public Response execute(Request request) {
        Product product = (Product) request.getParameter("product");

        ProductDAO<Product, Integer> dao = DAOFactory.getInstance().getProductDao();
        Response response = new Response();

        try {
            dao.insert(product);
        } catch(DAOException e){
            response.setError(true);
        }
        return response;
    }
}
