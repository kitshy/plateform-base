package cn.plateform.pub.exception;

import org.springframework.stereotype.Component;

@Component
public class ReturnException extends BaseException{

    public ReturnException(){}

    private Integer code;

    public ReturnException(Integer code,String message, String tranceId) {
        super(message, tranceId);
        this.code=code;
    }

    public Integer getCode() {
        return code;
    }
}
