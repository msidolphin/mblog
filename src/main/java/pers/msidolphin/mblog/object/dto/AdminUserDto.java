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
	private String avatar;
	private String username;
	private String nickname;
	private String password;
	private String email;
	private String phone;
	private String summary;
	private String accessToken;

	@Override
	public String toString() {
		return "AdminUserDto{" +
				"id='" + id + '\'' +
				", avatar='" + avatar + '\'' +
				", username='" + username + '\'' +
				", nickname='" + nickname + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", phone='" + phone + '\'' +
				", summary='" + summary + '\'' +
				", accessToken='" + accessToken + '\'' +
				'}';
	}
}

