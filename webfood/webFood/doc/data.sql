use webfood
insert into resadmin(raname,rapwd) values('a','Occ175b9c0f1b6a813c399e269772661');
select * from resadmin;

--用户表 初始数据
insert into resuser (username,pwd,email)	values('a','Occ175b9c0f1b6a813c399e269772661','a@163.com');
insert into resuser (username,pwd,email)	values('b','Occ175b9c0f1b6a813c399e269772661','b@163.com');

--插入菜

insert into resfood(fname,normprice,realprice,detail,fphoto)	values('素炒莴笋丝',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('蛋炒饭',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('酸辣鱼',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('鲁粉',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('西红柿炒蛋',22.0,20.0,'营养丰富','50008.jpg');


insert into resfood(fname,normprice,realprice,detail,fphoto)	values('炖鸡',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('炒鸡',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('炒饭',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('手撕前女友',22.0,20.0,'营养丰富','50008.jpg');
insert into resfood(fname,normprice,realprice,detail,fphoto)	values('面条',22.0,20.0,'营养丰富','50008.jpg');

--不测试 ：  生成一条订单   a用户订了  1号才一份   及2 号才2 份

insert into resorder (userid,address,tel,ordertime,deliverytime,ps,status)
values (1,'湖南省衡阳市','15386026653',now(),now(),'送餐上门',0);

insert into resorderitem(roid,fid,dealprice,num)
values(1,1,20,1);

insert into resorderitem(roid,fid,dealprice,num)
values(1,1,20,1);

---注意以上的三条语句 要求在事务中处理
commit;
