package cn.plateform.pub;

import cn.plateform.pub.exception.LoginException;
import cn.plateform.pub.exception.ParameterException;
import cn.plateform.pub.exception.ReturnException;
import cn.plateform.pub.exception.ServiceException;
import cn.plateform.utils.DefaultBackMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandle {

    //业务异常
    @ExceptionHandler(ServiceException.class)
     public DefaultBackMessage handlerServiceException(ServiceException e){
        return DefaultBackMessage.fail(ErrorCodeEnum.SERVICE_EERROR.getCode(),ErrorCodeEnum.SERVICE_EERROR.getMessage());
    }

    //登陆权限
    @ExceptionHandler(LoginException.class)
    public DefaultBackMessage handlerLoginException(LoginException e){
        return DefaultBackMessage.fail(ErrorCodeEnum.NO_ACCESS.getCode(),ErrorCodeEnum.NO_ACCESS.getMessage());
    }

    //参数异常
    @ExceptionHandler(ParameterException.class)
    public DefaultBackMessage handlerParamException(ParameterException e){
        return DefaultBackMessage.fail(ErrorCodeEnum.PARAM_ERROR.getCode(),ErrorCodeEnum.PARAM_ERROR.getMessage());
    }

    //公共返回异常
    @ExceptionHandler(ReturnException.class)
    public DefaultBackMessage handlerReturn(ReturnException e){
        return DefaultBackMessage.fail(e.getCode(),e.getMessage()) ;
    }
}
