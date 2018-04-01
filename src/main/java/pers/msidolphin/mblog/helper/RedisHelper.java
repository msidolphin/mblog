package pers.msidolphin.mblog.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 * Created by msidolphin on 2018/3/31.
 */
@Component
public class RedisHelper {

	private RedisTemplate<String, String> redisTemplate;

	public RedisHelper setValue(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
		return this;
	}

	@Autowired
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		RedisSerializer redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setValueSerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		this.redisTemplate = redisTemplate;
	}

	public RedisHelper setValue(String key, String value, Long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
		return this;
	}

	public String getValue(String key) {
		return redisTemplate.opsForValue().get(key);
	}

}
