--����������һ���������������3������

--�洢���� ����־Ը��
create procedure vol_insert
	(@c1 char(8), @c2 char(2),@c3 char(11),@c4 varchar(12),@c5 varchar(10),@c6 varchar(16)) as
insert into adpapply

--�洢���� ��������
create procedure adopt_insert 
	(@c1 char(8), @c2 char(8), @c3 char(8), @c4 varchar(200), @c5 char(4)='δ��') as
insert into adpapply(adp_applyno,ano,pno,apply_reason,isapproval)
values(@c1, @c2, @c3, @c4, @c5)

/*ִ��ʵ��*/EXEC adopt_insert ap122212, a2222222, p1123123, '��Ҳ��Ҫ��������'

go--�洢���� ͬ������
create procedure agree_apply (@c1 char(8)) as -- c1 ������������
update adpapply set isapproval = 'ͨ��', trail_time = getdate(), traillenght = 1
insert into scrty (adp_applyno, bondnum, ispay, isconfiscate, isreturn)
	select adp_applyno, bond, 'false' , 'false' , 'false' 
	from adpapply,pet,pettype
	where adpapply.pno = pet.pno AND pet.ptype = pettype.ptype AND adpapply.adp_applyno = @c1

/*ִ��ʵ��*/EXEC agree_apply ap999999

go--�洢���� �ܾ�����
create procedure refuse_apply(@c1 char(8)) as -- c1 ������������
update  adpapply set isapproval = '�ܾ�'

/*ִ��ʵ��*/EXEC agree_apply ap999999




go--������ ͬ�����������ɱ�֤�𵥵ȴ�֧��
create trigger applyagree on adpapply
for update as
insert into scrty (adp_applyno, bondnum, ispay, isconfiscate, isreturn)
	select adp_applyno, bond, 'false' , 'false' , 'false' 
	from adpapply,pet,pettype 
	where adpapply.pno = pet.pno AND pet.ptype = pettype.ptype AND adpapply.pno in (select pno from inserted where isapproval = 'ͨ��')



go--



go--����3��ΥԼ�Ĳ�������������
create trigger adopter_apply on adpapply
for insert
as
	declare @c1 int
	select @c1 = contractcnt from adopter 
	where ano = (select ano from inserted)
	if (@c1>=4)
	begin
		ROLLBACK
		PRINT '��ΥԼ�������࣬��������������'
	end

EXEC adopt_insert 'ap1230000','a1230007','p1230007','ϲ����԰Ȯ'

--������֤�𣬿۷� ispay��true pet����

--���϶�ΥԼ������adopter��ΥԼ����