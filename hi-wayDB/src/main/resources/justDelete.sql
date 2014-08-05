delete from hiwaydb.userevent where id > 0;
delete from hiwaydb.hiwayevent where id > 0;
delete from hiwaydb.file where id > 0;
delete from hiwaydb.accesstime where id > 0;
delete from hiwaydb.inoutput where id > 0;
delete from hiwaydb.invocation where id > 0;
delete from hiwaydb.task where taskid > 0;
delete from hiwaydb.workflowrun where id > 0;


CREATE TABLE `hiwaydb`.`Accesstime` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `funktion` VARCHAR(100) NULL,
  `input` VARCHAR(100) NULL,
  `tick` BIGINT UNSIGNED NULL,
  `tock` BIGINT UNSIGNED NULL,
  `ticktockdif` BIGINT UNSIGNED NULL,
  `dbvolume` BIGINT UNSIGNED NULL,
  `returnvolume` BIGINT UNSIGNED NULL,
  PRIMARY KEY (`id`));
