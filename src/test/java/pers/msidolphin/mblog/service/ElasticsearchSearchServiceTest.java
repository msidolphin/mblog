package pers.msidolphin.mblog.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.object.query.ArticleSearch;


/**
 * Created by msidolphin on 2018/4/20.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchSearchServiceTest {

	@Autowired
	public ElasticsearchSearchService service;

	@Test
	public void indexSuccess() {
		String id = "433737094465060864";
		boolean result = service.index(id);
		Assert.assertTrue(result);
	}

	@Test
	public void indexFailed() {
		String id = "aaa";
		try {
			service.index(id);
		}catch (ServiceException e) {
			if(e.getCode() == ResponseCode.NOT_FOUND.getCode()) {
				Assert.assertFalse(false);
				return;
			}
		}
		Assert.assertFalse(true);
	}

	@Test
	public void removeSuccess() {
		String id = "433737094465060864";
		service.index(id);
		boolean result = service.remove(id);
		Assert.assertTrue(result);
	}

	@Test
	public void removeFailed() {
		String id = "aaa";
		boolean result = service.remove(id);
		Assert.assertFalse(result);
	}

	@Test
	public void search() {
		String title = "elasticsearch 基础";
		String sumamry = "elasticsearch 基础";
		ArticleSearch search = new ArticleSearch();
		search.setSummary(sumamry);
		search.setTitle(title);
		search.setPageNum(1);
		search.setPageSize(10);
		ServerResponse response = service.search(search);
		System.out.println("response => " + response);
		Assert.assertNotNull(response);
	}
}