package pers.msidolphin.mblog.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.helper.JsonHelper;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.service.ElasticsearchSearchService;

import java.io.IOException;

/**
 * Created by msidolphin on 2018/4/20.
 */
@Aspect
@Component
public class ElasticsearchAspect {

	@Autowired
	public ElasticsearchSearchService elasticsearchSearchService;

	private Logger log = LoggerFactory.getLogger(LogAspect.class);

	@Pointcut("execution(public * pers.msidolphin.mblog.admin.controller.ArticlesController.save(..))||execution(public * pers.msidolphin.mblog.admin.controller.ArticlesController.recover(..))")
	public void index(){}

	@Pointcut("execution(public * pers.msidolphin.mblog.admin.controller.ArticlesController.logicDelete(..))||execution(public * pers.msidolphin.mblog.admin.controller.ArticlesController.delete(..))")
	public void remove() {}

	@AfterReturning(value = "index()", returning = "retVal")
	public void createIndex(JoinPoint joinPoint, Object retVal) throws IOException {
		if(Util.isEmpty(retVal)) return;
		ServerResponse response = (ServerResponse) retVal;
		String methodName = joinPoint.getSignature().getName();
		String id = null;
		System.out.println("方法名：" + methodName + " 返回值:" + retVal);
		if("save".equals(methodName)) {
			Article article = (Article) response.getData();
			id = article.getArticleId();
		}else if("recover".equals(methodName)) {
			id = (String) response.getData();
		}
		System.out.println("返回值:"+ id);
		if(Util.isNotEmpty(id)) {
			log.debug("准备创建文章索引{}...", id);
			elasticsearchSearchService.index(id);
		}else {
			log.warn("保存文章信息方法返回为空...");
		}
	}

	@AfterReturning(value = "remove()", returning = "retVal")
	public void removeIndex(JoinPoint joinPoint, Object retVal) {
		if(Util.isEmpty(retVal)) return;
		ServerResponse response = (ServerResponse) retVal;
		String id = (String) response.getData();
		log.debug("准备删除文章索引{}...", id);
		elasticsearchSearchService.remove(id);
	}
}
