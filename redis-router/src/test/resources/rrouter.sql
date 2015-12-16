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
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_endopoint` (`instance_id`,`rkey`,`type`),
  CONSTRAINT `fk_endpoint_instance` FOREIGN KEY (`instance_id`) REFERENCES `redis_instances` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

/*Data for the table `routes_endpoints` */

insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (8,NULL,'chat.svc','service');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (10,NULL,'mail.svc','service');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (9,NULL,'provision.svc','service');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (7,NULL,'user.svc','service');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (1,1,'user.new','queue');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (3,2,'user.hello','queue');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (4,2,'user.provision','queue');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (2,2,'user.topic','topic');
insert  into `routes_endpoints`(`id`,`instance_id`,`rkey`,`type`) values (6,2,'welcome.mail','queue');

/*Table structure for table `routes_links` */

DROP TABLE IF EXISTS `routes_links`;

CREATE TABLE `routes_links` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_from` int(11) NOT NULL,
  `active` int(1) NOT NULL DEFAULT '1',
  `id_to` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_dest` (`id_from`,`id_to`),
  KEY `fk_to_endpoint` (`id_to`),
  CONSTRAINT `fk_from_endpoint` FOREIGN KEY (`id_from`) REFERENCES `routes_endpoints` (`id`),
  CONSTRAINT `fk_to_endpoint` FOREIGN KEY (`id_to`) REFERENCES `routes_endpoints` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;

/*Data for the table `routes_links` */

insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (18,7,1,1);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (19,1,1,2);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (20,2,1,6);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (21,6,1,10);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (22,2,1,4);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (23,4,1,9);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (24,2,1,3);
insert  into `routes_links`(`id`,`id_from`,`active`,`id_to`) values (25,3,1,8);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
