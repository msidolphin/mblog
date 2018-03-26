package pers.msidolphin.mblog.exception;

/**
 * 应用异常基类，必须继承RuntimeException。当抛出异常<code>e instanceof AbstractApplicationException</code>为true时，
 * 全局异常处理将会把异常消息返回给前台
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public class AbstractApplicationException extends RuntimeException {

	public AbstractApplicationException(){}

	public AbstractApplicationException(String message) {
		this(message, new RuntimeException(), false, true);
	}

	public AbstractApplicationException(Throwable cause) {
		this(null, cause, false, true);
	}

	public AbstractApplicationException(String message, Throwable cause) {
		this(message, cause, false, true);
	}

	public AbstractApplicationException(String message, Throwable cause, boolean enableSuppression) {
		this(message, cause, enableSuppression, true);
	}

	public AbstractApplicationException(String message,
										Throwable cause,
										boolean enableSuppression,
										boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
