package pers.msidolphin.mblog.exception;

/**
 * Ajax请求异常
 *
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public class AjaxRequestException extends AbstractApplicationException {

	public AjaxRequestException() {
		super();
	}

	public AjaxRequestException(Throwable cause) {
		this("Ajax请求异常...", cause, false, true);
	}

	public AjaxRequestException(String message, Throwable cause) {
		this(message, cause, false, true);
	}

	public AjaxRequestException(String message, Throwable cause,
								boolean enableSuppression,
								boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
