CREATE TABLE article (
    id character varying(255) NOT NULL,
    title text,
    abstr text,
    pic character varying(255),
    body text,
    online boolean,
    orderno integer DEFAULT 0,
    rubric_id character varying(255) NOT NULL,
    adate character varying(10),
    visibleforever boolean DEFAULT true,
    visiblefrom date,
    visibleto date
);

CREATE TABLE bannerbox (
    id character varying(40),
);

create table banner (
	id character varying(40),
	banner_box_id character varying(40),
    banner character varying(1024),
    url character varying(2048)
);

CREATE TABLE box (
    id character varying(40) NOT NULL,
    site_id character varying(40),
    boxid character varying(40)
);

CREATE TABLE configuration (
    id character varying(40) NOT NULL,
    thumbsize integer DEFAULT 100 NOT NULL,
    fileurl character varying(256) DEFAULT '/files'::character varying NOT NULL
);


CREATE TABLE rubric (
    id character varying(255) NOT NULL,
    title character varying(255) NOT NULL,
    parent_id character varying(255),
    "mode" character varying(255) NOT NULL,
    orderno integer DEFAULT 0
);

CREATE TABLE rubricbox (
    id character varying(40),
    rubric_id character varying(40)
);

CREATE TABLE site (
    id character varying(40) NOT NULL,
    configuration_id character varying(40),
    root_rubric_id character varying(255) NOT NULL,
    "language" character varying(10)
);


CREATE TABLE textbox (
    id character varying(40),
    title character varying(1024),
    body text
);

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
	inviteKey varchar(40),
	primary key(id)
);

