use licenta;
DELETE FROM cpu;
delete from ram;
delete from hdd;
delete from vm;
delete from server; 
delete from rack;
delete from cooling; 

/*vm configs
frequency MHz & cpu_utilization not accurate
*/
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`)VALUES(1,1,30,'cpu1');
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`)VALUES(2,2,40,'cpu2');
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`)VALUES(3,4,2601,'cpu3');
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`)VALUES(4,8,1000,'cpu4');
/*server configs*/
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`)VALUES(5,16,1000,'cpu5');
select * from cpu;

/*vm configs
capacity GB*/
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(1,1,'hdd1');
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(2,10,'hdd2');
select * from hdd;

/*vm configs
capacity MB*/
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(1,512,'ram1');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(2,2048,'ram2');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(3,4096,'ram3');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(4,8192,'ram4');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(5,16384,'ram5');

select * from ram;

INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`)VALUES(1,'vm1.tiny','on',1,1,1);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`)VALUES(2,'vm1.small','on',1,2,2);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`)VALUES(3,'vm1.medium','on',2,3,2);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`)VALUES(4,'vm1.large','on',3,4,2);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`)VALUES(5,'vm1.xlarge','on',4,5,2);
select * from vm;

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`)VALUES(1,'host01','on',5,5,2,0);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`)VALUES(2,'host02','on',5,5,2,0);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`)VALUES(3,'host03','on',5,5,2,0);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`)VALUES(4,'host04','on',5,5,2,0);
select * from server;

INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`)VALUES(1,16,'on','rack01',0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`)VALUES(2,16,'on','rack02',0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`)VALUES(3,16,'on','rack03',0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`)VALUES(4,16,'on','rack04',0);
select * from rack;

