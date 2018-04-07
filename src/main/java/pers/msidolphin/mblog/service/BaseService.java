package pers.msidolphin.mblog.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.dto.EchartsDto;

import javax.servlet.ServletContext;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/31.
 */
public abstract class BaseService {


	protected ServerResponse<?> reportResult(EchartsDto.Title title, EchartsDto echartsDto) {
		Map<String, Object> data = Maps.newHashMap();
		data.put("title", title);
		data.put("option", echartsDto);
		return ServerResponse.success(data);
	}

}
