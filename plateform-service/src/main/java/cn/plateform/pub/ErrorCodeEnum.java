package cn.plateform.pub;

import cn.plateform.constant.StatusConstant;

public enum ErrorCodeEnum {

    NO_ACCESS(StatusConstant.NO_ACCESS,"没有访问权限"),
    SERVICE_EERROR(StatusConstant.SERVICE_EERROR,"服务器异常"),
    PARAM_ERROR(StatusConstant.PARAM_ERROR,"参数异常"),
    SERVICE_OVERTIME(StatusConstant.SERVICE_OVERTIME,"系统忙，稍后再试"),
    NO_REPEATE(StatusConstant.NO_REPEATE,"禁止重复提交");

    private Integer code;
    private String message;


    ErrorCodeEnum(Integer code,String message){
        this.code=code;
        this.message=message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
