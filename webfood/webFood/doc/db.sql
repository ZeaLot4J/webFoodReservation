-------------------------------------------------------------------
----			创建库，表，约束，过程，用户，权限等脚本
-------------------------------------------------------------------

create database webfood;

use webfood;

create table resadmin(
	raid int primary key auto_increment,
	raname varchar(50),
	rapwd varchar(50)
)engine=MYISAM character set utf8 ;
use webfood;
create table resuser(
	userid int primary key auto_increment,
	username varchar(50),
	pwd varchar(50),
	email varchar(500)
)engine=MYISAM character set utf8 ;

--normprice：原价   realprice：现价  description：简介  detail：详细的
use webfood;
create table resfood(
	fid int primary key auto_increment,
	fname varchar(50),
	normprice numeric(8,2),
	realprice numeric(8,2),
	detail varchar(2000),
	fphoto varchar(1000)
)engine=MYISAM character set utf8 ;

--订单表:		roid :订单号		userid:外键 ，下单的用户编号  ordertime:下单时间  uname :收货人姓名 
--					deliverytype：送货方式    payment :支付方式		ps :备注 
use webfood;
create table resorder(
	roid int primary key auto_increment,
	userid int,
	address varchar(500),
	tel varchar(100),
	ordertime date,
	deliverytime date,
	ps  varchar(2000),
	status int 
)engine=MYISAM character set utf8 ;


--订单表的下单人号 与用户表中的客户编号有主外键关系
use webfood;
alter table resorder
		add constraint fk_resorder
				foreign key (userid)  references resuser(userid);
				
--dealprice ：成交价		roid :订单号  		fid：商品号		num：数量
use webfood;
create table  resorderitem(
	roiid int primary key auto_increment,
	roid int,
	fid int,
	dealprice numeric(8,2),
	num int 
)engine=MYISAM character set utf8 ;
use webfood;
alter table resorderitem
		add constraint fk_resorderitem_roid
				foreign key (roid)  references resorder(roid);
				
alter table resorderitem
		add constraint fk_tbl_res_fid
				foreign key (fid)  references resfood(fid);				

commit;














