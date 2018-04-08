package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.PropertiesHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.mapper.SettingMapper;
import pers.msidolphin.mblog.object.dto.PortalSettingDto;
import pers.msidolphin.mblog.object.dto.SettingDto;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.SettingQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/4/6.
 */
@Service
public class SettingService {

	@Autowired
	private SettingMapper settingMapper;

	@Autowired
	private TagService tagService;

	@Autowired
	private LinkService linkService;

	@Autowired
	private PropertiesHelper propertiesHelper;

	public ServerResponse<?> getSettings(SettingQuery query) {
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		return ServerResponse.success(new PageInfo<>(settingMapper.findSettings(query)));
	}

	@Transactional
	public ServerResponse<?> updateSetting(SettingDto setting) {
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

	/**
	 * 获取前台配置
	 * @return
	 */
	public ServerResponse<?> getPortalSettings() {
		PortalSettingDto portalSettings = new PortalSettingDto();
		PortalSettingDto.Header header = portalSettings.new Header();
		PortalSettingDto.Footer footer = portalSettings.new Footer();
		PortalSettingDto.Site site = portalSettings.new Site();
		PortalSettingDto.Design design = portalSettings.new Design();
		PortalSettingDto.Copyright copyright = portalSettings.new Copyright();

		List<SettingDto> settings = settingMapper.selectPortalSettings();
		List<PortalSettingDto.Contract> contracts = Lists.newArrayList();
		for(SettingDto setting : settings) {
			String key = setting.getKey();
			String value = setting.getValue();
			if (key.equals(propertiesHelper.getValue("blog.portal.site.header.title.key"))) {
				//前台标题
				header.setTitle(value);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.header.avatar.key"))) {
				//前台头像
				header.setAvatar(value);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.footer.copyright.icp.key"))) {
				//IPC
				copyright.setIpc(value);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.footer.copyright.copyright.key"))) {
				copyright.setDate(value);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.footer.site.key"))) {
				site.setText("msidolphin");
				site.setUrl(value);
				copyright.setSite(site);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.footer.designed_by.key"))) {
				design.setName("vinceok");
				design.setValue(value);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.footer.contract.mail.key"))) {
				PortalSettingDto.Contract contract = portalSettings.new Contract();
				contract.setName(value);
				contract.setValue("#");
				contract.setIcon("fa fa-envelope-o");
				contracts.add(contract);
			} else if (key.equals(propertiesHelper.getValue("blog.portal.site.header.background.key"))) {
				header.setBackground(value);
			} else {

			}
		}
		//联系方式
		footer.setContracts(contracts);
		//Copyright
		footer.setCopyright(copyright);
		footer.setDesign(design);
		//友情链接
		footer.setLinks(linkService.selectLinks4Portal());
		//最热标签
		footer.setTags(tagService.selectTag4Portal());

		portalSettings.setFooter(footer);
		portalSettings.setHeader(header);

		return ServerResponse.success(portalSettings);
	}
}
