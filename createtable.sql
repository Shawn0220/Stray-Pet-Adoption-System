create table volunteer
(
	vno char(8) NOT NULL, --���
	vsex char(2) NOT NULL, --�Ա�
	tel char(11) NOT NULL UNIQUE, --�ֻ���
	vloc varchar(12) NOT NULL, --���ڵ�
	vname varchar(10) NOT NULL, --�û���
	vpsw varchar(16) NOT NULL, --����
	PRIMARY KEY(vno),
	check (vsex in ('��','Ů'))
)

create table adopter
(
	ano char(8) NOT NULL,
	asex char(2) NOT NULL,
	tel char(11) NOT NULL UNIQUE,
	aloc varchar(12) NOT NULL,
	aname varchar(10) NOT NULL,
	apsw varchar(16) NOT NULL,
	contractcnt smallint, --ΥԼ����ͳ��
	money int, --�˻����
	PRIMARY KEY(ano),
	check (asex in ('��','Ů'))
)

create table pettype
(
	ptype varchar(20) NOT NULL primary key, --���
	bond float(2) --������Ӧ�ı�֤��
)

create table pet
(
	pno char(8) NOT NULL,
	pname varchar(10) NOT NULL,
	psex char(2) NOT NULL,
	vno char(8) NOT NULL, --�Ըó��︺���־Ը��
	ptype varchar(20) NOT NULL, --���
	pweight int, --����
	dscrb varchar(200), --�������
	isapdated char(6), --����״̬
	PRIMARY KEY(pno),
	FOREIGN KEY(vno) REFERENCES volunteer
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(ptype) REFERENCES pettype
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	check (psex in ('��','��')),
	check (isapdated in ('������','������','������'))
)

create table adpapply --���������
(
	adp_applyno char(8) NOT NULL,
	ano char(8) NOT NULL,
	pno char(8) NOT NULL,
	apply_reason varchar(200),
	isapproval char(4) NOT NULL,
	refuse_reason varchar(100),
	trail_time DATE, --������ʼ����
	traillenght int, --����ʱ��
	formal_time DATE, --��ʽ������ʼ����
	bondstate char(4) check( bondstate in ('δ��','�Ѹ�','�黹','û��')), --�Ƿ��˻�
	PRIMARY KEY(adp_applyno),
	FOREIGN KEY(ano) REFERENCES adopter
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	FOREIGN KEY(pno) REFERENCES pet
		ON DELETE CASCADE
		ON UPDATE CASCADE,
	check (isapproval in ('δ��','ͨ��','�ܾ�')),
	check (isapproval in ('δ��','ͨ��') OR refuse_reason is NOT NULL) --�ܾ�����Ҫ���оܾ�����
)

create table admins --����Ա
(
	adminno char(8),
	pswd varchar(16) NOT NULL,
	PRIMARY KEY(adminno)
)

create table breach --ΥԼ�����
(
	bno char(1),
	behaviour varchar(18) NOT NULL UNIQUE,
	PRIMARY KEY(bno)
)

create table recentstand --�������
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

create table punish --ΥԼ�ͷ�
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

go--�洢���� ����־Ը��
create procedure vol_insert
	(@c1 char(8), @c2 char(2),@c3 char(11),@c4 varchar(12),@c5 varchar(10),@c6 varchar(16)) as
insert into volunteer (vno,vsex,tel,vloc,vname,vpsw)
values (@c1,@c2,@c3,@c4,@c5,@c6)

EXEC vol_insert v1234567,��,15962964901,����ʡ������,user1,pswtest1
EXEC vol_insert v1234568,��,15962961111,����ʡ������,user2,pswtest2
EXEC vol_insert v1234569,��,15962961221,����ʡ������,user3,pswtest3

go--�洢���� ����������
create procedure adopter_insert
	(@c1 char(8), @c2 char(2),@c3 char(11),@c4 varchar(12),@c5 varchar(10),@c6 varchar(16),@c7 int) as
insert into adopter (ano,asex,tel,aloc,aname,apsw,contractcnt,money)
values (@c1,@c2,@c3,@c4,@c5,@c6,0,@c7)

EXEC adopter_insert a1234567,Ů,18962182821,����ʡ������,auser1,pwsss1,200000
EXEC adopter_insert a1234568,Ů,18962122821,����ʡ������,auser2,pws123,800
EXEC adopter_insert a1234569,Ů,18962122811,����ʡ������,auser3,pws143,1000

go--�洢���� �������
create procedure pet_insert
	(@c1 char(8),@c2 varchar(10),@c3 char(2),@c4 char(8),@c5 varchar(20),@c6 int,@c7 varchar(200),@c8 char(6)) as
insert into pet (pno,pname,psex,vno,ptype,pweight,dscrb,isapdated)
values (@c1,@c2,@c3,@c4,@c5,@c6,@c7,@c8)

EXEC pet_insert p1111111,����,��,v1234567,��ë,69,�ǳ��������Ը����,������
EXEC pet_insert p1111112,����,��,v1234567,��è,30,��ճ��,������
EXEC pet_insert p1111113,��èè,����,v1234567,��è,40,�е����,������
EXEC pet_insert p1111114,����,��,v1234568,������,54,����δ����,������
EXEC pet_insert p1111115,����,��,v1234569,�껨è,69,�е�����,������

go--�洢���� ��������
create procedure sendapply
	(@c1 char(8),@c2 char(8),@c3 char(8),@c4 varchar(200)) as
insert into adpapply (adp_applyno,ano,pno,apply_reason,isapproval)
values (@c1,@c2,@c3,@c4,'δ��')

EXEC sendapply s1111111,a1234567,p1111111,�ر�ϲ����ë
EXEC sendapply s1111117,a1234567,p1111112,�ر�ϲ����è
EXEC sendapply s1122117,a9999922,p9292929,������һֻ�˴ո���
EXEC sendapply s1111119,a1234568,p1111112,��Ҳ�ر�ϲ����è
EXEC sendapply s1111127,a1234568,p1111112,��ҲҲ�ر�ϲ����è
EXEC sendapply s1111222,a2132313,p1111112,��ҲҲҲ�ر�ϲ����è
EXEC sendapply s1121137,a1234567,p1111114,С����ÿɰ�
EXEC sendapply s1111137,a1234567,p1111113,��ϲ������è��

go--�洢���� �������� ͬ��
create procedure agreeadp
	(@c1 char(8),@c2 int) as
if ((select count(*) from adpapply where adp_applyno=@c1 AND bondstate='ͨ��')=0)
begin
	update adpapply set isapproval='ͨ��',trail_time=GETDATE(),traillenght=@c2,bondstate='δ��'
	where adp_applyno = @c1
end

EXEC agreeadp s1111117,1
EXEC agreeadp s1111119,1
EXEC agreeadp s1111111,1
EXEC agreeadp s1121137,1
EXEC agreeadp s1122117,1

go--�洢���� �������� �ܾ�
create procedure refuseadp
	(@c1 char(8),@c2 varchar(100)) as
update adpapply set  isapproval='�ܾ�',refuse_reason=@c2
where adp_applyno = @c1

EXEC refuseadp s1111127,�Ҿ����㲻��������ϲ��

go--�洢���� ����֤��
create procedure paybond
	(@c1 char(8)) as
declare @c2 int
if ((select bondstate from adpapply where adp_applyno=@c1) = 'δ��' )--δ����׼�����Ļ��ѽ�����֤��Ĳ�������֤��
begin
	update adopter set money=money-(select bond from pet,pettype,adpapply where pettype.ptype=pet.ptype AND adpapply.pno = pet.pno AND adpapply.adp_applyno = @c1 )
	update adpapply set bondstate = '�Ѹ�' where  adp_applyno=@c1
	update pet set isapdated = '������' where pet.pno = (select pno from adpapply where adp_applyno=@c1)
end
else PRINT '���뻹δͨ�����ѽ�����֤�𣬲��ܽ���֤��'

EXEC paybond s1111137
EXEC paybond s1121137
EXEC paybond s1111222
EXEC paybond s1122117

go--����3��ΥԼ�Ĳ�������������
create trigger adopter_apply on adpapply
for insert
as
	declare @c1 int
	select @c1 = contractcnt from adopter 
	where ano = (select ano from inserted)
	if (@c1>=3)
	begin
		ROLLBACK
		PRINT '��ΥԼ�������࣬��������������'
	end

EXEC sendapply s1111112,a1234511,p1111111,��Ҳ�ر�ϲ����ë

go--��������3���Ĳ�������������
create trigger adopter_apply1 on adpapply
for insert
as
	declare @c1 int
	select @c1 = count(*) from adpapply
	where ano = (select ano from inserted) AND (trail_time is NOT NULL)
	if (@c1>=3)
	begin
		ROLLBACK
		PRINT '�������ĳ�����࣬�������������ʿһ�����'
	end

go--�ѱ������ĳ��ﲻ������������
create trigger adopter_apply2 on adpapply
for insert
as
	declare @c1 int
	select @c1 = count(*) from adpapply
	where pno = (select pno from inserted) AND isapproval = 'ͨ��'
	if (@c1>=1)
	begin
		ROLLBACK
		PRINT '���������ĳ����ѱ��������������뿴����ĳ�����'
	end

EXEC sendapply s1111113,a1231122,p1111111,��ҲҲ�ر�ϲ����ë

go--�����ߺͶ�Ӧ־Ը�߱�����ͬһ��ַ
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
		PRINT '�������ڵغͶԸó��︺���־Ը�߲�һ���أ��������������뿴����ĳ�����'
	end

EXEC sendapply s1111114,a1231122,p1111112,���ر�ϲ����è

go--��׼����������������Ǯ���ڶ���
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
		PRINT '�������˻����С�ڸó���ı�֤�𣬲�����׼������Ŷ'
	end

EXEC agreeadp s1111222,1

go --��ͼ ����������
create view ��ѯ�������ĳ��� as
select pet.pno ������,pname ��������,psex �����Ա�,ptype ��������, pweight ��������,dscrb ��������
from pet,adpapply where isapdated = '������'

go
select * from ��ѯ�������ĳ���