CREATE TABLE Base_Word
(
  base_word_id serial not null,
  b_word varchar(100),

  CONSTRAINT base_word_pk PRIMARY KEY (base_word_id)
);


CREATE TABLE Variant_Word
(
  variant_word_id serial not null,
  v_word varchar(100),
  base_word_id integer,
  base_not_found boolean default false,
  found_in_shop boolean default false,

  
  CONSTRAINT variant_word_pk PRIMARY KEY (variant_word_id)
);


ALTER TABLE Variant_Word ADD CONSTRAINT Variant_Base_word_fk
FOREIGN KEY (base_word_id)
REFERENCES base_word (base_word_id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

CREATE UNIQUE INDEX Variant_Word_index
 ON Variant_Word
 ( v_word );


insert into base_word (base_word_id,b_word) VALUES (-1,'invalid');
