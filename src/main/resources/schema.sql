CREATE TABLE IF NOT EXISTS store_employee (
	id_ int8 NOT NULL,
	lastname varchar(100) NOT NULL,
	firstname varchar(100) NOT NULL,
	patronymic varchar(100) NOT NULL,
	birth_date timestamp NOT NULL,
	position_id int8 NOT NULL,
	gender bool NOT NULL,
	CONSTRAINT store_employee_pkey PRIMARY KEY (id_)
);

CREATE TABLE IF NOT EXISTS counter (
	"name" varchar(75) NOT NULL,
	currentid int8 NULL,
	CONSTRAINT counter_pkey PRIMARY KEY (name)
);