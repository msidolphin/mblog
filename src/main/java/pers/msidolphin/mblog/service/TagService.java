package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ResponseCode;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.helper.RequestHolder;
import pers.msidolphin.mblog.helper.Util;
import pers.msidolphin.mblog.model.mapper.TagMapper;
import pers.msidolphin.mblog.model.repository.TagRepository;
import pers.msidolphin.mblog.object.dto.AdminTagDto;
import pers.msidolphin.mblog.object.po.Tag;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.TagQuery;

import java.util.*;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    public ServerResponse<?> getTags(TagQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize(), query.getOrder());
        PageInfo<AdminTagDto> pageInfo = new PageInfo<>(tagMapper.findTags(query));
        return ServerResponse.success(pageInfo);
    }

    /**
     * 批量保存多个标签
     * @param tags
     * @return
     */
    @Transactional
    public List<String> saveTags(List<String> tags, Long uid) {
        List<String> newIds = new ArrayList<>();
        for(String tag : tags) {
            String id = saveTag(tag, uid);
            if (id != null) newIds.add(id);
        }
        return newIds;
    }

    /**
     * 本方法为新增文章提供，返回所有的标签id
     * @param tags
     * @param uid
     * @return
     */
    public List<String> saveTags4newArticle(List<String> tags, Long uid) {
        List<String> ids = new ArrayList<>();
        for(String tag : tags) {
            ids.add(saveTag4newArticle(tag, uid));
        }
        return ids;
    }

    private String saveTag4newArticle(String tagName, Long uid) {
        Tag tag = tagRepository.findByName(tagName);
        if(tag != null) {
            tag.setFrequency(tag.getFrequency() + 1);
        }else {
            //新增
            tag = new Tag();
            tag.setFrequency(1);
            tag.setName(tagName);
            tag.setCreateTime(new Date());
            tag.setId(AutoIdHelper.getId());
            tag.setCreator(uid);
        }
        tag.setUpdator(uid);
        tag.setUpdateTime(new Date());
        tagRepository.save(tag);
        return tag.getId().toString();
    }

    /**
     * 批量保存多个标签
     * @param tagString
     * @return 新增标签id列表
     */
    public List<String> saveTags(String[] tagString, Long uid) {
        List<String> newIds = new ArrayList<>();
        for(String tag : tagString) {
            String id = saveTag(tag, uid);
            if(id != null) newIds.add(id);
        }
        return newIds;
    }



    /***
     * 根据标签名（唯一）保存标签
     * 若数据库中已存在同名的标签，那么只需要更新该标签的引用频率
     * 否则新增一个标签
     * @param tagName
     * @return 新增的标签id
     */
    public String saveTag(String tagName, Long currentUserId) {
        boolean isAdd = false;
        Tag tag = tagRepository.findByName(tagName);
        if(tag != null) {
            tag.setFrequency(tag.getFrequency() + 1);
        }else {
            isAdd = true;
            //新增
            tag = new Tag();
            tag.setFrequency(1);
            tag.setName(tagName);
            tag.setCreateTime(new Date());
            tag.setId(AutoIdHelper.getId());
            tag.setCreator(currentUserId);
        }
        tag.setUpdator(currentUserId);
        tag.setUpdateTime(new Date());
        tagRepository.save(tag);
        if (isAdd) {
            return tag.getId().toString();
        }
        return null;
    }

    public void delTag(List<String> tags, Long uid) {
        for(String tag : tags) {
            delTag(tag, uid);
        }
    }

    public void delTag(String tagName, Long uid) {
        Tag tag = tagRepository.findByName(tagName);
        if(tag == null) {
            return;
        }else {
            int newCount = tag.getFrequency() - 1;
            if(newCount <= 0) {
                //该标签引用数量为0，物理删除即可
                tagRepository.deleteByName(tagName);
                return;
            }else {
                //引用数量-1
                tag.setFrequency(newCount);
                tag.setUpdator(uid);
                tagRepository.save(tag);
            }
        }
    }

    @Transactional
    public int createRelationship(String aid, String tid) {
        return tagMapper.createRelationship(aid, tid);
    }

    @Transactional
    public int brokenRelationship(String aid, String tid) {
        return tagMapper.brokenRelationship(aid, tid);
    }

    public List<Map<String, Object>> getTags(String aid) {
        return tagMapper.findTagByArticleId(aid);
    }

    public String getTagIdByName(String name) {
        Tag tag = tagRepository.findByName(name);
        if(tag == null) return null;
        return tag.getId().toString();
    }

    @Transactional
    public ServerResponse<?> updateTagNameById(String id, String name) {
        User user = RequestHolder.getCurrentAdmin();
        //用户未登录
        if(Util.isEmpty(user)) return ServerResponse.unauthorized();
        if(Util.isEmpty(id) || Util.isEmpty(name)) throw new InvalidParameterException("标签id或标签名称不能为空");
        //检查该标签是否存在
        Tag tag = tagRepository.findByName(name);
        //存在冲突
        if (tag != null) return ServerResponse.response(ResponseCode.CONFLICT);
        tagMapper.updateTagById(id, name, user.getId().toString());
        return ServerResponse.success(name);
    }
}
