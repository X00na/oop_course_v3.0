package com.coursework.command;

import com.coursework.bean.Request;
import com.coursework.bean.Response;

public interface Command {
    Response execute(Request request);
}
