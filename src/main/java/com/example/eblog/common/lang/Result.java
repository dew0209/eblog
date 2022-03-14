package com.example.eblog.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int status;
    private String msg;
    private Object data;
    private String action;

    public static Result success(String msg,Object data){
        Result result = new Result();
        result.msg = msg;
        result.status = 0;
        result.data = data;
        return result;
    }
    public static Result success(Object data){
        return success("操作成功",data);
    }
    public static Result success(){
        return success("操作成功",null);
    }
    public static Result fail(String msg){
        Result result = new Result();
        result.msg = msg;
        result.status = -1;
        result.data = null;
        return result;
    }
    public Result action(String action){
        this.action = action;
        return this;
    }

}
