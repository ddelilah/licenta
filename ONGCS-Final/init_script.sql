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


INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(1,'vm1.tiny','RUNNING',1,1,1,250,1);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(2,'vm1.small','RUNNING',1,2,2,400,2);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(3,'vm1.medium','RUNNING',2,3,2,600,1);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(4,'vm1.large','RUNNING',3,4,2,800,3);
INSERT INTO `licenta`.`vm`(`vm_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`vm_mips`,`server_id`)VALUES(5,'vm1.xlarge','RUNNING',4,5,2,1000,1);
select * from vm;

INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(1,16,'on','rack01',70,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(2,16,'on','rack02',20,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(3,16,'on','rack03',10,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(4,16,'on','rack04',50,0,0);
INSERT INTO `licenta`.`rack`(`rack_id`,`capacity`,`state`,`name`,`utilization`,`cooling_value`,`power_value`)VALUES(5,16,'on','rack05',50,0,0);

select * from rack;

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(1,'host01','on',5,6,3,70,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(2,'host02','on',6,6,3,50,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(3,'host03','on',6,6,3,0,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(4,'host04','on',5,6,3,60,0,350,0,4,2000);
select * from server;
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(5,'host05','on',5,6,3,70,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(6,'host06','on',6,6,3,50,0,350,0,1,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(7,'host07','on',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(8,'host08','on',5,6,3,60,0,350,0,1,2000);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(9,'host09','on',5,6,3,70,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(10,'host10','on',6,6,3,50,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(11,'host11','on',6,6,3,0,0,350,0,2,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(12,'host12','on',5,6,3,60,0,350,0,2,2000);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(13,'host13','on',5,6,3,70,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(14,'host14','on',6,6,3,50,0,350,0,4,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(15,'host15','on',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(16,'host16','on',5,6,3,60,0,350,0,4,2000);

INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(17,'host17','on',5,6,3,70,0,350,0,4,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(18,'host18','on',6,6,3,50,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(19,'host19','on',6,6,3,0,0,350,0,3,2000);
INSERT INTO `licenta`.`server`(`server_id`,`name`,`state`,`cpu_id`,`ram_id`,`hdd_id`,`utilization`,`cooling_value`,`e_idle`,`power_value`,`rack_id`,`server_MIPS`)VALUES(20,'host20','on',5,6,3,60,0,350,0,4,2000);
