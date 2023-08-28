create table volunteer
(
	vno char(8) NOT NULL, --编号
	vsex char(2) NOT NULL, --性别
	tel char(11) NOT NULL UNIQUE, --手机号
	vloc varchar(12) NOT NULL, --所在地
	vname varchar(10) NOT NULL, --用户名
	vpsw varchar(16) NOT NULL, --密码
	PRIMARY KEY(vno),
	check (vsex in ('男','女'))
)

create table adopter
(
	ano char(8) NOT NULL,
	asex char(2) NOT NULL,
	tel char(11) NOT NULL UNIQUE,
	aloc varchar(12) NOT NULL,
	aname varchar(10) NOT NULL,
	apsw varchar(16) NOT NULL,
	contractcnt smallint, --违约次数统计
	money int, --账户余额
	PRIMARY KEY(ano),
	check (asex in ('男','女'))
)

create table pettype
(
	ptype varchar(20) NOT NULL primary key, --类别
	bond float(2) --该类别对应的保证金
)

create table pet
(
	pno char(8) NOT NULL,
	pname varchar(10) NOT NULL,
	psex char(2) NOT NULL,
	vno char(8) NOT NULL, --对该宠物负责的志愿者
	ptype varchar(20) NOT NULL, --类别
	pweight int, --体重
	dscrb varchar(200), --情况描述
	isapdated char(6), --领养状态
	PRIMARY KEY(pno),
	FOREIGN KEY(vno) REFERENCES volunteer
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(ptype) REFERENCES pettype
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	check (psex in ('雄','雌')),
	check (isapdated in ('试养期','待领养','已收养'))
)

create table adpapply --领养申请表
(
	adp_applyno char(8) NOT NULL,
	ano char(8) NOT NULL,
	pno char(8) NOT NULL,
	apply_reason varchar(200),
	isapproval char(4) NOT NULL,
	refuse_reason varchar(100),
	trail_time DATE, --试养开始日期
	traillenght int, --试养时长
	formal_time DATE, --正式领养开始日期
	bondstate char(4) check( bondstate in ('未付','已付','归还','没收')), --是否退回
	PRIMARY KEY(adp_applyno),
	FOREIGN KEY(ano) REFERENCES adopter
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(pno) REFERENCES pet
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	check (isapproval in ('未批','通过','拒绝')),
	check (isapproval in ('未批','通过') OR refuse_reason is NOT NULL) --拒绝必须要求有拒绝理由
)

create table admins --管理员
(
	adminno char(8),
	pswd varchar(16) NOT NULL,
	PRIMARY KEY(adminno)
)

create table breach --违约情况表
(
	bno char(1),
	behaviour varchar(18) NOT NULL UNIQUE,
	PRIMARY KEY(bno)
)

create table recentstand --宠物近况
(
	rno char(8),
	adp_applyno char(8) NOT NULL,
	rdescribe varchar(200) NOT NULL,
	rfile varchar(100) NOT NULL,
	updtime DATE NOT NULL,
	PRIMARY KEY(rno),
	FOREIGN KEY(adp_applyno) REFERENCES adpapply
		ON DELETE CASCADE
		ON UPDATE CASCADE,
)

create table punish --违约惩罚
(
	adminno char(8),
	rno char(8),
	bno char(1),
	pdate DATE NOT NULL,
	PRIMARY KEY(adminno,rno,bno),
	FOREIGN KEY(adminno) REFERENCES admins
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(rno) REFERENCES recentstand
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(bno) REFERENCES breach
		ON DELETE CASCADE
		ON UPDATE CASCADE
)

go--存储过程 插入志愿者
create procedure vol_insert
	(@c1 char(8), @c2 char(2),@c3 char(11),@c4 varchar(12),@c5 varchar(10),@c6 varchar(16)) as
insert into volunteer (vno,vsex,tel,vloc,vname,vpsw)
values (@c1,@c2,@c3,@c4,@c5,@c6)

EXEC vol_insert v1234567,男,15962964901,江苏省常州市,user1,pswtest1
EXEC vol_insert v1234568,男,15962961111,江苏省常州市,user2,pswtest2
EXEC vol_insert v1234569,男,15962961221,江苏省苏州市,user3,pswtest3

go--存储过程 插入领养者
create procedure adopter_insert
	(@c1 char(8), @c2 char(2),@c3 char(11),@c4 varchar(12),@c5 varchar(10),@c6 varchar(16),@c7 int) as
insert into adopter (ano,asex,tel,aloc,aname,apsw,contractcnt,money)
values (@c1,@c2,@c3,@c4,@c5,@c6,0,@c7)

EXEC adopter_insert a1234567,女,18962182821,江苏省常州市,auser1,pwsss1,200000
EXEC adopter_insert a1234568,女,18962122821,江苏省常州市,auser2,pws123,800
EXEC adopter_insert a1234569,女,18962122811,江苏省常州市,auser3,pws143,1000

go--存储过程 插入宠物
create procedure pet_insert
	(@c1 char(8),@c2 varchar(10),@c3 char(2),@c4 char(8),@c5 varchar(20),@c6 int,@c7 varchar(200),@c8 char(6)) as
insert into pet (pno,pname,psex,vno,ptype,pweight,dscrb,isapdated)
values (@c1,@c2,@c3,@c4,@c5,@c6,@c7,@c8)

EXEC pet_insert p1111111,旺仔,雄,v1234567,金毛,69,非常健康且性格活泼,待领养
EXEC pet_insert p1111112,波波,雄,v1234567,橘猫,30,很粘人,待领养
EXEC pet_insert p1111113,肥猫猫,雌性,v1234567,橘猫,40,有点肥胖,待领养
EXEC pet_insert p1111114,大王,雄,v1234568,吉娃娃,54,瘦弱未驱虫,待领养
EXEC pet_insert p1111115,花花,雄,v1234569,狸花猫,69,有点怕人,待领养

go--存储过程 领养申请
create procedure sendapply
	(@c1 char(8),@c2 char(8),@c3 char(8),@c4 varchar(200)) as
insert into adpapply (adp_applyno,ano,pno,apply_reason,isapproval)
values (@c1,@c2,@c3,@c4,'未批')

EXEC sendapply s1111111,a1234567,p1111111,特别喜欢金毛
EXEC sendapply s1111117,a1234567,p1111112,特别喜欢橘猫
EXEC sendapply s1122117,a9999922,p9292929,家里有一只了凑个伴
EXEC sendapply s1111119,a1234568,p1111112,我也特别喜欢橘猫
EXEC sendapply s1111127,a1234568,p1111112,我也也特别喜欢橘猫
EXEC sendapply s1111222,a2132313,p1111112,我也也也特别喜欢橘猫
EXEC sendapply s1121137,a1234567,p1111114,小动物好可爱
EXEC sendapply s1111137,a1234567,p1111113,最喜欢大橘猫了

go--存储过程 领养审批 同意
create procedure agreeadp
	(@c1 char(8),@c2 int) as
if ((select count(*) from adpapply where adp_applyno=@c1 AND bondstate='通过')=0)
begin
	update adpapply set isapproval='通过',trail_time=GETDATE(),traillenght=@c2,bondstate='未付'
	where adp_applyno = @c1
end

EXEC agreeadp s1111117,1
EXEC agreeadp s1111119,1
EXEC agreeadp s1111111,1
EXEC agreeadp s1121137,1
EXEC agreeadp s1122117,1

go--存储过程 领养审批 拒绝
create procedure refuseadp
	(@c1 char(8),@c2 varchar(100)) as
update adpapply set  isapproval='拒绝',refuse_reason=@c2
where adp_applyno = @c1

EXEC refuseadp s1111127,我觉得你不是真正的喜欢

go--存储过程 交保证金
create procedure paybond
	(@c1 char(8)) as
declare @c2 int
if ((select bondstate from adpapply where adp_applyno=@c1) = '未付' )--未被批准领养的或已交过保证金的不允许交保证金
begin
	update adopter set money=money-(select bond from pet,pettype,adpapply where pettype.ptype=pet.ptype AND adpapply.pno = pet.pno AND adpapply.adp_applyno = @c1 )
	update adpapply set bondstate = '已付' where  adp_applyno=@c1
	update pet set isapdated = '试养期' where pet.pno = (select pno from adpapply where adp_applyno=@c1)
end
else PRINT '申请还未通过或已交过保证金，不能交保证金'

EXEC paybond s1111137
EXEC paybond s1121137
EXEC paybond s1111222
EXEC paybond s1122117

go--超过3次违约的不允许申请领养
create trigger adopter_apply on adpapply
for insert
as
	declare @c1 int
	select @c1 = contractcnt from adopter 
	where ano = (select ano from inserted)
	if (@c1>=3)
	begin
		ROLLBACK
		PRINT '您违约次数过多，不允许申请领养'
	end

EXEC sendapply s1111112,a1234511,p1111111,我也特别喜欢金毛

go--领养超过3个的不允许申请领养
create trigger adopter_apply1 on adpapply
for insert
as
	declare @c1 int
	select @c1 = count(*) from adpapply
	where ano = (select ano from inserted) AND (trail_time is NOT NULL)
	if (@c1>=3)
	begin
		ROLLBACK
		PRINT '您领养的宠物过多，请给其他爱心人士一点机会'
	end

go--已被领养的宠物不允许申请领养
create trigger adopter_apply2 on adpapply
for insert
as
	declare @c1 int
	select @c1 = count(*) from adpapply
	where pno = (select pno from inserted) AND isapproval = '通过'
	if (@c1>=1)
	begin
		ROLLBACK
		PRINT '您想领养的宠物已被别人领走啦，请看看别的宠物呢'
	end

EXEC sendapply s1111113,a1231122,p1111111,我也也特别喜欢金毛

go--领养者和对应志愿者必须在同一地址
create trigger adopter_apply3 on adpapply
for insert
as
	declare @c1 int
	select @c1 = count (*)
	from adopter,volunteer,pet,adpapply
	where adpapply.pno = pet.pno AND pet.vno = volunteer.vno AND adpapply.pno = (select pno from inserted) AND volunteer.vloc=adopter.aloc AND adopter.ano = (select ano from inserted)
	if (@c1 = 0)
	begin
		ROLLBACK
		PRINT '您的所在地和对该宠物负责的志愿者不一致呢，不方便领养，请看看别的宠物呢'
	end

EXEC sendapply s1111114,a1231122,p1111112,我特别喜欢橘猫

go--批准领养必须在领养者钱大于定金
create trigger volunteer_agree on adpapply
for update
as
	declare @c1 int
	select @c1 = money
	from adopter
	where adopter.ano = (select ano from inserted)
	declare @c2 int
	select @c2 = bond
	from pet,pettype
	where pet.pno = (select pno from inserted) and pet.ptype = pettype.ptype
	if ( @c1 < @c2 )
	begin
		ROLLBACK
		PRINT '领养者账户余额小于该宠物的保证金，不能批准他领养哦'
	end

EXEC agreeadp s1111222,1

go --视图 可领养宠物
create view 查询可领养的宠物 as
select pet.pno 宠物编号,pname 宠物名称,psex 宠物性别,ptype 宠物种类, pweight 宠物体重,dscrb 总体描述
from pet,adpapply where isapdated = '待领养'

go
select * from 查询可领养的宠物