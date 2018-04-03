package pers.msidolphin.mblog.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.helper.JsonHelper;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.query.ArticleQuery;
import pers.msidolphin.mblog.service.ArticleService;

import java.io.IOException;
import java.text.ParseException;

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
		return ServerResponse.success(articleService.getArticles(query));
	}

	@PostMapping("")
	public ServerResponse<?> add(@RequestParam(value = "article") 	String article,
									   @RequestParam(value = "originTags", required = false)  String originTags) {
		try {
			Article articleObj = JsonHelper.string2Object(article, Article.class);
			articleService.saveArticle(articleObj, originTags);
			return ServerResponse.success(articleObj);
		} catch (IOException e) {
			return ServerResponse.internalError(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ServerResponse<?> get(@PathVariable  Long id) {
		Article article = articleService.getArticle(id);
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
	public ServerResponse<?> logicDelete(String id) {
		articleService.logicDelete(id);
		return ServerResponse.response(ResponseCode.NO_CONTENT);
	}

	/**
	 * 物理删除文章
	 * @param id
	 * @return
	 */
	@DeleteMapping("")
	public ServerResponse<?> delete(String id) {
		articleService.delete(id);
		return ServerResponse.response(ResponseCode.NO_CONTENT);
	}

}
