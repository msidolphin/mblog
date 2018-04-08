package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.SettingDto;
import pers.msidolphin.mblog.object.po.Setting;
import pers.msidolphin.mblog.object.query.SettingQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/4/5.
 */
public interface SettingMapper {

	List<Setting> findSettings(SettingQuery query);

	int updateSettingById(Map<String, String> params);

	List<SettingDto> selectPortalSettings();
}
