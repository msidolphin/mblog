package pers.msidolphin.mblog.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.ServiceException;


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
}