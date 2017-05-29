package com.coursework.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private String command;
    private Map<String, Object> attributes;

    public Request(){
        attributes = new HashMap<>();
    }

    public String getCommand(){
        return command;
    }

    public void setCommand(String command){
        this.command = command;
    }

    public void setAttribute(String key, Object value){
        attributes.put(key, value);
    }

    public Object getParameter(String key){
        return attributes.get(key);
    }
}
