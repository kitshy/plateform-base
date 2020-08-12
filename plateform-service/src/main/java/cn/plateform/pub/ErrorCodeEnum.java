package cn.plateform.pub;

import cn.plateform.constant.GlobalParams;

public enum ErrorCodeEnum {

    NO_ACCESS(GlobalParams.NO_ACCESS,"没有访问权限"),
    SERVICE_EERROR(GlobalParams.SERVICE_EERROR,"服务器异常"),
    PARAM_ERROR(GlobalParams.PARAM_ERROR,"参数异常"),
    SERVICE_OVERTIME(GlobalParams.SERVICE_OVERTIME,"系统忙，稍后再试"),
    NO_REPEATE(GlobalParams.NO_REPEATE,"禁止重复提交");

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
