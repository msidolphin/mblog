package pers.msidolphin.mblog.common.enums;

/**
 * Created by msidolphin on 2018/3/26.
 */
@SuppressWarnings({"unused"})
public enum ResponseCode {

	OK(200, "ok"),											// 请求成功，用于GET与POST请求
	CREATED(201, "created"),								// 已创建，成功请求并创建了新的资源
	NO_CONTENT(204, "no content"),							// 无内容。服务器成功处理，但未返回内容。在未更新网页的情况下，可确保浏览器继续显示当前文档
	BAD_REQUEST(400, "bad request"),						// 客户端请求的语法错误，服务器无法理解
	UNAUTHORIZED(401, "unauthorized"),						// 请求要求用户的身份认证
	FORBIDDEN(403, "forbidden"),							// 服务器理解请求客户端的请求，但是拒绝执行此请求
	NOT_FOUND(404, "not found"),							// 服务器无法根据客户端的请求找到资源
	INTERNAL_SERVER_ERROR(500, "internal server error"),	//服务器内部错误，无法完成请求
	NOT_IMPLEMENTED(501, "not implemented");				//服务器不支持请求的功能，无法完成请求

	private Integer code;
	private String  description;

	ResponseCode(Integer code, String description) {
		this.code = code;
		this.description = description;
	}

	public Integer getCode() {
		return code;
	}

	public ResponseCode setCode(Integer code) {
		this.code = code;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public ResponseCode setDescription(String description) {
		this.description = description;
		return this;
	}

	public static ResponseCode get(int code) {
		for(ResponseCode responseCode : ResponseCode.values()) {
			if(code == responseCode.getCode()) {
				return responseCode;
			}
		}
		return null;
	}
}
