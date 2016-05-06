-- Sequence: filelink_sequence

-- DROP SEQUENCE filelink_sequence;

CREATE SEQUENCE filelink_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 18
  CACHE 1;
ALTER TABLE filelink_sequence
  OWNER TO postgres;
  
 -- Table: file_links

-- DROP TABLE file_links;

CREATE TABLE file_links
(
  id bigint NOT NULL DEFAULT nextval('filelink_sequence'::regclass),
  directory_id bigint NOT NULL,
  file_id bigint NOT NULL,
  content_id bigint,
  version_number integer,
  version_id bigint,
  name character varying(255) NOT NULL,
  creationdate date NOT NULL DEFAULT ('now'::text)::date,
  creationtime time without time zone NOT NULL DEFAULT ('now'::text)::time with time zone,
  CONSTRAINT file_links_pk PRIMARY KEY (id),
  CONSTRAINT file_links_directory_fk FOREIGN KEY (directory_id)
      REFERENCES directories (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT file_links_uploadedfiles_fk FOREIGN KEY (file_id)
      REFERENCES uploadedfiles (fileid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE file_links
  OWNER TO postgres; 