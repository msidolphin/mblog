package pers.msidolphin.mblog.portal.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.object.dto.ArticleDto;
import pers.msidolphin.mblog.object.dto.ReportDto;
import pers.msidolphin.mblog.object.query.ArticleQuery;
import pers.msidolphin.mblog.service.ArticleService;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

/**
 * 门户 文章控制器
 * Created by msidolphin on 2018/3/26.
 */
@RestController("portalArticlesController")
@RequestMapping("/articles")
public class ArticlesController {

	@Autowired
	private ArticleService articleService;

	/**
	 * 文章列表
	 * @param query {ArticleQuery} 文章查询对象
	 * @return ServerResponse<?>
	 */
	@GetMapping("")
	public ServerResponse<?> list(ArticleQuery query) throws ParseException {
		PageInfo<ArticleDto> page = articleService.getArticles(query);
		return ServerResponse.success(page);
	}

	/**
	 * 文章详情
	 * @param id {String} 文章id
	 * @return ServerResponse<?>
	 */
	@GetMapping("/detail/{id}")
	public ServerResponse<?> detail(HttpServletRequest request, @PathVariable String id) {
		//处理文章访问量
		articleService.articleViewsHandle(id, request);
		return ServerResponse.success(articleService.getArticleDetail(id));
	}


	@GetMapping("/reports")
	public ServerResponse<?> report(ReportDto reportParam) {
		return articleService.articleReports(reportParam);
	}
}
