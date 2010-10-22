alter table rubric add column url varchar(256);
create table banner (
	id character varying(40),
	banner_box_id character varying(40),
    banner character varying(1024),
    url character varying(2048)
);

insert into banner (id, banner_box_id, url, banner) (select id, id, url, banner from bannerbox);

update banner set banner_box_id = id;

alter table bannerbox drop column banner;
alter table bannerbox drop column url;



create table walrususer (
	id varchar(40) not null, 
	firstName  varchar(40),
	lastName varchar(40),
	birthDay date,

	company  varchar(256),
	companyAddress varchar(256),
	position varchar(40),

	aboutMe varchar(2048),

	phone varchar(40),
	email varchar(255) not null, 
	password varchar(40), 
	role varchar(40), 
	inviteKey varchar(40) 
primary key(id));

insert into walrususer (id, email, password, role) values('42', 'admin', '21232f297a57a5a743894a0e4a801fc3', 'ROLE_ADMIN');



delete from banner;
delete from bannerbox;
delete from textbox;
delete from rubricbox;
delete from box;
delete from modification;
delete from visit;
delete from site;
delete from rubric;
