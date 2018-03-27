package pers.msidolphin.mblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.msidolphin.mblog.helper.AutoIdHelper;
import pers.msidolphin.mblog.model.repository.TagRepository;
import pers.msidolphin.mblog.object.po.Tag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    /***
     * 保存标签
     * @param tag
     * @return 标签对象
     */
    public Tag saveTag(Tag tag) {
        return saveTag(tag.getName());
    }

    /**
     * 批量保存多个标签
     * @param tags
     * @return
     */
    public List<Tag> saveTags(List<String> tags) {
        List<Tag> retTags = new ArrayList<>();
        for(String tag : tags) {
            retTags.add(saveTag(tag));
        }
        return retTags;
    }

    /**
     * 批量保存多个标签
     * @param tagString
     * @return 标签列表
     */
    public List<Tag> saveTags(String[] tagString) {
        List<Tag> tags = new ArrayList<>();
        for(String tag : tagString) {
            tags.add(saveTag(tag));
        }
        return tags;
    }

    /***
     * 根据标签名（唯一）保存标签
     * 若数据库中已存在同名的标签，那么只需要更新该标签的引用频率
     * 否则新增一个标签
     * @param tagName
     * @return 标签对象
     */
    public Tag saveTag(String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        if(tag != null) {
            tag.setFrequency(tag.getFrequency() + 1);
        }else {
            //新增
            tag = new Tag();
            tag.setFrequency(0);
            tag.setName(tagName);
            tag.setCreateTime(new Date());
            tag.setId(AutoIdHelper.getId());
        }
        //更新时间
        tag.setUpdateTime(new Date());
        return tagRepository.save(tag);
    }

    public void delTag(List<String> tags) {
        for(String tag : tags) {
            delTag(tag);
        }
    }

    public void delTag(String tagName) {
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
                tagRepository.save(tag);
            }
        }
    }



}
