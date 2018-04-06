package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.mapper.SettingMapper;
import pers.msidolphin.mblog.object.dto.AdminSettingDto;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.SettingQuery;

import java.util.Map;

/**
 * Created by msidolphin on 2018/4/6.
 */
@Service
public class SettingService {

	@Autowired
	private SettingMapper settingMapper;

	public ServerResponse<?> getSettings(SettingQuery query) {
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		return ServerResponse.success(new PageInfo<>(settingMapper.findSettings(query)));
	}

	@Transactional
	public ServerResponse<?> updateSetting(AdminSettingDto setting) {
		User user = RequestHolder.getCurrentAdmin();
		if(user == null) throw new AuthorizedException();
		if(Util.isEmpty(setting.getId())) throw new InvalidParameterException("id不能为空");
		Map<String, String> params = Maps.newHashMap();
		params.put("id", setting.getId());
		params.put("name", setting.getName());
		params.put("value", setting.getValue());
		params.put("type", setting.getType());
		params.put("isImage", setting.getIsImage() ? "1" : "0");
		params.put("updator", user.getId().toString());
		settingMapper.updateSettingById(params);
		return ServerResponse.success(params);
	}
}
