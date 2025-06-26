CREATE DATABASE sas_db;
GRANT ALL PRIVILEGES ON sas_db.* TO 'sas_app'@'%';

CREATE DATABASE sas_auth_server_db;
GRANT ALL PRIVILEGES ON sas_auth_server_db.* TO 'sas_app'@'%';

use sas_db;

-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (arm64)
--
-- Host: 127.0.0.1    Database: sas_db
-- ------------------------------------------------------
-- Server version	9.3.0

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
-- Table structure for table `t_billing`
--

DROP TABLE IF EXISTS t_billing;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_billing (
  id_billing bigint NOT NULL AUTO_INCREMENT,
  billing_date datetime(6) NOT NULL,
  billing_final_value double NOT NULL,
  billing_payment_type tinyint NOT NULL,
  billing_status tinyint NOT NULL,
  id_order bigint DEFAULT NULL,
  id_subscriber bigint NOT NULL,
  PRIMARY KEY (id_billing),
  KEY FK1fb77ehq2r5rtqtyt8vwj00ix (id_order),
  KEY FKodos3xeabwtttio3t1jtgyjqa (id_subscriber),
  CONSTRAINT FK1fb77ehq2r5rtqtyt8vwj00ix FOREIGN KEY (id_order) REFERENCES t_order (id_order),
  CONSTRAINT FKodos3xeabwtttio3t1jtgyjqa FOREIGN KEY (id_subscriber) REFERENCES t_subscriber (id),
  CONSTRAINT t_billing_chk_1 CHECK ((`billing_payment_type` between 0 and 2)),
  CONSTRAINT t_billing_chk_2 CHECK ((`billing_status` between 0 and 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_billing`
--

LOCK TABLES t_billing WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_billing_installments`
--

DROP TABLE IF EXISTS t_billing_installments;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_billing_installments (
  id_billing_installments bigint NOT NULL AUTO_INCREMENT,
  installment_date datetime(6) NOT NULL,
  installment_final_value double DEFAULT NULL,
  installment_payment_date datetime(6) DEFAULT NULL,
  installment_status tinyint NOT NULL,
  installment_value double NOT NULL,
  id_billing bigint DEFAULT NULL,
  id_subscriber bigint NOT NULL,
  PRIMARY KEY (id_billing_installments),
  KEY FKgy056sxyftgcwc1vebhbpgk (id_billing),
  KEY FKktpag9wkg8mtoxgl8fvq9brgb (id_subscriber),
  CONSTRAINT FKgy056sxyftgcwc1vebhbpgk FOREIGN KEY (id_billing) REFERENCES t_billing (id_billing),
  CONSTRAINT FKktpag9wkg8mtoxgl8fvq9brgb FOREIGN KEY (id_subscriber) REFERENCES t_subscriber (id),
  CONSTRAINT t_billing_installments_chk_1 CHECK ((`installment_status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_billing_installments`
--

LOCK TABLES t_billing_installments WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_company`
--

DROP TABLE IF EXISTS t_company;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_company (
  cnpj_cgc varchar(18) NOT NULL,
  inscr_estadual varchar(255) DEFAULT NULL,
  inscr_municipal varchar(255) DEFAULT NULL,
  id_customer int NOT NULL,
  PRIMARY KEY (id_customer),
  CONSTRAINT FK6cu7e3ywa95e1p5ynnmcubu7i FOREIGN KEY (id_customer) REFERENCES t_customer (id_customer)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_company`
--

LOCK TABLES t_company WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_customer`
--

DROP TABLE IF EXISTS t_customer;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_customer (
  id_customer int NOT NULL AUTO_INCREMENT,
  cust_details varchar(10000) DEFAULT NULL,
  cust_name varchar(30) NOT NULL,
  cust_status tinyint NOT NULL,
  id_subscriber bigint NOT NULL,
  PRIMARY KEY (id_customer),
  KEY FK3tcyqlnpsxvwonl2hy2p1bgv6 (id_subscriber),
  CONSTRAINT FK3tcyqlnpsxvwonl2hy2p1bgv6 FOREIGN KEY (id_subscriber) REFERENCES t_subscriber (id),
  CONSTRAINT t_customer_chk_1 CHECK ((`cust_status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_customer`
--

LOCK TABLES t_customer WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_customer_address`
--

DROP TABLE IF EXISTS t_customer_address;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_customer_address (
  id_address int NOT NULL AUTO_INCREMENT,
  city varchar(45) DEFAULT NULL,
  complement varchar(45) DEFAULT NULL,
  country varchar(3) DEFAULT NULL,
  `number` int DEFAULT NULL,
  state varchar(3) DEFAULT NULL,
  street_name varchar(60) DEFAULT NULL,
  zipcode varchar(15) DEFAULT NULL,
  id_customer int DEFAULT NULL,
  PRIMARY KEY (id_address),
  KEY FKl2owuxq2vshrv8g0p6hxsdbxj (id_customer),
  CONSTRAINT FKl2owuxq2vshrv8g0p6hxsdbxj FOREIGN KEY (id_customer) REFERENCES t_customer (id_customer)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_customer_address`
--

LOCK TABLES t_customer_address WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_customer_contact`
--

DROP TABLE IF EXISTS t_customer_contact;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_customer_contact (
  id_contact int NOT NULL AUTO_INCREMENT,
  email varchar(45) DEFAULT NULL,
  mobile_phone_number varchar(20) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  office_phone_number varchar(20) DEFAULT NULL,
  contact_status tinyint NOT NULL,
  id_customer int DEFAULT NULL,
  PRIMARY KEY (id_contact),
  KEY FKpk14w0wx2hfam2cc92bg7vht8 (id_customer),
  CONSTRAINT FKpk14w0wx2hfam2cc92bg7vht8 FOREIGN KEY (id_customer) REFERENCES t_customer (id_customer),
  CONSTRAINT t_customer_contact_chk_1 CHECK ((`contact_status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_customer_contact`
--

LOCK TABLES t_customer_contact WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_individual`
--

DROP TABLE IF EXISTS t_individual;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_individual (
  cpf varchar(14) NOT NULL,
  rg varchar(255) DEFAULT NULL,
  id_customer int NOT NULL,
  PRIMARY KEY (id_customer),
  CONSTRAINT FKrma0n3mr3197ur99o8cwiqr11 FOREIGN KEY (id_customer) REFERENCES t_customer (id_customer)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_individual`
--

LOCK TABLES t_individual WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_order`
--

DROP TABLE IF EXISTS t_order;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_order (
  id_order bigint NOT NULL AUTO_INCREMENT,
  close_date datetime(6) DEFAULT NULL,
  deliver_date datetime(6) DEFAULT NULL,
  order_description varchar(255) NOT NULL,
  open_date datetime(6) NOT NULL,
  order_code varchar(100) NOT NULL,
  order_price double DEFAULT NULL,
  qtd_item double NOT NULL,
  order_status tinyint NOT NULL,
  id_customer int DEFAULT NULL,
  id_subscriber bigint DEFAULT NULL,
  PRIMARY KEY (id_order),
  UNIQUE KEY UKgevcb8e17hlrdyjyu2e44inja (order_code),
  KEY FK1u2d3wjkr2lu2lsb9atinb6lc (id_customer),
  KEY FK3xptvqd9de25lmkob9nbji8ss (id_subscriber),
  CONSTRAINT FK1u2d3wjkr2lu2lsb9atinb6lc FOREIGN KEY (id_customer) REFERENCES t_customer (id_customer),
  CONSTRAINT FK3xptvqd9de25lmkob9nbji8ss FOREIGN KEY (id_subscriber) REFERENCES t_subscriber (id),
  CONSTRAINT t_order_chk_1 CHECK ((`order_status` between 0 and 5))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_order`
--

LOCK TABLES t_order WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_permission`
--

DROP TABLE IF EXISTS t_permission;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_permission (
  operation_name enum('ADD','ERASE','READ','UPDATE') NOT NULL,
  user_login bigint NOT NULL,
  id_resource int NOT NULL,
  PRIMARY KEY (operation_name,id_resource,user_login),
  KEY FKbqhofa1db1hxe7l15i4od25xp (user_login),
  KEY FK85g5qesqvtrxoreqb9q2vujw8 (id_resource),
  CONSTRAINT FK85g5qesqvtrxoreqb9q2vujw8 FOREIGN KEY (id_resource) REFERENCES t_resource (id_resource),
  CONSTRAINT FKbqhofa1db1hxe7l15i4od25xp FOREIGN KEY (user_login) REFERENCES t_user (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_permission`
--

LOCK TABLES t_permission WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_plan`
--

DROP TABLE IF EXISTS t_plan;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_plan (
  id smallint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(20) NOT NULL,
  plan_key varchar(50) NOT NULL,
  `status` tinyint NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK7he4oix42qlmyli1uckokvbpx (plan_key),
  CONSTRAINT t_plan_chk_1 CHECK ((`status` between 0 and 1))
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_plan`
--

LOCK TABLES t_plan WRITE;
INSERT INTO t_plan (id, created_at, description, name, plan_key, status, updated_at) VALUES (1,'2025-06-25 10:55:31.000000','','Simples','SIMPLE',0,'2025-06-25 10:55:31.000000'),(2,'2025-06-25 10:55:31.000000','','Profissional','PROFESSIONAL',0,'2025-06-25 10:55:31.000000');
UNLOCK TABLES;

--
-- Table structure for table `t_plan_config`
--

DROP TABLE IF EXISTS t_plan_config;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_plan_config (
  id smallint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  currency varchar(5) NOT NULL,
  max_users int NOT NULL,
  price double NOT NULL,
  updated_at datetime(6) NOT NULL,
  plan_id smallint DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UKf85g10rc1yj2tog0l2npq9u1h (plan_id),
  CONSTRAINT FKfy7xtgk9h20wmy2gujw6041tl FOREIGN KEY (plan_id) REFERENCES t_plan (id)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_plan_config`
--

LOCK TABLES t_plan_config WRITE;
INSERT INTO t_plan_config (id, created_at, currency, max_users, price, updated_at, plan_id) VALUES (1,'2025-06-25 10:55:31.000000','BRL',1,50,'2025-06-25 10:55:31.000000',1),(2,'2025-06-25 10:55:31.000000','BRL',10,150,'2025-06-25 10:55:31.000000',2);
UNLOCK TABLES;

--
-- Table structure for table `t_production_order`
--

DROP TABLE IF EXISTS t_production_order;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_production_order (
  id_po bigint NOT NULL AUTO_INCREMENT,
  actual_delivery_date date DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  deliver_date date DEFAULT NULL,
  estimated_time double NOT NULL,
  open_date date NOT NULL,
  po_number varchar(100) NOT NULL,
  qtd_pieces int DEFAULT NULL,
  po_status tinyint NOT NULL,
  id_order bigint DEFAULT NULL,
  id_subscriber bigint NOT NULL,
  PRIMARY KEY (id_po),
  UNIQUE KEY UKqv0k5jp3hqh2n0gv3xwf76ijm (po_number),
  KEY FKche61vvnhu6m3h89rcm639nyp (id_order),
  KEY FKps10kihe2248xqh6ee7lwngcl (id_subscriber),
  CONSTRAINT FKche61vvnhu6m3h89rcm639nyp FOREIGN KEY (id_order) REFERENCES t_order (id_order),
  CONSTRAINT FKps10kihe2248xqh6ee7lwngcl FOREIGN KEY (id_subscriber) REFERENCES t_subscriber (id),
  CONSTRAINT t_production_order_chk_1 CHECK ((`po_status` between 0 and 3))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_production_order`
--

LOCK TABLES t_production_order WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_registration_token`
--

DROP TABLE IF EXISTS t_registration_token;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_registration_token (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  expires_at datetime(6) NOT NULL,
  token varchar(30) NOT NULL,
  `type` tinyint NOT NULL,
  used bit(1) NOT NULL,
  subscriber_id bigint DEFAULT NULL,
  user_id bigint DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK6cb0buys9xsgnxqku5nphmqwf (subscriber_id),
  KEY FKmwldovryv6gj6e7wsfk9mki8d (user_id),
  CONSTRAINT FK6cb0buys9xsgnxqku5nphmqwf FOREIGN KEY (subscriber_id) REFERENCES t_subscriber (id),
  CONSTRAINT FKmwldovryv6gj6e7wsfk9mki8d FOREIGN KEY (user_id) REFERENCES t_user (id),
  CONSTRAINT t_registration_token_chk_1 CHECK ((`type` between 0 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_registration_token`
--

LOCK TABLES t_registration_token WRITE;
INSERT INTO t_registration_token (id, created_at, expires_at, token, type, used, subscriber_id, user_id) VALUES (1,'2025-06-26 18:56:05.048861','2025-06-26 18:57:05.048735','ThxuArP4DJherJgBQdMRFHmSOtXki7',0,_binary '',1,NULL);
UNLOCK TABLES;

--
-- Table structure for table `t_resource`
--

DROP TABLE IF EXISTS t_resource;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_resource (
  id_resource int NOT NULL AUTO_INCREMENT,
  resource_name varchar(255) NOT NULL,
  PRIMARY KEY (id_resource)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_resource`
--

LOCK TABLES t_resource WRITE;
UNLOCK TABLES;

--
-- Table structure for table `t_subscriber`
--

DROP TABLE IF EXISTS t_subscriber;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_subscriber (
  id bigint NOT NULL AUTO_INCREMENT,
  email varchar(50) NOT NULL,
  `name` varchar(60) NOT NULL,
  uuid varchar(60) NOT NULL,
  activated_at datetime(6) DEFAULT NULL,
  address varchar(150) NOT NULL,
  country varchar(20) NOT NULL,
  created_at datetime(6) NOT NULL,
  phone varchar(15) NOT NULL,
  `status` tinyint NOT NULL,
  terms_and_conditions_accepted bit(1) NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT t_subscriber_chk_1 CHECK ((`status` between 0 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_subscriber`
--

LOCK TABLES t_subscriber WRITE;
INSERT INTO t_subscriber (id, email, name, uuid, activated_at, address, country, created_at, phone, status, terms_and_conditions_accepted, updated_at) VALUES (1,'jane.doe@example.com','Jane Doe','5d4cf079-0433-4639-97ac-2c1949de865f',NULL,'123 Main St','Brazil','2025-06-26 18:56:04.533484','945767359554207',1,_binary '','2025-06-26 18:56:04.530931');
UNLOCK TABLES;

--
-- Table structure for table `t_subscriber_plan`
--

DROP TABLE IF EXISTS t_subscriber_plan;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_subscriber_plan (
  subscriber_plan_id bigint NOT NULL,
  created_at datetime(6) NOT NULL,
  `status` tinyint NOT NULL,
  updated_at datetime(6) NOT NULL,
  subscriber_id bigint NOT NULL,
  plan_id smallint NOT NULL,
  voucher_id varchar(30) DEFAULT NULL,
  PRIMARY KEY (subscriber_plan_id,plan_id,subscriber_id),
  KEY FKlc1f62p325owu7wes4x951wq3 (subscriber_id),
  KEY FK1mktnrxg3160kg9tjj9sbnvkj (plan_id),
  KEY FK3o2xsjggvoexidd2kwa2xddn9 (voucher_id),
  CONSTRAINT FK1mktnrxg3160kg9tjj9sbnvkj FOREIGN KEY (plan_id) REFERENCES t_plan (id),
  CONSTRAINT FK3o2xsjggvoexidd2kwa2xddn9 FOREIGN KEY (voucher_id) REFERENCES t_voucher (`code`),
  CONSTRAINT FKlc1f62p325owu7wes4x951wq3 FOREIGN KEY (subscriber_id) REFERENCES t_subscriber (id),
  CONSTRAINT t_subscriber_plan_chk_1 CHECK ((`status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_subscriber_plan`
--

LOCK TABLES t_subscriber_plan WRITE;
INSERT INTO t_subscriber_plan (subscriber_plan_id, created_at, status, updated_at, subscriber_id, plan_id, voucher_id) VALUES (1,'2025-06-26 18:56:04.533849',0,'2025-06-26 18:56:04.533774',1,1,'PROMO1');
UNLOCK TABLES;

--
-- Table structure for table `t_subscriber_plan_seq`
--

DROP TABLE IF EXISTS t_subscriber_plan_seq;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_subscriber_plan_seq (
  next_val bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_subscriber_plan_seq`
--

LOCK TABLES t_subscriber_plan_seq WRITE;
INSERT INTO t_subscriber_plan_seq (next_val) VALUES (51);
UNLOCK TABLES;

--
-- Table structure for table `t_user`
--

DROP TABLE IF EXISTS t_user;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_user (
  id bigint NOT NULL AUTO_INCREMENT,
  created_at datetime(6) NOT NULL,
  email varchar(50) NOT NULL,
  first_name varchar(50) DEFAULT NULL,
  last_name varchar(50) DEFAULT NULL,
  updated_at datetime(6) NOT NULL,
  user_name varchar(50) NOT NULL,
  uuid varchar(60) NOT NULL,
  subscriber_id bigint DEFAULT NULL,
  hash_password varchar(100) NOT NULL,
  is_admin bit(1) NOT NULL,
  `status` tinyint NOT NULL,
  PRIMARY KEY (id),
  KEY FKf0jttdugkiy7krif59rwra954 (subscriber_id),
  CONSTRAINT FKf0jttdugkiy7krif59rwra954 FOREIGN KEY (subscriber_id) REFERENCES t_subscriber (id),
  CONSTRAINT t_user_chk_1 CHECK ((`status` between 0 and 3))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user`
--

LOCK TABLES t_user WRITE;
INSERT INTO t_user (id, created_at, email, first_name, last_name, updated_at, user_name, uuid, subscriber_id, hash_password, is_admin, status) VALUES (1,'2025-06-26 18:56:16.386439','jane.doe@example.com',NULL,NULL,'2025-06-26 18:56:16.386502','jane.doe@example.com','d42fb248-4bc5-45ea-8af9-91799506a644',1,'$argon2id$v=19$m=65536,t=2,p=1$Jx8aLBOWvjBDG3NwEVeZDQ==$2d4eSyCuZyc9EshieJhZH8NPvym/k7SxfT4y4eUi6oI=',_binary '',1);
UNLOCK TABLES;

--
-- Table structure for table `t_voucher`
--

DROP TABLE IF EXISTS t_voucher;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE t_voucher (
  `code` varchar(30) NOT NULL,
  created_at datetime(6) NOT NULL,
  `status` tinyint NOT NULL,
  updated_at datetime(6) NOT NULL,
  PRIMARY KEY (`code`),
  CONSTRAINT t_voucher_chk_1 CHECK ((`status` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_voucher`
--

LOCK TABLES t_voucher WRITE;
INSERT INTO t_voucher (code, created_at, status, updated_at) VALUES ('PROMO1','2025-06-25 10:55:31.000000',0,'2025-06-25 10:55:31.000000'),('PROMO2','2025-06-25 10:55:31.000000',0,'2025-06-25 10:55:31.000000'),('PROMO3','2025-06-25 10:55:31.000000',0,'2025-06-25 10:55:31.000000'),('SPECIAL_PARTNER_1','2025-06-25 10:55:31.000000',0,'2025-06-25 10:55:31.000000');
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-26 20:04:09
