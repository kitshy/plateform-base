package cn.plateform.pub.exception;

import org.springframework.stereotype.Component;

@Component
public class BaseException extends RuntimeException {

    public BaseException(){}

    public BaseException(String message,String tranceId) {
        super(message);
    }
}
