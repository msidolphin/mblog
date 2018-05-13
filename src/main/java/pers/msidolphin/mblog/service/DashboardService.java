package pers.msidolphin.mblog.service;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.dto.DashboardDto;
import pers.msidolphin.mblog.object.dto.EchartsDto;
import pers.msidolphin.mblog.object.dto.ReportDto;

import java.util.Map;

/**
 * Created by msidolphin on 2018/4/7.
 */
@Service
public class DashboardService {

	@Autowired
	private ArticleService articleService;

	@Autowired
	private TagService tagService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	public ServerResponse<?> getDashboardData() {
		DashboardDto dashboardDto = new DashboardDto();
		//获取文章数量
		dashboardDto.setArticleCount(articleService.getArticleCount());
		//获取标签数量
		dashboardDto.setTagCount(tagService.getTagCount());
		//获取评论数量
		dashboardDto.setCommentCount(commentService.getCommentCount());
		//获取用户数量
		dashboardDto.setUserCount(userService.getUserCount());
		//获取统计报表 首页只统计本年度
		ReportDto reportDto = new ReportDto();
		reportDto.setChartType(1);
		reportDto.setVertical(true);
		ServerResponse<?> response = articleService.articleReports(reportDto);
		Map<String, Object> reportsMap = Maps.newHashMap();
		if (response.getData() != null) reportsMap = (Map<String, Object>) response.getData();
		Map<String, Object> echartsData = Maps.newHashMap();
		echartsData.put("dashboard-bar", reportsMap);
		dashboardDto.setEchartsData(echartsData);
		return ServerResponse.success(dashboardDto);
	}

}
