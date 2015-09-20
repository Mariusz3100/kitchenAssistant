CREATE TABLE Problem
(
  p_id serial not null,
  next_p_id int,
  message varchar(100) not null,
  solved boolean not null default false,

 CONSTRAINT problem_pk PRIMARY KEY (p_id)
);




ALTER TABLE Problem ADD CONSTRAINT P_nextP_fk
FOREIGN KEY (next_p_id)
REFERENCES Problem (p_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;
