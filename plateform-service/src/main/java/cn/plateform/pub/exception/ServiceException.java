package cn.plateform.pub.exception;

import org.springframework.stereotype.Component;

@Component
public class ServiceException extends BaseException{

    private Integer code;

    public ServiceException(){}

    public ServiceException(Integer code,String message, String tranceId) {
        super(message, tranceId);
        this.code=code;
    }

    public Integer getCode(){
        return code;
    }
}
