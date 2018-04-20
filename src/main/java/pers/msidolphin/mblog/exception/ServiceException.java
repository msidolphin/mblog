package pers.msidolphin.mblog.exception;

import pers.msidolphin.mblog.common.enums.ResponseCode;

/**
 * 普通业务类处理异常
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public class ServiceException extends AbstractApplicationException{

	private Integer code;

	private String message;

	public ServiceException() {}

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable cause) {
		this("业务异常", cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(ResponseCode responseCode) {
		this.code = responseCode.getCode();
		this.message = responseCode.getDescription();
	}

	public ServiceException(ResponseCode responseCode, String message) {
		this.code = responseCode.getCode();
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
