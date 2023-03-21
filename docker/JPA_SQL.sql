-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        8.0.31 - MySQL Community Server - GPL
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- spring5 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `spring5` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `spring5`;

-- 테이블 spring5.licenses 구조 내보내기
CREATE TABLE IF NOT EXISTS `licenses` (
  `license_id` varchar(50) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `organization_id` varchar(50) NOT NULL,
  `product_name` varchar(50) NOT NULL,
  `license_type` varchar(50) NOT NULL,
  `comment` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`license_id`),
  UNIQUE KEY `organization_id` (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 테이블 데이터 spring5.licenses:~2 rows (대략적) 내보내기
INSERT INTO `licenses` (`license_id`, `description`, `organization_id`, `product_name`, `license_type`, `comment`) VALUES
	('3639d60e-1cfe-40f9-8b7e-fa402a5f7d45', 'Software product', 'organization_test', 'Ostock', 'complete', 'comment'),
	('license1', 'test_description', 'organization1', 'product1', 'full', 'comment');

-- 테이블 spring5.organizations 구조 내보내기
CREATE TABLE IF NOT EXISTS `organizations` (
  `organization_id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `contact_name` varchar(50) NOT NULL,
  `contact_email` varchar(50) NOT NULL,
  `contact_phone` varchar(50) NOT NULL,
  PRIMARY KEY (`organization_id`),
  CONSTRAINT `FK__licensing` FOREIGN KEY (`organization_id`) REFERENCES `licenses` (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 테이블 데이터 spring5.organizations:~2 rows (대략적) 내보내기
INSERT INTO `organizations` (`organization_id`, `name`, `contact_name`, `contact_email`, `contact_phone`) VALUES
	('organization_test', 'Ostock_organization', 'contact_name', 'contact_email', 'contact_phone'),
	('organization1', 'organization_name', 'contact_name', 'contact_email@email.com', '001-3045-3957');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
