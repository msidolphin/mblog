package pers.msidolphin.mblog.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Order(5)
@Configuration
@EnableAutoConfiguration
public class RedisConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName("localhost");
		redisStandaloneConfiguration.setDatabase(0);
		redisStandaloneConfiguration.setPort(6379);
		JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration);
		return factory;
	}

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(10);
		jedisPoolConfig.setMinIdle(2);
		return jedisPoolConfig;
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(factory);
		RedisSerializer redisSerializer = new StringRedisSerializer();
		redisTemplate.setKeySerializer(redisSerializer);
		redisTemplate.setValueSerializer(redisSerializer);
		redisTemplate.setHashKeySerializer(redisSerializer);
		redisTemplate.setHashValueSerializer(redisSerializer);
		return redisTemplate;
	}
}
