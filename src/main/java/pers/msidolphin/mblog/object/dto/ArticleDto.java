package pers.msidolphin.mblog.object.dto;

import lombok.Getter;
import lombok.Setter;
import pers.msidolphin.mblog.object.po.Article;

@Getter
@Setter
public class ArticleDto{

    private Article article;

    //原来的标签
    private String originTags;
}
