package pers.msidolphin.mblog.common;

import com.mysql.fabric.Server;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;

/**
 * 全局异常处理类
 * Created by msidolphin on 2018/4/2.
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	/**
	 * 处理参数校验错误
	 * @param exception {InvalidParameterException}
	 * @return 400 bad request
	 */
	@ExceptionHandler(value = InvalidParameterException.class)
	public ServerResponse<?> invalidParameterExceptionHandler(InvalidParameterException exception) {
		return ServerResponse.badRequest(ResponseCode.BAD_REQUEST.getDescription(), exception.getValidateResult());
	}

	@ExceptionHandler(AuthorizedException.class)
	public ServerResponse<?> unauthorizedExceptionHandler(AuthorizedException e) {
		return ServerResponse.unauthorized();
	}
}
