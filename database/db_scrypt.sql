CREATE SCHEMA IF NOT EXISTS `webshop` DEFAULT CHARACTER SET utf8mb3 ;
USE `webshop` ;


CREATE TABLE IF NOT EXISTS `webshop`.`user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `surname` VARCHAR(45) NOT NULL,
  `role` ENUM('ADMIN', 'USER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `webshop`.`_order` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cost` DECIMAL(10,2) NOT NULL,
  `creation_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `user_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`, `user_id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  INDEX `fk_booking_user1_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_booking_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `webshop`.`user` (`id`))
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `webshop`.`category` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `Name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `webshop`.`item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `creation_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `image` LONGBLOB NULL DEFAULT NULL,
  `long_description` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `Id_UNIQUE` (`id` ASC) VISIBLE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `webshop`.`item_has_category` (
  `item_id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL,
  PRIMARY KEY (`item_id`, `category_id`),
  INDEX `fk_Item_has_Category_Category1_idx` (`category_id` ASC) VISIBLE,
  INDEX `fk_Item_has_Category_Item_idx` (`item_id` ASC) VISIBLE,
  CONSTRAINT `fk_Item_has_Category_Category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `webshop`.`category` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Item_has_Category_Item`
    FOREIGN KEY (`item_id`)
    REFERENCES `webshop`.`item` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `webshop`.`order_item` (
  `order_id` BIGINT NOT NULL,
  `item_id` BIGINT NOT NULL,
  `amount` INT NOT NULL,
  PRIMARY KEY (`order_id`, `item_id`),
  INDEX `fk_booking_has_Item_Item1_idx` (`item_id` ASC) VISIBLE,
  INDEX `fk_booking_has_Item_booking1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_booking_has_Item_booking1`
    FOREIGN KEY (`order_id`)
    REFERENCES `webshop`.`_order` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_booking_has_Item_Item1`
    FOREIGN KEY (`item_id`)
    REFERENCES `webshop`.`item` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;