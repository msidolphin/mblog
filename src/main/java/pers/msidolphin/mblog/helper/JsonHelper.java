package pers.msidolphin.mblog.helper;

import com.google.common.collect.Lists;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import pers.msidolphin.mblog.helper.bean_helper.core.BeanHelper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 基于javckson 的Json处理工具类
 *
 * @author msidolphin [OoMass-EffectoO@hotmail.com]
 */
@SuppressWarnings("ALL")
public class JsonHelper {

	private static ObjectMapper objectMapper = new ObjectMapper();
	private static final JsonEncoding JSON_ENCODING = JsonEncoding.UTF8;

	static {
		//格式化
		objectMapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, false);
		//以下配置用于序列化时排除为空的字段
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
	}

	/**
	 * JavaBean 转Json 字符串
	 *
	 * @param target 目标对象
	 * @return String
	 * @throws IOException
	 */
	public static String object2String(Object target) {
		try {
			if (target == null) {
				return null;
			}
			return objectMapper.writeValueAsString(target);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Json字符串转换为JavaBean
	 *
	 * @param jsonString   json字符串
	 * @param requiredType java bean class对象
	 * @param <T>
	 * @return T
	 * @throws IOException
	 */
	public static <T> T string2Object(String jsonString, Class<T> requiredType) throws IOException {
		if (StringHelper.isBlank(jsonString)) {
			return null;
		}
		return objectMapper.readValue(jsonString, requiredType);
	}


	/**
	 * 将Map转换为JSON字符串
	 *
	 * @param jsonMap 待转换的Map
	 * @return JSON字符串
	 * @throws IOException
	 */
	public static String buildJsonWithMap(Map<?, ?> jsonMap) throws IOException {
		if (MapsHelper.isEmpty(jsonMap)) {
			return null;
		}
		return objectMapper.writeValueAsString(jsonMap);
	}

	/**
	 * 将Map转换为指定类型，不建议使用
	 *
	 * @param jsonMap
	 * @param requiredType
	 * @param <T>
	 * @return T
	 * @throws IOException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Deprecated
	public static <T> T buildJsonWithMap(Map<?, ?> jsonMap, Class<T> requiredType) throws IOException,
			IllegalAccessException, InstantiationException {
		if (MapsHelper.isEmpty(jsonMap)) {
			return null;
		}
		String jsonString = object2String(jsonMap);
		return string2Object(jsonString, requiredType);
	}


	/**
	 * 将List转换为JSON字符串
	 *
	 * @param list 待转换List
	 * @return JSON字符串
	 * @throws IOException
	 */
	public static String buildJsonWithList(List<?> list) throws IOException {
		if (list.isEmpty()) {
			return null;
		}
		return object2String(list);
	}

	/**
	 * 将JSON字符串转换为List返回
	 *
	 * @param jsonString   JSON字符串
	 * @param requiredType List元素类型
	 * @param <T>
	 * @return List<T>
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> buildListWithJsonString(String jsonString, Class<T> requiredType) throws IOException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (StringHelper.isBlank(jsonString)) {
			return null;
		}
		List<Map<String, ?>> parseReuslt = objectMapper.readValue(jsonString, List.class);
		List<T> result = Lists.newArrayList();
		for (Map<String, ?> jsonMap : parseReuslt) {
			result.add(BeanHelper.mapToBean(jsonMap, requiredType));
		}
		return result;
	}
}
