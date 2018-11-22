CREATE TABLE uploadedfiles
(
	FileId             BIGINT IDENTITY NOT NULL,
	FileName           VARCHAR (255) NULL,
	FilePath           VARCHAR (750) NULL,
	CreationUserId     VARCHAR (64) NULL,
	CreationDate       VARCHAR (10) NULL,
	CreationTime       VARCHAR (8) NULL,
	FileSize           VARCHAR (20) NULL,
	Locked             TINYINT NULL,
	LockingUserId      VARCHAR (64) NULL,
	ModificationUserId VARCHAR (64) NULL,
	ModificationDate   VARCHAR (10) NULL,
	ModificationTime   VARCHAR (8) NULL,
	Description        VARCHAR (1024) NULL,
	versionnumber      INT NULL,
	filetypeid         BIGINT NULL,
	CONSTRAINT PK__uploadedfiles PRIMARY KEY (FileId)
)
;

CREATE TABLE directories
(
	id                   INT IDENTITY NOT NULL,
	dir_name             VARCHAR (255) NOT NULL,
	dir_path             VARCHAR (750) NOT NULL,
	creation_user_id     VARCHAR (200) NULL,
	creation_date        DATETIME NULL,
	creation_time        DATETIME NULL,
	modification_user_id VARCHAR (200) NULL,
	modification_date    DATETIME NULL,
	modification_time    DATETIME NULL,
	is_protected         BIT DEFAULT 0 NOT NULL,
	cmdr                 VARCHAR (2048) NULL,
	cod                  VARCHAR (2048) NULL,
	cud                  VARCHAR (2048) NULL,
	ccd                  VARCHAR (2048) NULL,
	crd                  VARCHAR (2048) NULL,
 	ctd                  VARCHAR (2048) NULL,
	cdd                  VARCHAR (2048) NULL,
	cwf                  VARCHAR (2048) NULL,
	ccf                  VARCHAR (2048) NULL,
	cuf                  VARCHAR (2048) NULL,
	cdf                  VARCHAR (2048) NULL,	
	CONSTRAINT PK__directories PRIMARY KEY (id),
	CONSTRAINT UQ__directories UNIQUE (dir_path)
)
;

CREATE TABLE filecontent
(
	id           INT IDENTITY NOT NULL,
	file_id      INT NOT NULL,
	file_content VARBINARY NULL,
	CONSTRAINT PK__filecontent PRIMARY KEY (id),
	CONSTRAINT UQ__filecontent UNIQUE (file_id)
)
;

CREATE TABLE file_versions
(
	versionid      BIGINT IDENTITY NOT NULL,
	file_id        INT NOT NULL,
	version_number INT NULL,
	fvc_id         INT NULL,
	file_name      VARCHAR (255) NOT NULL,
	cdate          DATETIME NULL,
	ctime          DATETIME NULL,
	cuser          VARCHAR (255) NULL,
	CONSTRAINT PK__file_versions PRIMARY KEY (versionid)
)
;

CREATE TABLE file_version_content
(
	fvcid      BIGINT IDENTITY NOT NULL,
	version_id BIGINT NULL,
	content    VARBINARY NULL,
	CONSTRAINT PK__file_version_content PRIMARY KEY (fvcid)
)
;

CREATE TABLE file_archive_tracker
(
  id BIGINT IDENTITY NOT NULL,
  fileid BIGINT NOT NULL,
  versionnumber INT NOT NULL,
  CONSTRAINT file_archive_tracker_pkey PRIMARY KEY (id),
  CONSTRAINT file_archive_tracker_fileid_versionnumber_key UNIQUE (fileid, versionnumber)
)
;

CREATE TABLE filetype
(
	id      BIGINT IDENTITY NOT NULL,
	name    VARCHAR (255) NOT NULL,
	appname VARCHAR (255) NULL,
	CONSTRAINT PK__filetype PRIMARY KEY (id),
	CONSTRAINT UQ__filetype UNIQUE (name,appname)
)
;

CREATE TABLE tags
(
	id     BIGINT IDENTITY NOT NULL,
	fileid BIGINT NOT NULL,
	tag    VARCHAR (255) NOT NULL,
	CONSTRAINT PK__filetags PRIMARY KEY (id),
	CONSTRAINT UQ__filetags UNIQUE (fileid,tag)
)
;

CREATE TABLE fileactiontype
(
	id      INT IDENTITY NOT NULL,
	atype 	INT NOT NULL,
	en      VARCHAR (64) NULL,
	fr      VARCHAR (64) NULL,
	de      VARCHAR (64) NULL,
	CONSTRAINT PK__fileactiontype PRIMARY KEY (id),
	CONSTRAINT UQ__fileactiontype UNIQUE (atype)
)
;

CREATE TABLE fileactionhistory
(
	id      BIGINT IDENTITY NOT NULL,
	file_id BIGINT NOT NULL,
	actiontype      INT NOT NULL,
	usern   VARCHAR (256) NOT NULL,
	uname   VARCHAR (1024) NULL,
	ttime	DATETIME NULL,
	ddate	DATETIME NULL,
	adesc VARCHAR (1600) NULL,
	CONSTRAINT PK__fileactionhistory PRIMARY KEY (id),
	CONSTRAINT FK_fileactionhistory_2 FOREIGN KEY (actiontype) REFERENCES fileactiontype (atype)
)
;

INSERT INTO fileactiontype (atype,en,de,fr) VALUES (1,'File created', 'Datei kreiert' , 'Fichier créé');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (2,'File content changed', 'Datei Inhalt geändert' , 'Fichier modifié');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (3,'File description changed', 'Datei Beschreibung geändert' , 'Description du Fichier modifiée');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (4,'File renamed', 'Datei umbennant' , 'Fichier renommé');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (5,'File deleted', 'Datei gelöscht' , 'Fichier supprimé');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (6,'File downloaded', 'Datei heruntergeladen' , 'Fichier téléchargé');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (7,'File printed', 'Datei gedruckt' , 'Fichier imprimé');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (8,'File opened', 'Datei geöffnet' , 'Fichier ouvert');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (9,'File copied and paste', 'Datei kopiert' , 'Fichier copié');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (10,'File moved', 'Datei verschoben' , 'Fichier déplacé');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (11,'New Version created', 'Neue Version erfasst' , 'Nouvelle version');
INSERT INTO fileactiontype (atype,en,de,fr) VALUES (12,'Last version rollbacked', 'Letzte Version gelöscht' , 'Dernière version supprimée');

CREATE TABLE fmlanguages(
	id bigint IDENTITY NOT NULL,
	isoname varchar(3) NOT NULL,
	CONSTRAINT UQ__fmlanguages UNIQUE (isoname)
)
;

CREATE TABLE dirtranslation(
	id bigint IDENTITY NOT NULL,
	translateditemid bigint NOT NULL,
	lang character varying(3) NOT NULL, 
	tr character varying(1024) NULL,
	CONSTRAINT PK__dirtranslation PRIMARY KEY (id),
	CONSTRAINT UQ__dirtranslation UNIQUE (translateditemid,lang)
)
;

CREATE TABLE fttranslation(
	id bigint IDENTITY NOT NULL,
	translateditemid bigint NOT NULL,
	lang character varying(3) NOT NULL, 
	tr character varying(1024) NULL,
	CONSTRAINT PK__fttranslation PRIMARY KEY (id),
	CONSTRAINT UQ__fttranslation UNIQUE (translateditemid,lang)
)
;

INSERT INTO fmlanguages(isoname) VALUES ('EN');
INSERT INTO fmlanguages(isoname) VALUES ('DE');
INSERT INTO fmlanguages(isoname) VALUES ('FR');
