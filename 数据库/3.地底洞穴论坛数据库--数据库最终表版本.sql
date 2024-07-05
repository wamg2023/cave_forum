/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.7.44-log : Database - cave_adventure_forum
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cave_adventure_forum` /*!40100 DEFAULT CHARACTER SET gbk */;

USE `cave_adventure_forum`;

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `commentId` int(15) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `content` varchar(100) NOT NULL DEFAULT '内容' COMMENT '评论内容',
  `createTime` date NOT NULL DEFAULT '2000-01-01' COMMENT '评论时间',
  `commentLikeCount` int(15) DEFAULT '0' COMMENT '评论点赞数',
  `commentCommentCount` int(15) DEFAULT '0' COMMENT '评论评论数',
  `userId` int(15) NOT NULL COMMENT '评论的用户id',
  `postId` int(15) NOT NULL COMMENT '评论的帖子id',
  `rootCommentId` int(15) DEFAULT '-1' COMMENT '评论的根评论',
  `toCommentId` int(15) DEFAULT '-1' COMMENT '评论的回复评论',
  PRIMARY KEY (`commentId`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `comment` */

/*Table structure for table `post` */

DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
  `postId` int(15) NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `title` varchar(20) NOT NULL DEFAULT '标题' COMMENT '帖子标题',
  `content` varchar(10000) NOT NULL DEFAULT '内容' COMMENT '帖子内容',
  `createTime` date NOT NULL DEFAULT '2000-01-01' COMMENT '帖子发布时间',
  `cover` varchar(100) NOT NULL DEFAULT '$(postId)Avatar.png' COMMENT '帖子封面',
  `postLikeCount` int(15) DEFAULT '0' COMMENT '帖子点赞数',
  `postBookmarkCount` int(15) DEFAULT '0' COMMENT '帖子收藏数',
  `postCommentCount` int(15) DEFAULT '0' COMMENT '帖子评论数',
  `tagId` varchar(5000) NOT NULL COMMENT '帖子tag',
  `userId` int(15) DEFAULT NULL COMMENT '作者的用户id',
  PRIMARY KEY (`postId`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `post` */

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `tagId` int(15) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT 'tag名字' COMMENT 'tag名字',
  PRIMARY KEY (`tagId`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `tag` */

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `userId` int(15) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户类型，0是管理员，1是用户',
  `account` varchar(15) NOT NULL DEFAULT '账号' COMMENT '用户账号',
  `password` varchar(15) NOT NULL DEFAULT '密码' COMMENT '用户密码',
  `nickname` varchar(10) NOT NULL DEFAULT '昵称' COMMENT '用户昵称',
  `birthday` date NOT NULL DEFAULT '2000-01-01' COMMENT '用户出生年月日',
  `email` varchar(100) NOT NULL DEFAULT 'test@itcast.cn' COMMENT '用户邮箱',
  `avatar` varchar(100) NOT NULL DEFAULT '$(userId)Avatar.png' COMMENT '用户头像',
  `followUser` varchar(5000) DEFAULT 'null' COMMENT '关注的用户',
  `likePost` varchar(5000) DEFAULT 'null' COMMENT '点赞的帖子',
  `favoritePost` varchar(5000) DEFAULT 'null' COMMENT '收藏的帖子',
  `likeComment` varchar(5000) DEFAULT 'null' COMMENT '点赞的评论',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

/*Data for the table `user` */

/* Trigger structure for table `user` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `after_user_delete` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'root'@'localhost' */ /*!50003 TRIGGER `after_user_delete` AFTER DELETE ON `user` FOR EACH ROW BEGIN  
    DELETE FROM post WHERE user_id = OLD.user_id; 
    DELETE FROM comment WHERE user_id = OLD.user_id; 
END */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
