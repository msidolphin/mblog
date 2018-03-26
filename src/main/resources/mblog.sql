-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: 2018-03-26 14:48:40
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
  `id` int(11) NOT NULL COMMENT '主键',
  `title` varchar(60) NOT NULL COMMENT '文章标题',
  `tags` varchar(100) NOT NULL COMMENT '标签',
  `type` tinyint(11) NOT NULL COMMENT '类型 0-原创 1-转载 2-翻译',
  `thumbnail` varchar(255) NOT NULL COMMENT '缩略图',
  `content` mediumtext NOT NULL COMMENT '文章内容',
  `is_detete` tinyint(4) DEFAULT '0' COMMENT '文章是否删除 0-否 1-是',
  `views` int(11) NOT NULL DEFAULT '0' COMMENT '文章查阅数',
  `cid` int(11) DEFAULT NULL COMMENT '分类id',
  `editor` tinyint(4) DEFAULT '0' COMMENT '编辑器类型 0-富文本 1-markdown',
  `summary` varchar(255) DEFAULT NULL COMMENT '文章摘要',
  `vote` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) NOT NULL COMMENT '创建者',
  `updator` int(11) NOT NULL COMMENT '更新者',
  `is_delete` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章表';

-- --------------------------------------------------------

--
-- 表的结构 `comment`
--

DROP TABLE IF EXISTS `comment`;
CREATE TABLE IF NOT EXISTS `comment` (
  `id` int(11) NOT NULL COMMENT '主键',
  `article_id` int(11) NOT NULL COMMENT '所属文章id',
  `user_id` int(11) NOT NULL COMMENT '评论人id',
  `content` text NOT NULL COMMENT '评论内容',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '评论状态 0-正常 1-删除',
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
  `id` int(11) NOT NULL COMMENT '主键',
  `name` varchar(30) NOT NULL COMMENT '链接名',
  `url` varchar(255) CHARACTER SET utf32 NOT NULL COMMENT 'URL',
  `sort` int(11) NOT NULL COMMENT '排序号',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除 0-否 1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `udpate_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
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
  `id` int(11) NOT NULL COMMENT '主键',
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
  `id` int(10) UNSIGNED NOT NULL COMMENT '主键',
  `comment_id` int(10) UNSIGNED NOT NULL COMMENT '回复所属的评论id',
  `user_id` int(10) UNSIGNED NOT NULL COMMENT '回复人id',
  `content` text NOT NULL COMMENT '回复内容',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态 0-正常 1-删除',
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
  `id` int(11) NOT NULL COMMENT '主键',
  `key_value` varchar(255) NOT NULL COMMENT '配置项key值',
  `value` text NOT NULL COMMENT '配置项值',
  `name` varchar(255) NOT NULL COMMENT '显示名称',
  `type` tinyint(4) NOT NULL COMMENT '类型 0-门户 1-后台 2-公共',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `setting_value_unqiue` (`key_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='站点配置表';

--
-- 转存表中的数据 `setting`
--

INSERT INTO `setting` (`id`, `key_value`, `value`, `name`, `type`, `create_time`, `update_time`, `creator`, `updator`) VALUES
(1, 'PORTAL_TITLE', '想要有空余时间，就不要浪费时间', '门户标题', 0, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(2, 'PORTAL_BACKGROUND', '/static/templates/images/banner.jpg', '门户背景图', 0, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(3, 'ICP', '粤ICP备xxxxxxxx号-x', 'ICP备案', 2, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(4, 'SITE', 'https://github.com/msidolphin/', '站点网址', 2, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(5, 'DESIGNED_BY', 'http://vinceok.com;vinceok', '站点模板引用', 0, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(6, 'COPYRIGHT', '2017-2018', '版权期限', 0, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(7, 'REPRODUCE', '除特别注明外，本站所有文章均为原创，转载请注明原文链接：', '转载说明', 0, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL),
(8, 'CONTRAT_EMAIL', '1226771922@qq.com', '联系邮箱', 0, '2018-03-26 22:15:11', '2018-03-26 22:15:11', NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `tags`
--

DROP TABLE IF EXISTS `tags`;
CREATE TABLE IF NOT EXISTS `tags` (
  `id` int(11) NOT NULL COMMENT '主键',
  `name` varchar(20) NOT NULL COMMENT '标签名称',
  `frequency` int(11) NOT NULL COMMENT '引用频率',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='标签表';

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL COMMENT '主键',
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
  `is_admin` tinyint(4) DEFAULT '1' COMMENT '是否是博主 0-是 1-否',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `summary` varchar(400) CHARACTER SET utf32 DEFAULT NULL COMMENT '介绍',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` int(11) DEFAULT NULL COMMENT '创建者',
  `updator` int(11) DEFAULT NULL COMMENT '更新者',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_username_unique` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
