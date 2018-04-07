package pers.msidolphin.mblog.model.mapper;

import org.apache.ibatis.annotations.Param;
import pers.msidolphin.mblog.object.dto.AdminTagDto;
import pers.msidolphin.mblog.object.query.TagQuery;

import java.util.List;
import java.util.Map;

/**
 * Created by msidolphin on 2018/4/5.
 */
public interface TagMapper {

	List<AdminTagDto> findTags(TagQuery query);

	int updateTagById(@Param("id") String id,@Param("name") String name,@Param("updator") String updator);

	List<Map<String, Object>> findTagByArticleId(String id);

	int createRelationship(@Param("aid") String aid,@Param("tid") String tid);

	int brokenRelationship(@Param("aid") String aid,@Param("tid") String tid);

	List<Map<String, Integer>> frequencyBarReport(Integer limit);

	int selectTagCount();
}
