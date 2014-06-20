SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `hiwayDB` ;
CREATE SCHEMA IF NOT EXISTS `hiwayDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
SHOW WARNINGS;
USE `hiwayDB` ;

-- -----------------------------------------------------
-- Table `Workflowrun`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Workflowrun` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Workflowrun` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `RunID` VARCHAR(36) NOT NULL,
  `WfName` TEXT NULL,
  `WfTime` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`ID`),
  UNIQUE INDEX `RundID_UNIQUE` (`RunID` ASC))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Task`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Task` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Task` (
  `TaskID` BIGINT UNSIGNED NOT NULL,
  `TaskName` TEXT NULL,
  `Language` TEXT NOT NULL,
  PRIMARY KEY (`TaskID`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Invocation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Invocation` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Invocation` (
  `InvocationID` BIGINT UNSIGNED NOT NULL,
  `hostname` VARCHAR(250) NULL,
  `scheduleTime` BIGINT UNSIGNED NULL,
  `StandardOut` LONGTEXT NULL,
  `StandardError` LONGTEXT NULL,
  `Workflowrun_ID` BIGINT UNSIGNED NOT NULL,
  `Task_TaskID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`InvocationID`),
  INDEX `fk_Invocation_Workflowrun1_idx` (`Workflowrun_ID` ASC),
  INDEX `fk_Invocation_Task1_idx` (`Task_TaskID` ASC),
  CONSTRAINT `fk_Invocation_Workflowrun1`
    FOREIGN KEY (`Workflowrun_ID`)
    REFERENCES `Workflowrun` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Invocation_Task1`
    FOREIGN KEY (`Task_TaskID`)
    REFERENCES `Task` (`TaskID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `File`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `File` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `File` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(250) NOT NULL,
  `Size` BIGINT UNSIGNED NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_FileStagingEvent_Invocation1_idx` (`Invocation_InvocationID` ASC),
  CONSTRAINT `fk_FileStagingEvent_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Timestat`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Timestat` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Timestat` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `DidOn` TIMESTAMP NOT NULL,
  `nMinPageFault` BIGINT UNSIGNED NOT NULL,
  `nForcedContextSwitch` BIGINT UNSIGNED NOT NULL,
  `avgDataSize` BIGINT UNSIGNED NOT NULL,
  `nSocketRead` BIGINT UNSIGNED NOT NULL,
  `nIoWrite` BIGINT UNSIGNED NOT NULL,
  `avgResidentSetSize` BIGINT UNSIGNED NOT NULL,
  `nMajPageFault` BIGINT UNSIGNED NOT NULL,
  `nWaitContextSwitch` BIGINT UNSIGNED NOT NULL,
  `userTime` DOUBLE UNSIGNED NOT NULL,
  `realTime` DOUBLE UNSIGNED NOT NULL,
  `sysTime` DOUBLE UNSIGNED NOT NULL,
  `nSocketWrite` BIGINT UNSIGNED NOT NULL,
  `maxResidentSetSize` BIGINT UNSIGNED NOT NULL,
  `avgStackSize` BIGINT UNSIGNED NOT NULL,
  `nSwapOutMainMem` BIGINT UNSIGNED NOT NULL,
  `nIoRead` BIGINT UNSIGNED NOT NULL,
  `nSignal` BIGINT UNSIGNED NOT NULL,
  `avgTextSize` BIGINT UNSIGNED NOT NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NULL,
  `Type` VARCHAR(100) NOT NULL,
  `File_ID` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Timestat_Invocation1_idx` (`Invocation_InvocationID` ASC),
  INDEX `fk_Timestat_File1_idx` (`File_ID` ASC),
  UNIQUE INDEX `uniInvoc` (`Invocation_InvocationID` ASC, `Type` ASC),
  UNIQUE INDEX `uniFile` (`File_ID` ASC, `Type` ASC),
  CONSTRAINT `fk_Timestat_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Timestat_File1`
    FOREIGN KEY (`File_ID`)
    REFERENCES `File` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Inoutput`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Inoutput` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Inoutput` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Keypart` TEXT NOT NULL,
  `Content` LONGTEXT NOT NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NOT NULL,
  `Type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Output_Invocation1_idx` (`Invocation_InvocationID` ASC),
  UNIQUE INDEX `NoDouble` (`Keypart`(100) ASC, `Invocation_InvocationID` ASC, `Type`(45) ASC),
  CONSTRAINT `fk_Output_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Userevent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Userevent` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Userevent` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Content` LONGTEXT NOT NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Userevent_Invocation1_idx` (`Invocation_InvocationID` ASC),
  CONSTRAINT `fk_Userevent_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Hiwayevent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Hiwayevent` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Hiwayevent` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Content` LONGTEXT NOT NULL,
  `Type` VARCHAR(200) NOT NULL,
  `Workflowrun_ID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Hiwayevent_Workflowrun1_idx` (`Workflowrun_ID` ASC),
  CONSTRAINT `fk_Hiwayevent_Workflowrun1`
    FOREIGN KEY (`Workflowrun_ID`)
    REFERENCES `Workflowrun` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
