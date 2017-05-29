package com.coursework.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean error;
    private String message;
    private Map<String, Object> attributes;

    public Response(){
        attributes = new HashMap<>();
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setAttribute(String key, Object value){
        attributes.put(key, value);
    }

    public Object getParameter(String key){
        return attributes.get(key);
    }
}
