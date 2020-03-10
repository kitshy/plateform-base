package cn.plateform.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultBackMessage<T> implements Serializable {

    private boolean success;
    private int code;
    private String msg;
    private T data;
    private int total;

    public static <T> DefaultBackMessage success(String msg){
        return new DefaultBackMessage(true,200,msg);
    }
    public static <T> DefaultBackMessage success(T data){
        return new DefaultBackMessage(true,200,data);
    }
    public static <T> DefaultBackMessage success(String msg,T data){
        return new DefaultBackMessage(true,200,msg,data);
    }
    public static <T> DefaultBackMessage success(T data,int total){
        return new DefaultBackMessage(true,200,data,total);
    }
    public  static <T> DefaultBackMessage success(String msg,T data,int total){
        return new DefaultBackMessage(true,200,msg,data,total);
    }
    public static <T> DefaultBackMessage success(int code,String msg){
        return new DefaultBackMessage(true,code,msg);
    }
    public static <T> DefaultBackMessage sucess(int code,T data){
       return new DefaultBackMessage(true,code,data);
    }
    public static <T> DefaultBackMessage success(int code,String msg,T data){
        return new DefaultBackMessage(true,code,msg,data);
    }
    public static <T> DefaultBackMessage success(int code,T data,int total){
        return new DefaultBackMessage(true,code,data,total);
    }
    public  static <T> DefaultBackMessage success(int code,String msg,T data,int total){
        return new DefaultBackMessage(true,code,msg,data,total);
    }



    public static <T> DefaultBackMessage fail(String msg){
        return new DefaultBackMessage(false,400,msg);
    }
    public static <T> DefaultBackMessage fail(T data){
        return new DefaultBackMessage(false,400,data);
    }
    public static <T> DefaultBackMessage fail(String msg,T data){
        return new DefaultBackMessage(false,400,msg,data);
    }
    public static <T> DefaultBackMessage fail(T data,int total){
        return new DefaultBackMessage(false,400,data,total);
    }
    public  static <T> DefaultBackMessage fail(String msg,T data,int total){
        return new DefaultBackMessage(false,400,msg,data,total);
    }
    public static <T> DefaultBackMessage fail(int code,String msg){
        return new DefaultBackMessage(false,code,msg);
    }
    public static <T> DefaultBackMessage fail(int code,T data){
        return new DefaultBackMessage(false,code,data);
    }
    public static <T> DefaultBackMessage fail(int code,String msg,T data){
        return new DefaultBackMessage(false,code,msg,data);
    }
    public static <T> DefaultBackMessage fail(int code,T data,int total){
        return new DefaultBackMessage(false,code,data,total);
    }
    public  static <T> DefaultBackMessage fail(int code,String msg,T data,int total){
        return new DefaultBackMessage(false,code,msg,data,total);
    }




    public DefaultBackMessage(boolean success, int code, String msg) {
        this.code = code;
        this.success=success;
        this.msg = msg;
    }
    public DefaultBackMessage(boolean success, int code, T data){
        this.success=success;
        this.code=code;
        this.data=data;
    }
    public DefaultBackMessage(boolean success, int code, String msg, T data){
        this.success=success;
        this.code=code;
        this.msg=msg;
        this.data=data;
    }
    public DefaultBackMessage(boolean success, int code, T data, int total){
        this.success=success;
        this.code=code;
        this.data=data;
        this.total=total;
    }
    public DefaultBackMessage(boolean success, int code, String msg, T data, int total){
        this.success=success;
        this.code=code;
        this.msg=msg;
        this.data=data;
        this.total=total;
    }


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
