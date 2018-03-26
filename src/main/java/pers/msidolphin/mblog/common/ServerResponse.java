package pers.msidolphin.mblog.common;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import pers.msidolphin.mblog.common.enums.ResponseCode;

import java.io.Serializable;

/**
 * Created by msidolphin on 2018/3/26.
 */
@Getter
@Setter
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@SuppressWarnings({"unused"})
public class ServerResponse<T> implements Serializable {

	//相应状态码
	private Integer status;

	//相应消息
	private String message;

	//数据
	private T data;

	public ServerResponse() {}

	public ServerResponse(Integer status, String message) {
		this(status, message, null);
	}

	public ServerResponse(Integer status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	// Custom

	public static <T> ServerResponse<T> response(Integer status, String message, T data) {
		return new ServerResponse<>(status, message, data);
	}

	public static <T> ServerResponse<T> response(Integer status, String message) {
		return new ServerResponse<>(status, message);
	}

	// Success

	public static <T> ServerResponse<T> success(T data) {
		return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription(), data);
	}

	public static ServerResponse success(String msg) {
		return new ServerResponse(ResponseCode.OK.getCode(), msg);
	}

	public static ServerResponse success() {
		return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription());
	}

	// Bad Request
	public static ServerResponse badRequest() {
		return new ServerResponse(ResponseCode.BAD_REQUEST.getCode(), ResponseCode.BAD_REQUEST.getDescription());
	}

	public static ServerResponse badRequest(String msg) {
		return new ServerResponse(ResponseCode.BAD_REQUEST.getCode(), msg);
	}

	// Internal Server Error
	public static ServerResponse internalError() {
		return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), ResponseCode.INTERNAL_SERVER_ERROR.getDescription());
	}

	public static ServerResponse internalError(String msg) {
		return new ServerResponse(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), msg);
	}

	// unauthorized
	public static ServerResponse unauthorized() {
		return new ServerResponse(ResponseCode.UNAUTHORIZED.getCode(), ResponseCode.UNAUTHORIZED.getDescription());
	}

	public static ServerResponse unauthorized(String msg) {
		return new ServerResponse(ResponseCode.UNAUTHORIZED.getCode(), msg);
	}

	//forbidden
	public static ServerResponse forbidden() {
		return new ServerResponse(ResponseCode.FORBIDDEN.getCode(), ResponseCode.FORBIDDEN.getDescription());
	}

	public static ServerResponse forbidden(String msg) {
		return new ServerResponse(ResponseCode.FORBIDDEN.getCode(), msg);
	}

	@Override
	public String toString() {
		return "response:{" +
				"status=" + status +
				", message='" + message + '\'' +
				", data=" + data +
				'}';
	}
}
