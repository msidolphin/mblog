package pers.msidolphin.mblog.exception;

/**
 * 普通业务类处理异常
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public class ServiceException extends AbstractApplicationException{

	public ServiceException() {}

	public ServiceException(Throwable cause) {
		this("业务异常", cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
