-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: k9a101.p.ssafy.io    Database: observe_site
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
-- Table structure for table `observe_site`
--

DROP TABLE IF EXISTS `observe_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `observe_site` (
  `observe_site_id` varchar(20) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `rating_sum` bigint(20) DEFAULT 0,
  `review_count` bigint(20) DEFAULT 0,
  PRIMARY KEY (`observe_site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observe_site`
--

LOCK TABLES `observe_site` WRITE;
/*!40000 ALTER TABLE `observe_site` DISABLE KEYS */;
INSERT INTO `observe_site` VALUES ('37429-127048',37.429985,127.04815,3114430758,'',0,0),('37476-127046',37.47632,127.04653,3114430758,'',0,0),('37483-127046',37.483685,127.04674,3114430758,'',0,0),('37486-127030',37.486996,127.030205,3114430758,'',0,0),('37494-127040',37.494793,127.04002,3139622743,'',0,0),('37496-127042',37.496647,127.04202,3114430758,'',0,0),('37497-127028',37.497265,127.028885,3114430758,'',0,0),('37498-127037',37.498653,127.03728,3114430758,'',0,0),('37498-127038',37.49877,127.03824,3114430758,'',0,0),('37498-127040',37.498817,127.040474,3114430758,'',0,0),('37498-127042',37.49819,127.042206,3114430758,'',0,0),('37504-127036',37.504417,127.03673,3114430758,'',0,0);
/*!40000 ALTER TABLE `observe_site` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observe_site_review`
--

DROP TABLE IF EXISTS `observe_site_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `observe_site_review` (
  `review_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(100) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  `rating` bigint(20) DEFAULT NULL,
  `observe_site_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`review_id`),
  KEY `FKswo3sxkwhhatvrjuor2l2n90c` (`observe_site_id`),
  CONSTRAINT `FKswo3sxkwhhatvrjuor2l2n90c` FOREIGN KEY (`observe_site_id`) REFERENCES `observe_site` (`observe_site_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observe_site_review`
--

LOCK TABLES `observe_site_review` WRITE;
/*!40000 ALTER TABLE `observe_site_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `observe_site_review` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-16 15:43:59
