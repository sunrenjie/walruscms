CREATE TABLE box (
    id character varying(40) NOT NULL,
    site_id character varying(40),
    boxid character varying(40)
);

CREATE TABLE bannerbox (
    id character varying(40),
    banner character varying(1024),
    url character varying(2048)
);

CREATE TABLE rubricbox (
    id character varying(40),
    rubric_id character varying(40)
);

CREATE TABLE textbox (
    id character varying(40),
    title character varying(1024),
    body text
);

ALTER TABLE article ADD COLUMN visibleForever BOOLEAN DEFAULT 't';
ALTER TABLE article ADD COLUMN visibleFrom DATE;
ALTER TABLE article ADD COLUMN visibleTo DATE;