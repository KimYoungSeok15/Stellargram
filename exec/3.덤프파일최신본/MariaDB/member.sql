-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: k9a101.p.ssafy.io    Database: member
-- ------------------------------------------------------
-- Server version	11.1.2-MariaDB-1:11.1.2+maria~ubu2204

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `follow`
--

DROP TABLE IF EXISTS `follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `follow` (
  `follow_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `followee` bigint(20) DEFAULT NULL,
  `follower` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`follow_id`),
  KEY `FKmaaj8hxatqcq0tbnni8glg96b` (`followee`),
  KEY `FKon174ytyau6iulr7uyxfoui4m` (`follower`),
  CONSTRAINT `FKmaaj8hxatqcq0tbnni8glg96b` FOREIGN KEY (`followee`) REFERENCES `member` (`member_id`),
  CONSTRAINT `FKon174ytyau6iulr7uyxfoui4m` FOREIGN KEY (`follower`) REFERENCES `member` (`member_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `follow`
--

LOCK TABLES `follow` WRITE;
/*!40000 ALTER TABLE `follow` DISABLE KEYS */;
INSERT INTO `follow` VALUES (2,3140000396,12345),(4,12345,3140000396),(6,222,12345),(7,222,12345),(20,222,3140000396);
/*!40000 ALTER TABLE `follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` bigint(20) NOT NULL,
  `activated` bit(1) NOT NULL,
  `card_count` bigint(20) DEFAULT 0,
  `follow_count` bigint(20) DEFAULT 0,
  `following_count` bigint(20) DEFAULT 0,
  `nickname` varchar(20) NOT NULL,
  `profile_image_url` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,_binary '',0,0,0,'닉네임','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/profile_image/profile_stellagram.jpg'),(111,_binary '',0,0,0,'닉네임111','https://health.chosun.com/site/data/img_dir/2023/07/17/2023071701753_0.jpg'),(222,_binary '',0,5,0,'닉네임222','https://health.chosun.com/site/data/img_dir/2023/07/17/2023071701753_0.jpg'),(12345,_binary '',0,1,3,'닉네임123','https://health.chosun.com/site/data/img_dir/2023/07/17/2023071701753_0.jpg'),(1010101010,_binary '',0,0,0,'nick','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/profile_image/profile_stellagram.jpg'),(3114430758,_binary '',0,0,0,'이현도','https://k.kakaocdn.net/dn/b9c39p/btsyzclrjpr/U026nMj0pulOYVs6QNJSKk/img_640x640.jpg'),(3139622743,_binary '',0,0,0,'ppsrac','https://k.kakaocdn.net/dn/o8W8t/btsyTspM1Ez/TIpzqkNyQEZL6j2PcMMhBK/img_640x640.jpg'),(3140000396,_binary '',0,1,4,'영석몬','https://k.kakaocdn.net/dn/X0tHO/btszM2PIrU6/1SO0y42mgHzYdgWpNk8egk/img_640x640.jpg'),(3143210788,_binary '',0,0,0,'커피','https://k.kakaocdn.net/dn/HWowr/btrXm8A5ffa/l6PKAis6ofR4zOOEZ1WXdK/img_640x640.jpg'),(3151893247,_binary '',0,0,0,'J','https://k.kakaocdn.net/dn/HvmAY/btsjO1Nfeiq/areu5OynJGDhRLVlbo89G1/img_640x640.jpg'),(3163410905,_binary '',0,0,0,'허재','https://k.kakaocdn.net/dn/drclYc/btrtY9Asfam/UwVjEiGeOqwcPRBNkrkUxK/img_640x640.jpg');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-16 14:19:50
