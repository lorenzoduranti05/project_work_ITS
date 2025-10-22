-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: infotirocini
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `lavoro`
--

DROP TABLE IF EXISTS `lavoro`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lavoro` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `durata` varchar(255) DEFAULT NULL,
  `orari` varchar(255) DEFAULT NULL,
  `descrizione` text,
  `azienda_id` int DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `azienda_id` (`azienda_id`),
  CONSTRAINT `lavoro_ibfk_1` FOREIGN KEY (`azienda_id`) REFERENCES `azienda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lavoro`
--

LOCK TABLES `lavoro` WRITE;
/*!40000 ALTER TABLE `lavoro` DISABLE KEYS */;
INSERT INTO `lavoro` VALUES (1,'Tirocinio Sviluppatore Frontend','6 mesi','9:00 - 18:00','Stiamo cercando uno stagista appassionato di sviluppo frontend per unirsi al nostro team. Lavorerai su progetti reali usando React e Tailwind.',1,'techsolutions.jpeg'),(2,'Stage Marketing Digitale','3 mesi','Part-time Pomeriggio','Opportunità di stage nel nostro reparto marketing. Aiuterai nella gestione dei social media e nella creazione di campagne AdWords.',2,'visionate.png'),(3,'Tirocinio Backend Developer (Java)','6 mesi','Full-time','Unisciti al nostro team backend e impara a sviluppare API RESTful con Spring Boot e a gestire database relazionali.',1,'techsolutions.jpeg'),(4,'Stage Assistente Risorse Umane','4 mesi','9:00 - 13:00','Supporto nelle attività di recruiting, gestione documentale e organizzazione eventi aziendali. Richiesta buona conoscenza pacchetto Office.',2,'visionate.png'),(5,'Tirocinio Sviluppo Software Embedded','6 mesi','Full-time','Partecipazione allo sviluppo di firmware per dispositivi IoT. Richiesta conoscenza base di C/C++ e sistemi Linux.',3,'innovatech.png'),(6,'Stage Social Media Manager Junior','5 mesi','Flessibile (20h/sett)','Creazione e gestione di contenuti per i canali social aziendali (Facebook, Instagram, LinkedIn). Analisi delle performance e reporting.',1,'techsolutions.jpeg');
/*!40000 ALTER TABLE `lavoro` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-22 17:18:03
