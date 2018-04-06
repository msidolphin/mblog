package pers.msidolphin.mblog.model.mapper;

import pers.msidolphin.mblog.object.dto.AdminLinkDto;
import pers.msidolphin.mblog.object.query.LinkQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/4/5.
 */
public interface LinkMapper {

	List<AdminLinkDto> findLinks(LinkQuery query);

	int updateLinkById(Map<String, String> params);
}
