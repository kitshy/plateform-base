package cn.plateform.pub.exception;

import org.springframework.stereotype.Component;

@Component
public class LoginException extends RuntimeException {

    public LoginException(){}

    public LoginException(String message) {
        super(message);
    }
}
