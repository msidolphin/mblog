package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.dto.SettingDto;
import pers.msidolphin.mblog.object.query.SettingQuery;
import pers.msidolphin.mblog.service.SettingService;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/settings")
public class SettingsController {

	@Autowired
	private SettingService settingService;

	@GetMapping("")
	public ServerResponse<?> list(SettingQuery query) {
		return settingService.getSettings(query);
	}

	@PutMapping("")
	public ServerResponse<?> put(@RequestBody SettingDto adminSettingDto) {
		return settingService.updateSetting(adminSettingDto);
	}
}
