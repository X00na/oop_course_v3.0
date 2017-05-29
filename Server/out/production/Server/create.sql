-- DROP DATABASE `shop`;

CREATE SCHEMA IF NOT EXISTS `shop` DEFAULT CHARACTER SET utf8 ;
USE `shop` ;

-- Table `shop`.`users`
CREATE TABLE IF NOT EXISTS `shop`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `role` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_idx` (`login` ASC))
ENGINE = InnoDB;

-- Table `shop`.`types`
CREATE TABLE IF NOT EXISTS `shop`.`types` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- Table `shop`.`products`
CREATE TABLE IF NOT EXISTS `shop`.`products` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `info` VARCHAR(50) NOT NULL,
  `count` INT NOT NULL,
  `price` INT NOT NULL,
  `date_create` DATETIME NOT NULL,
  `type_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_products_type_idx` (`type_id` ASC),
  CONSTRAINT `fk_products_type`
    FOREIGN KEY (`type_id`)
    REFERENCES `shop`.`types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;

-- Table `shop`.`orders`
CREATE TABLE IF NOT EXISTS `shop`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `count` INT NOT NULL,
  `date_order` DATETIME NOT NULL,
  `info` VARCHAR(255) NOT NULL,
  `product_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_orders_products1_idx` (`product_id` ASC),
  CONSTRAINT `fk_orders_products1`
    FOREIGN KEY (`product_id`)
    REFERENCES `shop`.`products` (`id`)
    ON DELETE NO ACTION
    ON UPDATE CASCADE)
ENGINE = InnoDB;