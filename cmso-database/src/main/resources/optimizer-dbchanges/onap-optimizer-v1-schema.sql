
CREATE TABLE IF NOT EXISTS `request` (
  uuid BINARY(16) NOT NULL,
  request LONGTEXT NOT NULL,
  created_time BIGINT(20) NULL DEFAULT NULL,
  request_start BIGINT(20) NULL DEFAULT NULL,
  request_end BIGINT(20) NULL DEFAULT NULL,
  status VARCHAR(45) NULL DEFAULT NULL,
  message MEDIUMTEXT NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `response` (
  uuid BINARY(16) NOT NULL,
  response LONGTEXT NULL DEFAULT NULL,
  delivered_time BIGINT(20) NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


CREATE TABLE IF NOT EXISTS `topology` (
  uuid BINARY(16) NOT NULL,
  topology LONGTEXT NULL DEFAULT NULL,
  topology_start BIGINT(20) NULL DEFAULT NULL,
  topology_end BIGINT(20) NULL DEFAULT NULL,
  topology_retries INT NULL DEFAULT NULL,
  topology_polling_interval INT NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `tickets` (
  uuid BINARY(16) NOT NULL,
  tickets LONGTEXT NULL DEFAULT NULL,
  tickets_start BIGINT(20) NULL DEFAULT NULL,
  tickets_end BIGINT(20) NULL DEFAULT NULL,
  tickets_retries INT NULL DEFAULT NULL,
  topology_polling_interval INT NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `optimizer` (
  uuid BINARY(16) NOT NULL,
  optimize_response LONGTEXT NULL DEFAULT NULL,
  optimize_start BIGINT(20) NULL DEFAULT NULL,
  optimize_end BIGINT(20) NULL DEFAULT NULL,
  optimize_retries INT NULL DEFAULT NULL,
  optimize_polling_interval INT NULL DEFAULT NULL,
  PRIMARY KEY (`uuid`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
