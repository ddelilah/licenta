CREATE DATABASE  IF NOT EXISTS `licenta` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `licenta`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: licenta
-- ------------------------------------------------------
-- Server version	5.5.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cooling`
--

DROP TABLE IF EXISTS `cooling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cooling` (
  `cooling_id` int(11) NOT NULL,
  `fan_speed` float DEFAULT NULL,
  `power_value` float DEFAULT NULL,
  `rack_id` int(11) NOT NULL,
  PRIMARY KEY (`cooling_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cooling`
--

LOCK TABLES `cooling` WRITE;
/*!40000 ALTER TABLE `cooling` DISABLE KEYS */;
/*!40000 ALTER TABLE `cooling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cpu`
--

DROP TABLE IF EXISTS `cpu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cpu` (
  `cpu_id` int(11) NOT NULL,
  `nr_cores` int(11) NOT NULL,
  `frequency` float DEFAULT NULL,
  `cpu_utlization` float DEFAULT NULL,
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`cpu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cpu`
--

LOCK TABLES `cpu` WRITE;
/*!40000 ALTER TABLE `cpu` DISABLE KEYS */;
/*!40000 ALTER TABLE `cpu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hdd`
--

DROP TABLE IF EXISTS `hdd`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hdd` (
  `hdd_id` int(11) NOT NULL,
  `capacity` float DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`hdd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hdd`
--

LOCK TABLES `hdd` WRITE;
/*!40000 ALTER TABLE `hdd` DISABLE KEYS */;
/*!40000 ALTER TABLE `hdd` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rack`
--

DROP TABLE IF EXISTS `rack`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rack` (
  `rack_id` int(11) NOT NULL,
  `capacity` float NOT NULL,
  `state` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `utilization` float NOT NULL,
  `power_value` float DEFAULT NULL,
  `cooling_value` float DEFAULT NULL,
  PRIMARY KEY (`rack_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rack`
--

LOCK TABLES `rack` WRITE;
/*!40000 ALTER TABLE `rack` DISABLE KEYS */;
/*!40000 ALTER TABLE `rack` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ram`
--

DROP TABLE IF EXISTS `ram`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ram` (
  `ram_id` int(11) NOT NULL,
  `capacity` float DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ram_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ram`
--

LOCK TABLES `ram` WRITE;
/*!40000 ALTER TABLE `ram` DISABLE KEYS */;
/*!40000 ALTER TABLE `ram` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `server`
--

DROP TABLE IF EXISTS `server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `server` (
  `server_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `cpu_id` int(11) DEFAULT NULL,
  `ram_id` int(11) DEFAULT NULL,
  `hdd_id` int(11) DEFAULT NULL,
  `rack_id` int(11) DEFAULT NULL,
  `cooling_value` float DEFAULT NULL,
  `power_value` float DEFAULT NULL,
  `e_idle` float NOT NULL,
  `utilization` double NOT NULL,
  PRIMARY KEY (`server_id`),
  KEY `cpu_id_idx` (`cpu_id`),
  KEY `hdd_id_idx` (`hdd_id`),
  KEY `ram_id_idx` (`ram_id`),
  KEY `rack_id_idx` (`rack_id`),
  CONSTRAINT `cpu_id` FOREIGN KEY (`cpu_id`) REFERENCES `cpu` (`cpu_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `hdd_id` FOREIGN KEY (`hdd_id`) REFERENCES `hdd` (`hdd_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ram_id` FOREIGN KEY (`ram_id`) REFERENCES `ram` (`ram_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `rack_id` FOREIGN KEY (`rack_id`) REFERENCES `rack` (`rack_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `server`
--

LOCK TABLES `server` WRITE;
/*!40000 ALTER TABLE `server` DISABLE KEYS */;
/*!40000 ALTER TABLE `server` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vm`
--

DROP TABLE IF EXISTS `vm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vm` (
  `vm_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `state` varchar(45) NOT NULL,
  `cpu_id` int(11) DEFAULT NULL,
  `ram_id` int(11) DEFAULT NULL,
  `hdd_id` int(11) DEFAULT NULL,
  `server_id` int(11) DEFAULT NULL,
  `power_value` float DEFAULT NULL,
  PRIMARY KEY (`vm_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vm`
--

LOCK TABLES `vm` WRITE;
/*!40000 ALTER TABLE `vm` DISABLE KEYS */;
/*!40000 ALTER TABLE `vm` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-03-23 18:57:16
