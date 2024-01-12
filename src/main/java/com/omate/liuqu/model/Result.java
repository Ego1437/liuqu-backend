package com.omate.liuqu.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class Result implements Serializable {

    @JsonIgnore
    private final Map<Integer, String> resultMessage;

    public Result() {
        this.resultMessage = new HashMap<>();
        resultMessage.put(0, "success");
        resultMessage.put(1, "User name and password do not match");
        resultMessage.put(2, "The username already exists!");
        resultMessage.put(3, "search failed");
        resultMessage.put(4, "Invalid token!");
        resultMessage.put(5, "Failed to send email");
        resultMessage.put(6, "Passwords do not match");
        resultMessage.put(7, "Reset password link has been sent to the email if it exists");
        resultMessage.put(8, "Only JPEG, PNG, GIF, BMP, TIFF, SVG, or WebP images are allowed");
        resultMessage.put(9, "Invalid email format");


    }


    private int code;
    private String message;
    private Object data;


    public void setResultSuccess(int code) {
        this.message = resultMessage.get(code);
        this.data = null;
    }

    public void setResultSuccess(int code, Object data) {
        this.code = code;
        this.message = resultMessage.get(code);
        this.data = data;
    }

    public void setResultFailed(int code) {
        this.message = resultMessage.get(code);
        this.data = null;
    }

    public void setResultFailed(int code, Object data) {
        this.code = code;
        this.message = resultMessage.get(code);
        this.data = data;
    }

    public void setResultFailed(int code, String Log) {
        this.code = code;
        this.message = Log;
    }
}
