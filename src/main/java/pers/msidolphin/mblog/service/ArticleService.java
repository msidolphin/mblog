package pers.msidolphin.mblog.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pers.msidolphin.mblog.common.ServerResponse;
import pers.msidolphin.mblog.common.enums.ArticleType;
import pers.msidolphin.mblog.common.enums.MonthType;
import pers.msidolphin.mblog.common.enums.ReportType;
import pers.msidolphin.mblog.exception.AuthorizedException;
import pers.msidolphin.mblog.exception.InvalidParameterException;
import pers.msidolphin.mblog.exception.ServiceException;
import pers.msidolphin.mblog.helper.*;
import pers.msidolphin.mblog.model.mapper.ArticleMapper;
import pers.msidolphin.mblog.model.repository.ArticleRepository;
import pers.msidolphin.mblog.object.dto.*;
import pers.msidolphin.mblog.object.po.Article;
import pers.msidolphin.mblog.object.po.User;
import pers.msidolphin.mblog.object.query.ArticleQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

@Service
@SuppressWarnings("ALL")
public class ArticleService extends BaseService{

    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    @Autowired
    private RedisHelper redis;

    @Autowired
    private PropertiesHelper propertiesHelper;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    private static final String DEFAULT_VIEWS_PREFIX = "articleViewsCache:";
    private static final Long   DEFAULT_VIEWS_TIMEOUT = 900000L;

    public PageInfo<ArticleDto> getArticles(ArticleQuery query) throws ParseException {
        Assert.notNull(query);
        //处理查询对象中的结束时间
        if(Util.isNotEmpty(query.getEndTime())) {
            query.setEndTime(query.getEndTime() + " 23:59:59");
        }
        if(Util.isNotEmpty(query.getTags())) {
            //将标签字符串转换为List
//            query.setTagList(Util.asList(query.getTags().split(",")));
            String[] tags = query.getTags().split(",");

            //获取效应标签的id
            List<String> ids = Lists.newArrayList();
            for(String tag : tags) {
                String id = tagService.getTagIdByName(tag);
                //该标签不存在，已经不需要继续查询了
                if (id == null) return new PageInfo<>(Lists.newArrayList());
                ids.add(id);
            }
            query.setTagIdList(ids);
        }else {
            query.setTags(null);
        }
        //设置分页参数
        PageHelper.startPage(query.getPageNum(), query.getPageSize(), "a.create_time desc");
        List<ArticleDto> lists = articleMapper.findArticles(query);
        //获取标签
        for(ArticleDto articleDto : lists) {
            List<Map<String, Object>> tags = tagService.getTags(articleDto.getId());
            StringBuffer tagsName = new StringBuffer();
            for(Map<String, Object> tag : tags) {
                tagsName.append(tag.get("name").toString()).append(",");
            }
            articleDto.setTags(tagsName.substring(0, tagsName.lastIndexOf(",")));
        }
        return new PageInfo<>(lists);
    }

    @Transactional
    public Article saveArticle(AdminArticleDto articleDto, String originTagStr, String originTagId) {
        //判断用户是否登录
        User user = RequestHolder.getCurrentAdmin();
        if(user == null) throw new AuthorizedException();
        Article article = new Article();
        BeanUtils.copyProperties(articleDto,article);
        //判断id是否为空，若id
        String id = article.getId();

        List<String> dels;
        List<String> adds;

        //维护文章表与标签表的关系
        List<String> delIds;
        List<String> addIds;

        if(Util.isEmpty(id)) {
            //新增操作
            article.setId(AutoIdHelper.getId().toString());
            //判断是否新增标签
            if(Util.isNotEmpty(articleDto.getTags())) {
                //tags字段现在不存入数据库
                List<String> ids = tagService.saveTags4newArticle(Util.asList(articleDto.getTags().split(",")), user.getId());
                for(String nid : ids) {
                    //建立关联
                    tagService.createRelationship(article.getId().toString(), nid);
                }
            }
            article.setCreator(user.getId());
            article.setUpdator(user.getId());
            article.setUpdateTime(new Date());
            article.setCreateTime(new Date());
            article.setCid(null);
            article.setIsDelete(0);
            article.setViews(0);
            article.setVote(0);
            article.setSummary("");
            //====
            //保存文章
            articleRepository.save(article);

        }else {
            //判断当前标签和原标签的差异
            List<String> originTags = Util.asList(originTagStr.split(","));
            List<String> currentTags = Util.asList(articleDto.getTags().split(","));
            //删除的
            dels = Util.diff(originTags, currentTags);
            //新增的
            adds = Util.diff(currentTags, originTags);

            //add
            if(originTagId == null) originTagId = "";
            if(articleDto.getTagsId() == null) articleDto.setTags("");
            List<String> originTagIds = Util.asList(originTagId.split(","));
            List<String> currentTagIds = Util.asList(articleDto.getTagsId().split(","));
            //===========

            delIds = Util.diff(originTagIds, currentTagIds);
            addIds = Util.diff(currentTagIds, originTagIds);

            if(dels.size() > 0) {
                //删除标签
                tagService.delTag(dels, user.getId());

                //打破文章与标签的原有关系
                for(int i = 0 ; i < delIds.size() ; ++i)
                    tagService.brokenRelationship(article.getId().toString(), delIds.get(i));

            }
            if(adds.size() > 0) {
                //新增的
                List<String> newIds = tagService.saveTags(adds, user.getId());
                for(String newId : newIds) {
                    addIds.add(newId);
                }
                //建立关联
                for(int i = 0 ; i < addIds.size() ; ++i)
                    tagService.createRelationship(article.getId().toString(), addIds.get(i));

            }
            article.setUpdator(user.getId());
            articleMapper.updateById(article);
        }
        return article;
    }

    /**
     * 根据文章id获取文章
     * @param id
     * @return
     */
    public AdminArticleDto getArticle(Long id) {
        if(Util.isEmpty(id)) {
            throw new InvalidParameterException("文章id不能为为空");
        }
        Optional<Article> optionalArticle = articleRepository.findById(id.toString());
        Article article = optionalArticle.get();
        //获取文章标签
        AdminArticleDto adminArticleDto = new AdminArticleDto();
        BeanUtils.copyProperties(article, adminArticleDto);
        //查询文章标签
        List<Map<String, Object>> tags = tagService.getTags(article.getId());
        System.out.println("tags:"+ tags);
        StringBuffer tagsName = new StringBuffer();
        StringBuffer tagsId = new StringBuffer();
        for(Map<String, Object> tag : tags) {
            tagsName.append(tag.get("name").toString()).append(",");
            tagsId.append(tag.get("id").toString()).append(",");
        }
        adminArticleDto.setTagsId(tagsId.substring(0, tagsId.lastIndexOf(",")));
        adminArticleDto.setTags(tagsName.substring(0, tagsName.lastIndexOf(",")));
        return adminArticleDto;
    }

    /**
     * 逻辑删除文章
     * @param id 文章id
     * @return {int} 影响行数
     */
    @Transactional
    public int logicDelete(String id) {
        return articleRepository.updateStatusById(1, id);
    }


    /**
     * 物理删除文章
     * @param id 文章id
     */
    @Transactional
    public void delete(String id) {
        if(Util.isEmpty(id)) throw new InvalidParameterException("文章id不能为空");
        articleRepository.deleteById(id);
    }


    /**
     * 获取文章详情 包含评论信息
     * @param id 文章id
     * @return ArticleDto
     */
    public ArticleDto getArticleDetail(String id) {
        Assert.notNull(id);
        List<ArticleDto> articleDtos = articleMapper.findArticles(new ArticleQuery().setId(id));
        ArticleDto articleDto = null;
        if(Util.isNotEmpty(articleDtos) && articleDtos.size() == 1) {

            articleDto = articleDtos.get(0);
            //文章类型
            articleDto.setTypeName(ArticleType.get(articleDto.getTypeCode()).getValue());
            //获取评论信息
            PageInfo<CommentDto> comments = commentService.getComments(id, 10, 1);

            Integer replies = articleMapper.selectArtcileCommentAndReplyCount(articleDto.getId());
            //获取评论和回复数
            articleDto.setReplies(replies != null ? replies : 0);
            articleDto.setCommentList(comments);

            //获取文章标签
            String tags = getArticleTags(articleDto.getId());
            articleDto.setTags(tags);
        }else {
            //文章不存在
            throw new ServiceException("文章不存在或返回多个文章记录");
        }
        return articleDto;
    }

    public boolean articleViewsHandle(String id, HttpServletRequest request) {

        String viewsPrefix = propertiesHelper.getValue("blog.article.ip.cache.prefix");

        Long viewsTimeout = propertiesHelper.getLong("blog.article.ip.cache.timeout");

        if(viewsPrefix == null) viewsPrefix = DEFAULT_VIEWS_PREFIX;
        if(viewsTimeout == null) viewsTimeout = DEFAULT_VIEWS_TIMEOUT;

        //获取ip地址
        String ipAddr = IpAddressHelper.getIpAddress(request);
        log.info("ip地址为{}的游客访问了文章{}", ipAddr, id);
        //从redis中获取ip地址对应的访问列表
        if(redis.getValue(viewsPrefix + ipAddr + id) == null) {
            //文章阅览数自增1
            articleRepository.incrementsViewsById(id);
            //文章阅览数新增一条
            redis.setValue(viewsPrefix + ipAddr + id,
                    JsonHelper.object2String(true), viewsTimeout);
        }else {
            return false;
        }
        return true;
    }

    @Deprecated
    private Specification<Article> where(ArticleQuery query) {
        return new Specification<Article>() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Article> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {

                List<Predicate> predicates = new ArrayList<>();

                if(Util.isNotEmpty(query)) {

                    if(Util.isNotEmpty(query.getTitle())) {
                        //查询条件标题不为空
                        predicates.add(cb.like(root.get("title").as(String.class), "%" + query.getTitle() + "%"));
                    }

                    if(Util.isNotEmpty(query.getStartTime())) {
                        //发布时间 开始
                        //predicates.add(cb.greaterThan(root.get("createTime").as(Date.class), query.getStartTime()));
                    }

                    if(Util.isNotEmpty(query.getEndTime())) {
                        //发布时间 结束
//                        Date endTime = query.getEndTime();
//                        predicates.add(cb.lessThan(root.get("createTime").as(Date.class), query.getEndTime()));
                    }

                    if(Util.isNotEmpty(query.getTags())) {
                        //标签
                        String[] tags = query.getTags().split(",");
                        for(String tag : tags) {
                            predicates.add(cb.like(root.get("tags").as(String.class), "%" + tag + "%"));
                        }
                    }
                }
                Predicate[] pres = new Predicate[predicates.size()];
                return criteriaQuery.where(predicates.toArray(pres)).getGroupRestriction();
            }
        };
    }

    /**
     * 文章相关统计
     * 分为按年 按月统计
     * @param report {ReportDto}
     * @return
     */
    public ServerResponse<?> articleReports(ReportDto report) {
        Integer type = report.getType();
        if (report.getChartType() == null) report.setChartType(0); //默认为折线图

        String chartType = "line";
        if(report.getChartType() == 1) chartType = "bar";

        if (type == null) {
            //默认按月统计
            type = ReportType.MONTH.getKey();
        }
        EchartsDto echartsDto = new EchartsDto();
        EchartsDto.Legend legend = echartsDto.new Legend();
        EchartsDto.Title title = echartsDto.new Title();
        EchartsDto.xAxis xAxis = echartsDto.new xAxis();
        EchartsDto.yAxis yAxis = echartsDto.new yAxis();
        List<EchartsDto.Series> seriesList = Lists.newArrayList();

        //图例
        legend.setOrient("horizontal");
        legend.setTop("bottom");
        legend.setLeft("center");
        List<Object> legendData = Lists.newArrayList();

        Map<String, EchartsDto.Series> seriesMap = Maps.newHashMap();
        for(ArticleType articleType : ArticleType.values()) {
            //图例数据
            legendData.add(articleType.getValue());

            //series初始化数据
            EchartsDto.Series series = echartsDto.new Series();
            series.setName(articleType.getValue());
            series.setType(chartType);
            series.setStack("发布量");
            seriesMap.put(articleType.getValue(), series);
        }
        //x轴
        if(!report.isVertical()) {
            xAxis.setType("category");
            //y轴
            yAxis.setType("value");
        }else {
            yAxis.setType("category");
            xAxis.setType("value");
        }
        List<Object> axisData = Lists.newArrayList();

        List<Map<String, Integer>> result = Lists.newArrayList();
        switch (type) {
            case 0: //按月
                //判断是否指定统计年份，若未指定，默认为当前年份
                Integer year;
                Integer maxMonth = 12;  //默认统计1-12月
                List<String> months = Lists.newArrayList();
                Calendar calendar = Calendar.getInstance();
                Integer now;
                now = calendar.get(Calendar.YEAR);
                if ((Util.isNotEmpty(report.getSpecific()) && report.getSpecific().equals(now.toString())) || Util.isEmpty(report.getSpecific())) {
                    maxMonth = calendar.get(Calendar.MONTH) + 1;  //JANUARY> which is 0
                    year = now;
                }else year = Integer.parseInt(report.getSpecific());
                //计算统计月份范围
                for(Integer i = 1 ; i <= maxMonth   ; ++i) {
                    months.add(i.toString());
                    //x轴
                    axisData.add(MonthType.getChinese(i) + "月");
                }
                //判断图表是否为垂直方向显示 即图例在x轴
                if (!report.isVertical()) xAxis.setData(axisData);
                else yAxis.setData(axisData);
                result = articleMapper.monthlyReport(year.toString(), maxMonth, months);
                //统计标题
                StringBuffer titleStr = new StringBuffer(year);
                if (maxMonth != 1) {
                    titleStr.append(year + "年").append(MonthType.getChinese(1) + "月份到" + MonthType.getChinese(maxMonth) + "月份")
                            .append("文章发布量统计报表");
                }else {
                    titleStr.append("年份").append(MonthType.getChinese(1) + "月份")
                            .append("文章发布量统计报表");
                }
                title.setText(titleStr.toString());

                //series
                for(Map<String, Integer> res : result) {
                    EchartsDto.Series series = seriesMap.get(ArticleType.get(res.get("type")).getValue());
                    if (series != null) {
                        List<Object> data = Lists.newArrayList();
                        for(Integer i = 1 ; i <= maxMonth ; ++i) data.add(res.get(i.toString()));
                        series.setData(data);
                    }
                }
                //再次遍历seriesMap 检索是否存在data为null的情况 如果为null说明未统计出该分类的情况，全部置为0
                for(String key : seriesMap.keySet()) {
                    EchartsDto.Series series = seriesMap.get(key);
                    if (series.getData() == null || series.getData().size() == 0) {
                        List<Object> data = Lists.newArrayList();
                        for(Integer i = 1  ; i <= maxMonth   ; ++i) {
                            data.add(0);
                        }
                        series.setData(data);
                    }
                    seriesList.add(series);
                }
                break;
            case 1: //按年
                //按年必须指定统计范围
                if(Util.isEmpty(report.getStart()) || Util.isEmpty(report.getEnd())) return ServerResponse.badRequest("按年统计必须指定统计范围");
                if(report.getStart() == report.getEnd()) return ServerResponse.badRequest("开始年份必须小于结束年份");
                List<String> years = Lists.newArrayList();
                for(Integer i = report.getStart() ; i <= report.getEnd() ; ++i ) {
                    years.add(i.toString());

                    //x轴
                    axisData.add(i.toString());
                }
                xAxis.setData(axisData);
                //title
                StringBuffer sb = new StringBuffer();
                sb.append(report.getStart() + "年到" + report.getEnd() + "年文章发布量统计报表");
                title.setText(sb.toString());

                //开始统计
                result = articleMapper.yearReport(report.getStart(), report.getEnd(), years);

                //series
                for(Map<String, Integer> res : result) {
                    EchartsDto.Series series= seriesMap.get(ArticleType.get(res.get("type")).getValue());
                    if(series != null) {
                        List<Object> data = Lists.newArrayList();
                        for(Integer i = report.getStart(); i <= report.getEnd() ; ++i) {
                            data.add(res.get(i.toString()));
                        }
                        series.setData(data);
                    }
                }

                //未统计到年份数据归零
                for(String key : seriesMap.keySet()) {
                    EchartsDto.Series series = seriesMap.get(key);
                    if(series.getData() == null || series.getData().size() == 0) {
                        List<Object> data = Lists.newArrayList();
                        for(int i = report.getStart() ; i <= report.getEnd() ; ++i) data.add(0);
                        series.setData(data);
                    }
                    seriesList.add(series);
                }

                break;
            case 2: //TODO 按周
                break;
        }

        legend.setData(legendData);
//        echartsDto.setTitle(title);  //标题单独设置
        echartsDto.setLegend(legend);
        echartsDto.setXAxis(Util.asList(xAxis));
        echartsDto.setYAxis(Util.asList(yAxis));
        echartsDto.setSeries(seriesList);

        return reportResult(title, echartsDto);
    }

    /**
     * 饼状图统计
     * @return
     */
    public ServerResponse<?> pieReports() {
        EchartsDto echartsDto = new EchartsDto();

        EchartsDto.Legend legend = echartsDto.new Legend();
        EchartsDto.Title title = echartsDto.new Title();

        //series初始化数据
        EchartsDto.Series series = echartsDto.new Series();
        series.setName("创作类型");
        series.setType("pie");
        List<Map<String, Object>> seriesData = Lists.newArrayList();

        legend.setOrient("vertical");
        legend.setTop("top");
        List<Object> legendData = Lists.newArrayList();

        for(ArticleType articleType : ArticleType.values()) {
            //图例数据
            legendData.add(articleType.getValue());
            Map<String, Object> obj = Maps.newHashMap();
            obj.put("name", articleType.getValue());
            obj.put("value", 0);
            seriesData.add(obj);
        }
        legend.setData(legendData);

        title.setText("文章发布类型饼状图");

        List<Map<String, Integer>> result = articleMapper.pieReport();
        for(Map<String, Integer> res : result) {
            String type = ArticleType.get(res.get("type")).getValue();
            for(Map<String, Object> obj : seriesData ) {
                if (obj.get("name").equals(type)) obj.put("value", res.get("count"));
            }
        }

        series.setData(seriesData);
        echartsDto.setSeries(Util.asList(series));
        echartsDto.setLegend(legend);

        return reportResult(title, echartsDto);
    }

    public int getArticleCount() {
        return articleMapper.selectArticleCount();
    }

    private String getArticleTags(String id) {
        //查询文章标签
        List<Map<String, Object>> tags = tagService.getTags(id);
        System.out.println("tags:"+ tags);
        StringBuffer tagsName = new StringBuffer();
        StringBuffer tagsId = new StringBuffer();
        for(Map<String, Object> tag : tags) {
            tagsName.append(tag.get("name").toString()).append(",");
        }
        return tagsName.substring(0, tagsName.lastIndexOf(","));
    }

}
