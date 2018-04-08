package pers.msidolphin.mblog.portal.controller;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.object.dto.PortalSettingDto;
import pers.msidolphin.mblog.object.dto.SettingDto;
import pers.msidolphin.mblog.service.SettingService;

import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalSettingsController")
@RequestMapping("/settings")
public class SettingsController {

	@Autowired
	private SettingService settingService;

	@GetMapping("")
	public ServerResponse<?> portal() {
		return settingService.getPortalSettings();
	}

}
