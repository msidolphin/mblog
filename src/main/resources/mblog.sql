-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 2018-05-12 15:22:08
-- 服务器版本： 5.7.14
-- PHP Version: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mblog`
--
CREATE DATABASE IF NOT EXISTS `mblog` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `mblog`;

-- --------------------------------------------------------

--
-- 表的结构 `article`
--

DROP TABLE IF EXISTS `article`;
CREATE TABLE IF NOT EXISTS `article` (
  `id` bigint(11) NOT NULL COMMENT '主键',
  `title` varchar(60) NOT NULL COMMENT '文章标题',
  `tags` varchar(100) DEFAULT NULL COMMENT '标签',
  `type` tinyint(11) NOT NULL COMMENT '类型 0-原创 1-转载 2-翻译',
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图',
  `content` mediumtext NOT NULL COMMENT '文章内容',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '文章是否删除 0-否 1-是',
  `views` int(11) DEFAULT '0' COMMENT '文章查阅数',
  `cid` int(11) DEFAULT NULL COMMENT '分类id',
  `editor` tinyint(4) DEFAULT '0' COMMENT '编辑器类型 0-富文本 1-markdown',
  `summary` varchar(255) DEFAULT NULL COMMENT '文章摘要',
  `vote` int(11) DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章表';


-- --------------------------------------------------------

--
-- 表的结构 `article_tag`
--

DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE IF NOT EXISTS `article_tag` (
  `aid` bigint(20) NOT NULL,
  `tid` bigint(20) NOT NULL,
  PRIMARY KEY (`aid`,`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `article_id` bigint(20) NOT NULL COMMENT '所属文章id',
  `user_id` bigint(20) NOT NULL COMMENT '评论人id',
  `content` text NOT NULL COMMENT '评论内容',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '评论状态 1-正常 0-删除',
  `vote` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';


-- --------------------------------------------------------

--
-- 表的结构 `links`
--

DROP TABLE IF EXISTS `links`;
CREATE TABLE IF NOT EXISTS `links` (
  `id` bigint(11) NOT NULL COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '链接名',
  `url` varchar(255) CHARACTER SET utf32 NOT NULL COMMENT 'URL',
  `sort` int(11) NOT NULL COMMENT '排序号',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='友情链接';


-- --------------------------------------------------------

--
-- 表的结构 `oauth`
--

DROP TABLE IF EXISTS `oauth`;
CREATE TABLE IF NOT EXISTS `oauth` (
  `id` bigint(11) NOT NULL COMMENT '主键',
  `uid` int(11) NOT NULL COMMENT '关联系统用户表',
  `oauth_id` varchar(255) NOT NULL COMMENT '第三方用户id',
  `access_token` varchar(255) NOT NULL COMMENT 'access_token',
  `username` varchar(255) NOT NULL COMMENT '第三方用户名',
  `bind_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
  `type` tinyint(4) NOT NULL COMMENT '三方类型 0-微信 1-QQ 2-Github 3-新浪',
  `avatar` varchar(255) NOT NULL COMMENT '第三方用户头像',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方认证表';

-- --------------------------------------------------------

--
-- 表的结构 `replies`
--

DROP TABLE IF EXISTS `replies`;
CREATE TABLE IF NOT EXISTS `replies` (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '主键',
  `comment_id` bigint(20) UNSIGNED NOT NULL COMMENT '回复所属的评论id',
  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '回复人id',
  `to_user_id` bigint(20) DEFAULT NULL COMMENT '回复给',
  `content` text NOT NULL COMMENT '回复内容',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 1-正常 0-删除',
  `vote` int(11) NOT NULL COMMENT '点赞数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '回复时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='回复表';


-- --------------------------------------------------------

--
-- 表的结构 `setting`
--

DROP TABLE IF EXISTS `setting`;
CREATE TABLE IF NOT EXISTS `setting` (
  `id` bigint(11) NOT NULL COMMENT '主键',
  `key_value` varchar(255) NOT NULL COMMENT '配置项key值',
  `value` text NOT NULL COMMENT '配置项值',
  `name` varchar(255) NOT NULL COMMENT '显示名称',
  `type` tinyint(4) NOT NULL COMMENT '类型 0-门户 1-后台 2-公共',
  `is_image` tinyint(4) NOT NULL COMMENT '是否为图片 0-否 1-是',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `setting_value_unqiue` (`key_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站点配置表';



-- --------------------------------------------------------

--
-- 表的结构 `tags`
--

DROP TABLE IF EXISTS `tags`;
CREATE TABLE IF NOT EXISTS `tags` (r
  `id` bigint(11) NOT NULL COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '标签名称',
  `frequency` int(11) NOT NULL COMMENT '引用频率',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';


--
-- 表的结构 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `username` varchar(30) NOT NULL COMMENT '用户名',
  `nickname` varchar(30) DEFAULT NULL COMMENT '昵称',
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `salt` varchar(120) DEFAULT NULL COMMENT '盐值',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `website` varchar(255) DEFAULT NULL COMMENT '个人网址',
  `login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `login_ip` varchar(40) DEFAULT NULL COMMENT '最后登录ip',
  `status` tinyint(4) DEFAULT '0' COMMENT '状态 0-启用 1-禁用',
  `is_admin` tinyint(4) DEFAULT '0' COMMENT '是否是博主 1-是 0-否',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `summary` varchar(400) CHARACTER SET utf32 DEFAULT NULL COMMENT '介绍',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_username_email_u` (`username`,`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `username`, `nickname`, `password`, `salt`, `avatar`, `email`, `phone`, `website`, `login_time`, `login_ip`, `status`, `is_admin`, `update_time`, `summary`, `create_time`, `creator`, `updator`) VALUES(1, 'msidolphin', 'msidolphin', '0AD7EB6ECA6DF5AC7B9D5707CECF84AE', 'MIIEowIBAAKCAQEAsaDDhOsMR4vgwUFhHjR9lvhTU7eRjLruMCtWQ3l+ACIB7xbF', '2018/04/T/jpg/2AA42FC337DC353F6F8B3AF0346D3682.jpg', '1226771922@qq.com', '15363393934', NULL, NULL, NULL, 0, 1, '2018-04-06 17:37:36', NULL, '2018-03-29 22:00:45', NULL, NULL);


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
