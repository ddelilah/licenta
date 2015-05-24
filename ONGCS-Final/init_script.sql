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
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(1,1,30,'cpu1',1);
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(2,2,40,'cpu2',1);
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(3,4,2601,'cpu3',1);
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(4,8,1000,'cpu4',1);
/*server configs*/
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(5,16,1000,'cpu5',1);
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(6,32,1000,'cpu6',1);
INSERT INTO `licenta`.`cpu`(`cpu_id`,`nr_cores`,`frequency`,`name`,`cpu_utilization`)VALUES(7,20,2500,'dellCpu',1);

select * from cpu;

/*vm configs
capacity MB*/
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(1,1024,'hdd1');
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(2,10240,'hdd2');
select * from hdd;
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(3,262144,'dellHdd');
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(4,50331648,'dellHdd');

/*vm configs
capacity MB*/
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(1,512,'ram1');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(2,2048,'ram2');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(3,4096,'ram3');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(4,8192,'ram4');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(5,16384,'ram5');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(7,524288,'dellRam');

select * from ram;
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(6,131072,'ram5');


INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(1,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(2,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(3,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(4,'vm1.large','PENDING',3,4,2,800, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(5,'vm1.xlarge','PENDING',4,5,2,1000, NULL);
select * from vm;


INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(1,1048576,'off','rack01',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(2,1048576,'off','rack02',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(3,1048576,'off','rack03',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(4,1048576,'off','rack04',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(5,1048576,'off','rack05',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(6,1048576,'off','rack06',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(7,1048576,'off','rack07',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(8,1048576,'off','rack08',0,0,0);

select * from rack;

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(1,'host01','off',7,7,3,0,0,235,0,1,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(2,'host02','off',7,7,3,0,0,235,0,1,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(3,'host03','off',7,7,3,0,0,235,0,1,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(4,'host04','off',7,7,3,0,0,235,0,1,2660);

select * from server;
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(5,'host05','off',7,7,3,0,0,235,0,2,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(6,'host06','off',7,7,3,0,0,235,0,2,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(7,'host07','off',7,7,3,0,0,235,0,2,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(8,'host08','off',7,7,3,0,0,235,0,2,2660);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(9,'host09','off',7,7,3,0,0,235,0,3,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(10,'host10','off',7,7,3,0,0,235,0,3,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(11,'host11','off',7,7,3,0,0,235,0,3,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(12,'host12','off',7,7,3,0,0,235,0,3,2660);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(13,'host13','off',7,7,3,0,0,235,0,4,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(14,'host14','off',7,7,3,0,0,235,0,4,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(15,'host15','off',7,7,3,0,0,235,0,4,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(16,'host16','off',7,7,3,0,0,235,0,4,2660);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(17,'host17','off',7,7,3,0,0,235,0,5,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(18,'host18','off',7,7,3,0,0,235,0,5,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(19,'host19','off',7,7,3,0,0,235,0,5,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(20,'host20','off',7,7,3,0,0,235,0,5,2660);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(21,'host21','off',7,7,3,0,0,235,0,6,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(22,'host22','off',7,7,3,0,0,235,0,6,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(23,'host23','off',7,7,3,0,0,235,0,6,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(24,'host24','off',7,7,3,0,0,235,0,6,2660);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(25,'host25','off',7,7,3,0,0,235,0,7,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(26,'host26','off',7,7,3,0,0,235,0,7,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(27,'host27','off',7,7,3,0,0,235,0,7,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(28,'host28','off',7,7,3,0,0,235,0,7,2660);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(29,'host29','off',7,7,3,0,0,235,0,8,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(30,'host30','off',7,7,3,0,0,235,0,8,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(31,'host31','off',7,7,3,0,0,235,0,8,2660);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(32,'host32','off',7,7,3,0,0,235,0,8,2660);
