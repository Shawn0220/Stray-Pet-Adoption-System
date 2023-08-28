--触发器限制一个领养者最多领养3个宠物

--存储过程 插入志愿者
create procedure vol_insert
	(@c1 char(8), @c2 char(2),@c3 char(11),@c4 varchar(12),@c5 varchar(10),@c6 varchar(16)) as
insert into adpapply

--存储过程 申请领养
create procedure adopt_insert 
	(@c1 char(8), @c2 char(8), @c3 char(8), @c4 varchar(200), @c5 char(4)='未批') as
insert into adpapply(adp_applyno,ano,pno,apply_reason,isapproval)
values(@c1, @c2, @c3, @c4, @c5)

/*执行实例*/EXEC adopt_insert ap122212, a2222222, p1123123, '我也想要宠物的陪伴'

go--存储过程 同意领养
create procedure agree_apply (@c1 char(8)) as -- c1 申请领养单号
update adpapply set isapproval = '通过', trail_time = getdate(), traillenght = 1
insert into scrty (adp_applyno, bondnum, ispay, isconfiscate, isreturn)
	select adp_applyno, bond, 'false' , 'false' , 'false' 
	from adpapply,pet,pettype
	where adpapply.pno = pet.pno AND pet.ptype = pettype.ptype AND adpapply.adp_applyno = @c1

/*执行实例*/EXEC agree_apply ap999999

go--存储过程 拒绝领养
create procedure refuse_apply(@c1 char(8)) as -- c1 申请领养单号
update  adpapply set isapproval = '拒绝'

/*执行实例*/EXEC agree_apply ap999999




go--触发器 同意领养后生成保证金单等待支付
create trigger applyagree on adpapply
for update as
insert into scrty (adp_applyno, bondnum, ispay, isconfiscate, isreturn)
	select adp_applyno, bond, 'false' , 'false' , 'false' 
	from adpapply,pet,pettype 
	where adpapply.pno = pet.pno AND pet.ptype = pettype.ptype AND adpapply.pno in (select pno from inserted where isapproval = '通过')



go--



go--超过3次违约的不允许申请领养
create trigger adopter_apply on adpapply
for insert
as
	declare @c1 int
	select @c1 = contractcnt from adopter 
	where ano = (select ano from inserted)
	if (@c1>=4)
	begin
		ROLLBACK
		PRINT '您违约次数过多，不允许申请领养'
	end

EXEC adopt_insert 'ap1230000','a1230007','p1230007','喜欢田园犬'

--交付保证金，扣费 ispay置true pet试养

--当认定违约后，增加adopter的违约次数