package pers.msidolphin.mblog.common;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import pers.msidolphin.mblog.common.enums.ResponseCode;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/26.
 */
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

	public ServerResponse(ResponseCode responseCode) {
		this(responseCode.getCode(), responseCode.getDescription());
	}

	public ServerResponse(Integer status, String message) {
		this(status, message, null);
	}

	public ServerResponse(Integer status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	// Custom

	public static <T> ServerResponse<T> response(ResponseCode responseCode, T data) {
		return new ServerResponse<>(responseCode.getCode(), responseCode.getDescription(), data);
	}

	public static ServerResponse response(ResponseCode responseCode) {
		return new ServerResponse(responseCode.getCode(), responseCode.getDescription());
	}

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

	public static ServerResponse success() {
		return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription());
	}

	public static ServerResponse success4Elasticsearch() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("hits", 0);
		result.put("meta", new Object());
		return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription(), result);
	}

	public static ServerResponse success4Elasticsearch(long hits, Object meta) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("hits", hits);
		result.put("meta", meta);
		return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription(), result);
	}

	// Bad Request
	public static ServerResponse badRequest() {
		return new ServerResponse(ResponseCode.BAD_REQUEST.getCode(), ResponseCode.BAD_REQUEST.getDescription());
	}

	public static ServerResponse badRequest(String msg) {
		return new ServerResponse(ResponseCode.BAD_REQUEST.getCode(), msg);
	}

	public static <T> ServerResponse<T> badRequest(String msg, T data) {
		return new ServerResponse<>(ResponseCode.BAD_REQUEST.getCode(), msg, data);
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

	//conflict
	public static ServerResponse conflict() {
		return new ServerResponse(ResponseCode.CONFLICT);
	}

	public Integer getStatus() {
		return status;
	}

	public ServerResponse setStatus(Integer status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public ServerResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public ServerResponse setData(T data) {
		this.data = data;
		return this;
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
