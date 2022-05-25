create database proiect_pao;
use proiect_pao;

CREATE TABLE `proiect_pao`.`books` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(100) NOT NULL DEFAULT 'no title',
  `datePublished` DATE NULL,
  `copiesInLibrary` INT NOT NULL DEFAULT 1,
  `category` VARCHAR(30) NOT NULL,
  `subcategory` VARCHAR(30) NULL,
  `yearWritten` INT NULL,
  `originalLanguage` VARCHAR(20) NULL,
  `translatedInto` VARCHAR(20) NULL,
  PRIMARY KEY (`id`));
  
CREATE TABLE `proiect_pao`.`novels` (
  `bookId` INT NOT NULL,
  `novelTitle` VARCHAR(100) NOT NULL DEFAULT 'no title',
  `genres` VARCHAR(100) NULL,
  `themes` VARCHAR(100) NULL,
  PRIMARY KEY (`bookId`),
  CONSTRAINT `bookIdFK1`
    FOREIGN KEY (`bookId`)
    REFERENCES `proiect_pao`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
  
CREATE TABLE `proiect_pao`.`dramas` (
  `bookId` INT NOT NULL,
  `dramaTitle` VARCHAR(100) NOT NULL DEFAULT 'no title',
  `dramaGenre` VARCHAR(30) NULL,
  `themes` VARCHAR(100) NULL,
  PRIMARY KEY (`bookId`),
  CONSTRAINT `bookIdFK2`
    FOREIGN KEY (`bookId`)
    REFERENCES `proiect_pao`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
  
CREATE TABLE `proiect_pao`.`textbooks` (
  `bookId` INT NOT NULL,
  `textbookTitle` VARCHAR(100) NOT NULL DEFAULT 'no title',
  `domain` VARCHAR(50) NULL,
  `subject` VARCHAR(50) NULL,
  `level` VARCHAR(50) NULL,
  PRIMARY KEY (`bookId`),
  CONSTRAINT `bookIdFK3`
    FOREIGN KEY (`bookId`)
    REFERENCES `proiect_pao`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
  
CREATE TABLE `proiect_pao`.`dictionaries` (
  `bookId` INT NOT NULL,
  `dictioTitle` VARCHAR(100) NOT NULL DEFAULT 'no title',
  `type` VARCHAR(50) NULL,
  `targetLanguage` VARCHAR(50) NULL,
  `field` VARCHAR(50) NULL,
  PRIMARY KEY (`bookId`),
  CONSTRAINT `bookIdFK4`
    FOREIGN KEY (`bookId`)
    REFERENCES `proiect_pao`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
  
CREATE TABLE `proiect_pao`.`authors` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL DEFAULT 'unknown',
  `country` VARCHAR(50) NULL,
  `dateBorn` DATE NULL,
  `dateDied` DATE NULL,
  PRIMARY KEY (`id`));
    
CREATE TABLE `proiect_pao`.`addresses` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `country` VARCHAR(50) NOT NULL DEFAULT '-',
  `city` VARCHAR(50) NOT NULL DEFAULT '-',
  `street` VARCHAR(50) NULL,
  PRIMARY KEY (`id`));
  
  CREATE TABLE `proiect_pao`.`library_members` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL DEFAULT 'anonymous',
  `email` VARCHAR(50) NOT NULL DEFAULT '-',
  `phone` VARCHAR(20) NOT NULL DEFAULT '-',
  `membershipStarted` DATE NOT NULL,
  `membershipExpires` DATE NOT NULL,
  `address_id` INT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `addressIdFK`
    FOREIGN KEY (`address_id`)
    REFERENCES `proiect_pao`.`addresses` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `uq_constr` UNIQUE (`name` , `email`, `phone`));
  
  
CREATE TABLE `proiect_pao`.`borrowed_books` (
  `memberId` INT NOT NULL,
  `bookId` INT NOT NULL,
  PRIMARY KEY (`memberId`, `bookId`),
  CONSTRAINT `memberIdFK`
    FOREIGN KEY (`memberId`)
    REFERENCES `proiect_pao`.`library_members` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `borrowedBookIdFK`
    FOREIGN KEY (`bookId`)
    REFERENCES `proiect_pao`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
CREATE TABLE `proiect_pao`.`book_authors` (
  `bookId` INT NOT NULL,
  `authorId` INT NOT NULL,
  PRIMARY KEY (`bookId`, `authorId`),
  CONSTRAINT `bookIdFK`
    FOREIGN KEY (`bookId`)
    REFERENCES `proiect_pao`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `authorIdFK`
    FOREIGN KEY (`authorId`)
    REFERENCES `proiect_pao`.`authors` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
