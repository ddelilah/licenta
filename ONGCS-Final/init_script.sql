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

select * from cpu;

/*vm configs
capacity GB*/
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(1,1024,'hdd1');
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(2,10240,'hdd2');
select * from hdd;
INSERT INTO `licenta`.`hdd`(`hdd_id`,`capacity`,`name`)VALUES(3,163840,'hdd3');

/*vm configs
capacity MB*/
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(1,512,'ram1');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(2,2048,'ram2');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(3,4096,'ram3');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(4,8192,'ram4');
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(5,16384,'ram5');

select * from ram;
INSERT INTO `licenta`.`ram`(`ram_id`,`capacity`,`name`)VALUES(6,131072,'ram5');


INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(1,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(2,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(3,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(4,'vm1.large','PENDING',3,4,2,800, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(5,'vm1.xlarge','PENDING',4,5,2,1000, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(6,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(7,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(8,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(9,'vm1.large','PENDING',3,4,2,800, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(10,'vm1.xlarge','PENDING',4,5,2,1000, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(11,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(12,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(13,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(14,'vm1.large','PENDING',3,4,2,800, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(15,'vm1.xlarge','PENDING',4,5,2,1000, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(16,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(17,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(18,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(19,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(20,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(21,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(22,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(23,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(24,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(25,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(26,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(27,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(28,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(29,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(30,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(31,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(32,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(33,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(34,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(35,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(36,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(37,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(38,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(39,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(40,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(41,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(42,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(43,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(44,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(45,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(46,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(47,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(48,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(49,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(50,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(51,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(52,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(53,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(54,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(55,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(56,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(57,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(58,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(59,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(60,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(61,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(62,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(63,'vm1.medium','PENDING',2,3,2,600, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(64,'vm1.tiny','PENDING',1,1,1,250, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(65,'vm1.small','PENDING',1,2,2,400, NULL);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(66,'vm1.medium','PENDING',2,3,2,600, NULL);
select * from vm;


INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(1,16,'off','rack01',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(2,16,'off','rack02',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(3,16,'off','rack03',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(4,16,'off','rack04',0,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(5,16,'off','rack05',0,0,0);

select * from rack;

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(1,'host01','off',5,6,3,0,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(2,'host02','off',6,6,3,0,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(3,'host03','off',6,6,3,0,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(4,'host04','off',5,6,3,0,0,350,0,4,2000);
select * from server;
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(5,'host05','off',5,6,3,0,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(6,'host06','off',6,6,3,0,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(7,'host07','off',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(8,'host08','off',5,6,3,0,0,350,0,1,2000);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(9,'host09','off',5,6,3,0,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(10,'host10','off',6,6,3,0,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(11,'host11','off',6,6,3,0,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(12,'host12','off',5,6,3,0,0,350,0,2,2000);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(13,'host13','off',5,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(14,'host14','off',6,6,3,0,0,350,0,4,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(15,'host15','off',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(16,'host16','off',5,6,3,0,0,350,0,4,2000);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(17,'host17','off',5,6,3,0,0,350,0,4,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(18,'host18','off',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(19,'host19','off',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(20,'host20','off',5,6,3,0,0,350,0,4,2000);
