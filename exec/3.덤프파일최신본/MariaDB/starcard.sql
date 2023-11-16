-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: k9a101.p.ssafy.io    Database: starcard
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
-- Table structure for table `starcard`
--

DROP TABLE IF EXISTS `starcard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `starcard` (
  `card_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(10) DEFAULT NULL,
  `content` varchar(300) DEFAULT NULL,
  `image_path` varchar(100) NOT NULL,
  `image_url` varchar(200) NOT NULL,
  `like_count` int(11) DEFAULT NULL,
  `member_id` varchar(20) NOT NULL,
  `observe_site_id` varchar(20) NOT NULL,
  `photo_at` datetime(6) DEFAULT NULL,
  `tools` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `starcard`
--

LOCK TABLES `starcard` WRITE;
/*!40000 ALTER TABLE `starcard` DISABLE KEYS */;
INSERT INTO `starcard` VALUES (1,'GALAXY','카드345','starcard_image/1231700032772715.jpeg','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/starcard_image/1231700032772715.jpeg',0,'3140000396','124402030495933',NULL,''),(2,'GALAXY','카드345','starcard_image/1231700032776419.jpeg','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/starcard_image/1231700032776419.jpeg',4,'3140000396','124402030495933',NULL,''),(3,'GALAXY','카드3','starcard_image/1231700032792356.jpeg','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/starcard_image/1231700032792356.jpeg',1,'3140000396','124402030495933',NULL,''),(11,'GALAXY','내용내내용내용입니','starcard_image/2mb2mb1700037851893.jpg','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/starcard_image/2mb2mb1700037851893.jpg',0,'222','124402030495932',NULL,'zzzz'),(12,'GALAXY','테스트','starcard_image/10000035571700045407976.jpg','https://stellagram-bucket-a101.s3.ap-northeast-2.amazonaws.com/starcard_image/10000035571700045407976.jpg',1,'3140000396','',NULL,'');
/*!40000 ALTER TABLE `starcard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `starcard_like`
--

DROP TABLE IF EXISTS `starcard_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `starcard_like` (
  `like_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) DEFAULT NULL,
  `card_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`like_id`),
  KEY `FKiu5dey2o3qrm9c4wmdvl0muiv` (`card_id`),
  CONSTRAINT `FKiu5dey2o3qrm9c4wmdvl0muiv` FOREIGN KEY (`card_id`) REFERENCES `starcard` (`card_id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `starcard_like`
--

LOCK TABLES `starcard_like` WRITE;
/*!40000 ALTER TABLE `starcard_like` DISABLE KEYS */;
INSERT INTO `starcard_like` VALUES (2,12345,1),(8,44,1),(11,44,2),(12,14,2),(13,2222,2),(43,3140000396,11),(46,3140000396,2),(47,3140000396,3),(48,3140000396,12),(93,3140000396,1);
/*!40000 ALTER TABLE `starcard_like` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-16 14:58:22
