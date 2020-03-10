package cn.plateform.pub.exception;

import org.springframework.stereotype.Component;

@Component
public class ParameterException extends BaseException{

    public ParameterException(){}

    public ParameterException(String message, String tranceId) {
        super(message, tranceId);
    }
}
