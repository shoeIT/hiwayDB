SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema hiwayDB
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `hiwayDB` ;
CREATE SCHEMA IF NOT EXISTS `hiwayDB` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
SHOW WARNINGS;
USE `hiwayDB` ;

-- -----------------------------------------------------
-- Table `WorkflowRun`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `WorkflowRun` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `WorkflowRun` (
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
  `StandardOut` TEXT NULL,
  `StandardError` TEXT NULL,
  `WorkflowRun_ID` BIGINT UNSIGNED NOT NULL,
  `Task_TaskID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`InvocationID`),
  INDEX `fk_Invocation_WorkflowRun1_idx` (`WorkflowRun_ID` ASC),
  INDEX `fk_Invocation_Task1_idx` (`Task_TaskID` ASC),
  CONSTRAINT `fk_Invocation_WorkflowRun1`
    FOREIGN KEY (`WorkflowRun_ID`)
    REFERENCES `WorkflowRun` (`ID`)
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
-- Table `StagingEvent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `StagingEvent` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `StagingEvent` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `In` BIT NOT NULL COMMENT 'In our Out',
  `Invocation_InvocationID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_StagingEvent_Invocation1_idx` (`Invocation_InvocationID` ASC),
  CONSTRAINT `fk_StagingEvent_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `FileStagingEvent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `FileStagingEvent` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `FileStagingEvent` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(250) NULL,
  `In` BIT NOT NULL COMMENT 'In our Out',
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
-- Table `TimeStat`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `TimeStat` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `TimeStat` (
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
  `StagingEvent_ID` BIGINT UNSIGNED NULL,
  `FileStagingEvent_ID` BIGINT UNSIGNED NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_TimeStat_StagingEvent1_idx` (`StagingEvent_ID` ASC),
  INDEX `fk_TimeStat_FileStagingEvent1_idx` (`FileStagingEvent_ID` ASC),
  INDEX `fk_TimeStat_Invocation1_idx` (`Invocation_InvocationID` ASC),
  CONSTRAINT `fk_TimeStat_StagingEvent1`
    FOREIGN KEY (`StagingEvent_ID`)
    REFERENCES `StagingEvent` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TimeStat_FileStagingEvent1`
    FOREIGN KEY (`FileStagingEvent_ID`)
    REFERENCES `FileStagingEvent` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TimeStat_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `Output`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `Output` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `Output` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Key` TEXT NOT NULL,
  `Content` TEXT NOT NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_Output_Invocation1_idx` (`Invocation_InvocationID` ASC),
  CONSTRAINT `fk_Output_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `UserEvent`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `UserEvent` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `UserEvent` (
  `ID` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `Content` TEXT NOT NULL,
  `Invocation_InvocationID` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_UserEvent_Invocation1_idx` (`Invocation_InvocationID` ASC),
  CONSTRAINT `fk_UserEvent_Invocation1`
    FOREIGN KEY (`Invocation_InvocationID`)
    REFERENCES `Invocation` (`InvocationID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
