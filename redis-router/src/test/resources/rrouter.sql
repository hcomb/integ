/*
SQLyog Community v12.08 (64 bit)
MySQL - 5.6.27-0ubuntu0.14.04.1 : Database - hcomb_rrouter
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`hcomb_rrouter` /*!40100 DEFAULT CHARACTER SET latin1 */;

/*Table structure for table `redis_instances` */

DROP TABLE IF EXISTS `redis_instances`;

CREATE TABLE `redis_instances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `port` int(11) DEFAULT '6379',
  `minIdle` int(11) DEFAULT '0',
  `maxIdle` int(11) DEFAULT '0',
  `maxTotal` int(11) DEFAULT '1024',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_UNIQUE_NAME` (`name`),
  UNIQUE KEY `idx_unique_host_port` (`host`,`port`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

/*Data for the table `redis_instances` */

insert  into `redis_instances`(`id`,`name`,`host`,`port`,`minIdle`,`maxIdle`,`maxTotal`) values (1,'frontier','localhost',6379,0,0,1024);
insert  into `redis_instances`(`id`,`name`,`host`,`port`,`minIdle`,`maxIdle`,`maxTotal`) values (2,'internal','localhost',7379,0,0,1024);

/*Table structure for table `routes_endpoints` */

DROP TABLE IF EXISTS `routes_endpoints`;

CREATE TABLE `routes_endpoints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `instance_id` int(11) DEFAULT NULL,
  `rkey` varchar(255) NOT NULL,
  `type` varchar(50) NOT NULL,
  `x` int(11) DEFAULT NULL,
  `y` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_endopoint` (`instance_id`,`rkey`,`type`),
  CONSTRAINT `fk_endpoint_instance` FOREIGN KEY (`instance_id`) REFERENCES `redis_instances` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=latin1;

/*Data for the table `routes_endpoints` */

insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (1,1,'user.new','queue',-807,181);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (2,2,'user.t.new','topic',-811,307);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (3,2,'user.hello','queue',-433,589);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (4,2,'user.provision','queue',-743,583);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (6,2,'welcome.mail','queue',-984,382);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (7,NULL,'authz-service','service',-790,43);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (8,NULL,'chat.svc','service',-193,572);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (9,NULL,'provision.svc','service',-750,712);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (10,NULL,'mail-service','service',-1273,466);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (11,1,'user.update','queue',-330,49);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (12,1,'user.delete','queue',-405,213);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (13,2,'user.t.delete','topic',-132,289);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (14,2,'user.t.update','topic',-94,125);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (15,1,'login.errors','queue',-344,-157);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (16,1,'login.success','queue',-373,-353);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (17,NULL,'authn-service','service',-609,-226);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (18,1,'route.sync','queue',-1991,-55);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (19,2,'route.topic','topic',-1981,67);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (20,1,'service.status','queue',-1725,-238);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (21,2,'status.topic','topic',-1732,-355);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (22,NULL,'zookeeper-service','service',-1762,-479);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (23,NULL,'base-service','service',-1660,28);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (24,1,'exception.queue','queue',-1545,184);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (25,NULL,'redis-router','service',-2041,-253);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (26,1,'error.queue','queue',-1835,192);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (27,NULL,'report-service','service',-1674,318);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (28,2,'error.alert','queue',-1494,417);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (29,1,'log.topic','queue',-1432,-94);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (30,NULL,'log-collector','service',-1443,-274);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (31,NULL,'base-queue','service',-1148,-94);
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`,`x`,`y`) values (32,2,'queue.debug','queue',-1172,-276);

/*Table structure for table `routes_links` */

DROP TABLE IF EXISTS `routes_links`;

CREATE TABLE `routes_links` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_from` int(11) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `id_to` int(11) NOT NULL,
  `event` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_dest` (`id_from`,`id_to`),
  KEY `fk_to_endpoint` (`id_to`),
  CONSTRAINT `fk_from_endpoint` FOREIGN KEY (`id_from`) REFERENCES `routes_endpoints` (`id`),
  CONSTRAINT `fk_to_endpoint` FOREIGN KEY (`id_to`) REFERENCES `routes_endpoints` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1;

/*Data for the table `routes_links` */

insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (18,7,1,1,'user.create');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (19,1,1,2,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (20,2,1,6,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (21,6,1,10,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (22,2,1,4,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (23,4,1,9,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (24,2,1,3,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (25,3,1,8,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (26,7,1,11,'user.update');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (27,7,1,12,'user.delete');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (28,12,1,13,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (29,11,1,14,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (31,17,1,15,'login.ko');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (40,17,1,16,'login.ok');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (41,25,1,18,'route.status');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (42,18,1,19,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (44,19,1,23,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (46,23,1,20,'status.update');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (48,20,1,21,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (49,21,1,22,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (50,21,1,25,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (51,23,1,24,'exception');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (52,23,1,26,'error');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (53,26,1,27,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (54,24,1,27,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (55,27,1,28,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (56,28,1,10,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (57,17,1,7,'login');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (58,23,1,29,'log');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (59,29,1,30,NULL);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (60,31,1,32,'debug-message');
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`,`event`) values (61,32,1,30,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
