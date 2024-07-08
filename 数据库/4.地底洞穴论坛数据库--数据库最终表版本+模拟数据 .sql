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
  `comment_id` int(15) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `content` varchar(100) NOT NULL DEFAULT '内容' COMMENT '评论内容',
  `create_time` date NOT NULL DEFAULT '2000-01-01' COMMENT '评论时间',
  `comment_like_count` int(15) DEFAULT '0' COMMENT '评论点赞数',
  `comment_comment_count` int(15) DEFAULT '0' COMMENT '评论评论数',
  `user_id` int(15) NOT NULL COMMENT '评论的用户id',
  `post_id` int(15) NOT NULL COMMENT '评论的帖子id',
  `root_comment_id` int(15) DEFAULT '-1' COMMENT '评论的根评论',
  `to_comment_id` int(15) DEFAULT '-1' COMMENT '评论的回复评论',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=gbk;

/*Data for the table `comment` */

insert  into `comment`(`comment_id`,`content`,`create_time`,`comment_like_count`,`comment_comment_count`,`user_id`,`post_id`,`root_comment_id`,`to_comment_id`) values (1,'test用户1（于post_id=1）评论1：这有一个新的游戏','2000-01-01',0,0,3,1,-1,-1),(2,'test用户1（于post_id=3）评论1：看上去还不错','2000-01-01',0,0,3,3,-1,-1),(3,'test用户2（于post_id=3）评论1：有点意思','2000-01-01',0,0,4,3,-1,-1),(4,'test用户2（于post_id=5）评论1：我来帮你，我做个攻略','2000-01-01',0,0,4,5,-1,-1),(5,'test用户1（于post_id=5回复CommentId=4）评论1：谢谢大佬','2000-01-01',0,0,3,5,4,4),(6,'test用户2（于post_id=5回复commentId=5）评论2：攻略已经发出来啦','2000-01-01',0,0,4,5,4,5),(7,'test用户1（于post_id=6）评论1：感谢大佬的帮助！！！','2000-01-01',0,0,3,6,-1,-1),(8,'test用户2（于post_id=6回复commentId=7）评论1：小菜一碟~~~','2000-01-01',0,0,4,4,7,7);

/*Table structure for table `post` */

DROP TABLE IF EXISTS `post`;

CREATE TABLE `post` (
  `post_id` int(15) NOT NULL AUTO_INCREMENT COMMENT '帖子id',
  `title` varchar(20) NOT NULL DEFAULT '标题' COMMENT '帖子标题',
  `content` varchar(10000) NOT NULL DEFAULT '内容' COMMENT '帖子内容',
  `create_time` date NOT NULL DEFAULT '2000-01-01' COMMENT '帖子发布时间',
  `cover` varchar(100) NOT NULL DEFAULT '$(post_id)Cover.png' COMMENT '帖子封面',
  `post_like_count` int(15) DEFAULT '0' COMMENT '帖子点赞数',
  `post_bookmark_count` int(15) DEFAULT '0' COMMENT '帖子收藏数',
  `post_comment_count` int(15) DEFAULT '0' COMMENT '帖子评论数',
  `tag_id` int(15) NOT NULL DEFAULT '1' COMMENT '帖子tag',
  `user_id` int(15) NOT NULL COMMENT '作者的用户id',
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=gbk;

/*Data for the table `post` */

insert  into `post`(`post_id`,`title`,`content`,`create_time`,`cover`,`post_like_count`,`post_bookmark_count`,`post_comment_count`,`tag_id`,`user_id`) values (1,'test管理员1标题1：新的游戏论坛','test管理员1内容1','2000-01-01','1Cover.png',0,0,0,1,1),(2,'test管理员1标题2：更新公告','test管理员1内容2','2000-01-01','2Cover.png',0,0,0,1,1),(3,'test管理员2标题1：新的活动！','test管理员2内容1','2000-01-01','3Cover.png',0,0,0,1,2),(4,'test用户1标题1：好玩，爱玩','test用户1内容1','2000-01-01','4Cover.png',0,0,0,4,3),(5,'test用户1标题2：打不过求助','test用户1内容2','2000-01-01','5Cover.png',0,0,0,3,3),(6,'test用户2标题2：***攻略','test用户2内容2','2000-01-01','6Cover.png',0,0,0,2,4);

/*Table structure for table `tag` */

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
  `tag_id` int(15) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT 'tag名字' COMMENT 'tag名字',
  PRIMARY KEY (`tag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=gbk;

/*Data for the table `tag` */

insert  into `tag`(`tag_id`,`name`) values (1,'更新公告'),(2,'游戏攻略'),(3,'玩家求助'),(4,'分享日常');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `user_id` int(15) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '用户类型，0是管理员，1是用户',
  `account` varchar(15) NOT NULL DEFAULT '账号' COMMENT '用户账号',
  `password` varchar(15) NOT NULL DEFAULT '密码' COMMENT '用户密码',
  `nickname` varchar(10) NOT NULL DEFAULT '昵称' COMMENT '用户昵称',
  `birthday` date NOT NULL DEFAULT '2000-01-01' COMMENT '用户出生年月日',
  `email` varchar(100) NOT NULL DEFAULT 'test@itcast.cn' COMMENT '用户邮箱',
  `avatar` varchar(100) NOT NULL DEFAULT '$(user_id)Avatar.png' COMMENT '用户头像',
  `follow_user` varchar(5000) DEFAULT 'null' COMMENT '关注的用户',
  `like_post` varchar(5000) DEFAULT 'null' COMMENT '点赞的帖子',
  `bookmark_post` varchar(5000) DEFAULT 'null' COMMENT '收藏的帖子',
  `like_comment` varchar(5000) DEFAULT 'null' COMMENT '点赞的评论',
  `verify_code` char(4) DEFAULT 'null' COMMENT '用户验证码',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=gbk;

/*Data for the table `user` */

insert  into `user`(`user_id`,`type`,`account`,`password`,`nickname`,`birthday`,`email`,`avatar`,`follow_user`,`like_post`,`bookmark_post`,`like_comment`,`verify_code`) values (0,0,'111','123456789','test管理员1','2000-01-01','test1@itcast.cn','0Avatar.png','null','null','null','null',NULL),(1,0,'222','123456789','test管理员2','2000-01-01','test2@itcast.cn','1Avatar.png','null','null','null','null',NULL),(2,1,'111111','123456789','test用户1','2000-01-01','test用户1@itcast.cn','2Avatar.png','null','null','null','null',NULL),(3,1,'222222','123456789','test用户2','2000-01-01','test用户2@itcast.cn','3Avatar.png','null','null','null','null',NULL);

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
