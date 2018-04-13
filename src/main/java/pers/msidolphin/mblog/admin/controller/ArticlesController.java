package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.helper.JsonHelper;
import pers.msidolphin.mblog.object.dto.AdminArticleDto;
import pers.msidolphin.mblog.object.dto.ReportDto;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.query.ArticleQuery;
import pers.msidolphin.mblog.service.ArticleService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

/**
 * Created by msidolphin on 2018/3/26.
 */
@RestController
@RequestMapping("/admin/articles")
@SuppressWarnings("ALL")
public class ArticlesController {

	@Autowired
	private ArticleService articleService;

	@GetMapping("")
	public ServerResponse<?> list(ArticleQuery query) throws ParseException {
		System.out.println("isDelete:" + query.getIsDelete());
		return ServerResponse.success(articleService.getArticles(query));
	}

	@PostMapping("")
	public ServerResponse<?> save(@RequestParam(value = "article") 	String article,
									   @RequestParam(value = "originTags", required = false)  String originTags,
								       @RequestParam(value = "originTagsId", required = false)  String originTagsId) {
		try {
			AdminArticleDto articleObj = JsonHelper.string2Object(article, AdminArticleDto.class);
			articleService.saveArticle(articleObj, originTags, originTagsId);
			return ServerResponse.success(articleObj);
		} catch (IOException e) {
			return ServerResponse.internalError(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ServerResponse<?> get(@PathVariable  Long id) {
		AdminArticleDto article = articleService.getArticle(id);
		if(article != null) {
			return ServerResponse.success(article);
		}
		return ServerResponse.response(ResponseCode.NOT_FOUND.getCode(), ResponseCode.NOT_FOUND.getDescription());
	}

	/**
	 * 逻辑删除文章
	 * @param id 文章id
	 * @return
	 */
	@PutMapping("")
	public ServerResponse<?> logicDelete(@RequestBody Map<String, String> params) {
		articleService.logicDelete(params.get("id"));
		return ServerResponse.response(ResponseCode.NO_CONTENT);
	}

	@PostMapping("/{id}")
	public ServerResponse<?> recover(@PathVariable String id) {
		return ServerResponse.success(articleService.recoverArticle(id));
	}

	/**
	 * 物理删除文章
	 * @param id
	 * @return
	 */
	@DeleteMapping("")
	public ServerResponse<?> delete(@RequestBody Map<String, String> params) {
		articleService.delete(params.get("id"));
		return ServerResponse.response(ResponseCode.NO_CONTENT);
	}


	@GetMapping("/reports/line")
	public ServerResponse<?> report(ReportDto reportParam) {
		return articleService.articleReports(reportParam);
	}

	@GetMapping("/reports/pie")
	public ServerResponse<?> report() {
		return articleService.pieReports();
	}

}
