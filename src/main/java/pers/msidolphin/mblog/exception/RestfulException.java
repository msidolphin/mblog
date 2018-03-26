package pers.msidolphin.mblog.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Restful Service 异常
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@Getter
@SuppressWarnings("ALL")
public class RestfulException extends AbstractApplicationException {

	public HttpStatus httpStatus;

	public RestfulException(){
		this(HttpStatus.INTERNAL_SERVER_ERROR, null);
	}

	public RestfulException(HttpStatus httpStatus) {
		this(httpStatus, null);
	}

	public RestfulException(String message) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

	public RestfulException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}
}
