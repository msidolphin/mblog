package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;
import pers.msidolphin.mblog.common.annotation.Validation;

import javax.validation.constraints.NotEmpty;

/**
 * Created by msidolphin on 2018/4/4.
 */
@Getter
@Setter
public class AdminUserDto {
	private String id;
	private String username;
	private String password;
	private String accessToken;

	@Override
	public String toString() {
		return "AdminUserDto{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", accessToken='" + accessToken + '\'' +
				'}';
	}
}

