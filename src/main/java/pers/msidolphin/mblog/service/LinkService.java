package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.mapper.LinkMapper;
import pers.msidolphin.mblog.model.repository.LinkRepository;
import pers.msidolphin.mblog.object.dto.AdminLinkDto;
import pers.msidolphin.mblog.object.dto.PortalLinkDto;
import pers.msidolphin.mblog.object.po.Link;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.LinkQuery;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/4/6.
 */
@Service
public class LinkService {

	@Autowired
	private LinkMapper linkMapper;

	@Autowired
	private LinkRepository linkRepository;

	public ServerResponse<?> getLinks(LinkQuery query) {
		PageHelper.startPage(query.getPageNum(), query.getPageSize());
		return ServerResponse.success(new PageInfo<>(linkMapper.findLinks(query)));
	}

	@Transactional
	public ServerResponse<?> updateLink(AdminLinkDto linkDto, String uid) {
		Map<String, String> params = Maps.newHashMap();
		params.put("id", linkDto.getId());
		params.put("name", linkDto.getName());
		params.put("url", linkDto.getUrl());
		params.put("sort", linkDto.getSort());
		params.put("updator", uid);
		linkMapper.updateLinkById(params);
		return ServerResponse.success(params);
	}

	@Transactional
	public ServerResponse<?> deletelink(String id) {
		if (Util.isEmpty(id)) throw new InvalidParameterException("链接id不能为空");
		User user = RequestHolder.getCurrentAdmin();
		if (user == null) throw new AuthorizedException();
		Map<String, String> params = Maps.newHashMap();
		params.put("id", id);
		params.put("is_delete", "1"); //删除
		params.put("updator", user.getId().toString());
		linkMapper.updateLinkById(params);
		return ServerResponse.success(params);
	}

	public ServerResponse<?> save(AdminLinkDto linkDto) {
		if(Util.isEmpty(linkDto)) throw new InvalidParameterException();
		User user = RequestHolder.getCurrentAdmin();
		if(linkDto.getId() != null) return updateLink(linkDto, user.getId().toString());
		Link link = new Link();
		BeanUtils.copyProperties(linkDto, link);
		link.setId(AutoIdHelper.getId().toString());
		link.setCreateTime(new Date());
		link.setUpdateTime(new Date());
		link.setCreator(user.getId());
		link.setUpdator(user.getId());
		linkRepository.save(link);
		return ServerResponse.success(link);
	}

	public List<PortalLinkDto> selectLinks4Portal() {
		return linkMapper.selectLink4Portal();
	}

}
