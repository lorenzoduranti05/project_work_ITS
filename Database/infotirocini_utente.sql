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
-- Table structure for table `utente`
--

DROP TABLE IF EXISTS `utente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `utente` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `cognome` varchar(255) DEFAULT NULL,
  `mail` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `ruolo` varchar(50) DEFAULT NULL,
  `profile_image_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mail` (`mail`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `utente`
--

LOCK TABLES `utente` WRITE;
/*!40000 ALTER TABLE `utente` DISABLE KEYS */;
INSERT INTO `utente` VALUES (1,'Admin','Superuser','admin@example.com','adminpass','ADMIN',NULL),(9,'Lorenz','Duran','duranti@gmail.com','$2a$10$m6eTMflBQp7OSCBRxis92.9nP5BRDDc7.loVHlp8WGJaDNtwnN0Jm','ADMIN','/immagini-profilo/user_9_06dbab56-cc3b-47a7-b9aa-97d6da091804.jpg'),(10,'Nicolò','Esposito','Nicesp0505@gmail.com','$2a$10$AN9.21ia1UAnZu9BKHD5SeMYAhY3xItxHGH/XC/XRpRW3zusE/HNi','USER',NULL),(11,'test','test','test@gmail.com','$2a$10$tO89HZJ5CuDg00ZWg7KjLuK.dHKO2tg7mPgeVSbHolGFC0yIVUPpe','USER','/immagini-profilo/user_11_05ad73df-a5bb-4c5c-a1bf-e0d15fb96bf6.png'),(12,'Lorenzo','Duranti','lol@gmail.com','$2a$10$aLgaltWh2mhkhoQcUGUhXumRDHKuuYDVl7OUmqJbDFooBoSH9dSN.','USER','/immagini/profili/user_12_a3fd0c05-70f6-4fd2-8836-65f800c65435.png'),(13,'test','test','test@test.test','$2a$10$exdOC3Iqi5J3BOKuAzpz3OXc.Myc4sREMNJexdJRi54Re7oGGIIvG','USER','/immagini-profilo/user_13_50fd1697-8b7a-4cd8-ada2-8aeb0dbbcb26.jpg'),(14,'foto','profilo','foto@gmail.com','$2a$10$Wt81d/.n26EJRxMjaHAi8OEFfzhr4dHHaBNHFvEljLRQjTpm9RCdy','USER','/immagini-profilo/icona.png'),(15,'fotooo','profilo','foto2@gmail.com','$2a$10$S/mj0U9zmewHrqcBr8sf8.CYgBMmSEn8wVSQCo04AHTQDA7gKB1xO','USER','/immagini/icona.png'),(16,'francesca','piscitiello','test@franci.com','$2a$10$5WNGiA63uxxBmZMim/qfmO6Zv6bPPtdCpm9MGOQmDmZ5IDjxUEd7S','USER','/immagini/icona.png');
/*!40000 ALTER TABLE `utente` ENABLE KEYS */;
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
