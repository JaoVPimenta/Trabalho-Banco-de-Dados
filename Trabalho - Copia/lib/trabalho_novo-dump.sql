-- MySQL dump 10.13  Distrib 8.0.44, for Linux (x86_64)
--
-- Host: localhost    Database: trabalho
-- ------------------------------------------------------
-- Server version	8.0.44-0ubuntu0.24.04.1

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
-- Table structure for table `Integrantes`
--

DROP TABLE IF EXISTS `Integrantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Integrantes` (
  `cargo` varchar(255) NOT NULL,
  `pessoa_id` int NOT NULL,
  `projeto_id` int NOT NULL,
  KEY `fk_func_integrantes` (`pessoa_id`),
  KEY `fk_funk_integrantes` (`projeto_id`),
  CONSTRAINT `fk_func_integrantes` FOREIGN KEY (`pessoa_id`) REFERENCES `Pessoas` (`id`),
  CONSTRAINT `fk_funk_integrantes` FOREIGN KEY (`projeto_id`) REFERENCES `Projetos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Integrantes`
--

LOCK TABLES `Integrantes` WRITE;
/*!40000 ALTER TABLE `Integrantes` DISABLE KEYS */;
INSERT INTO `Integrantes` VALUES ('Programador',2,1),('Desenvolvedor',3,1);
/*!40000 ALTER TABLE `Integrantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Pessoas`
--

DROP TABLE IF EXISTS `Pessoas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Pessoas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cpf` varchar(500) NOT NULL,
  `data_nascimento` date NOT NULL,
  `nome` varchar(500) NOT NULL,
  `sexo` varchar(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Pessoas`
--

LOCK TABLES `Pessoas` WRITE;
/*!40000 ALTER TABLE `Pessoas` DISABLE KEYS */;
INSERT INTO `Pessoas` VALUES (2,'12345','2008-09-22','Rafael','M'),(3,'777','2008-11-19','Joao','M'),(7,'157','2008-03-22','Davi','M');
/*!40000 ALTER TABLE `Pessoas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Projetos`
--

DROP TABLE IF EXISTS `Projetos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Projetos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `data_inicial` date DEFAULT NULL,
  `data_final` date DEFAULT NULL,
  `nome` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Projetos`
--

LOCK TABLES `Projetos` WRITE;
/*!40000 ALTER TABLE `Projetos` DISABLE KEYS */;
INSERT INTO `Projetos` VALUES (1,'2025-11-28','2025-12-13','TI'),(3,'2025-12-12',NULL,'Construcao'),(4,'2026-02-11','2026-08-02','Fazenda');
/*!40000 ALTER TABLE `Projetos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contatos`
--

DROP TABLE IF EXISTS `contatos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contatos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `contato` varchar(255) NOT NULL,
  `tipo` varchar(10) NOT NULL,
  `pessoa_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_func_contatos` (`pessoa_id`),
  CONSTRAINT `fk_func_contatos` FOREIGN KEY (`pessoa_id`) REFERENCES `Pessoas` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contatos`
--

LOCK TABLES `contatos` WRITE;
/*!40000 ALTER TABLE `contatos` DISABLE KEYS */;
INSERT INTO `contatos` VALUES (1,'62992229957','TELEFONE',3),(2,'Rodriguesfaellll@gmail.com','EMAIL',2);
/*!40000 ALTER TABLE `contatos` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-13 15:28:57
