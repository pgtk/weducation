--
-- PostgreSQL database dump
--
SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--
CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;
--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--
COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;
--
-- Name: clientsessions; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE clientsessions (
    cls_pcode integer NOT NULL,
    cls_ssid character varying(50) NOT NULL,
    cls_acocode integer NOT NULL,
    cls_lastaction timestamp without time zone DEFAULT now() NOT NULL,
    cls_ipaddr character varying(128),
    cls_created timestamp without time zone DEFAULT now() NOT NULL
);
--
-- Name: getclientsession(character varying); Type: FUNCTION; Schema: public; Owner: glassfish
--
CREATE FUNCTION getclientsession(character varying) RETURNS SETOF clientsessions
    LANGUAGE sql
    AS $_$
UPDATE clientsessions SET cls_lastaction = now() WHERE (cls_ssid = $1);
SELECT * FROM clientsessions WHERE (cls_ssid = $1);
$_$;
--
-- Name: accounts; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE accounts (
    aco_pcode integer NOT NULL,
    aco_login character varying(50) NOT NULL,
    aco_password character varying(50) NOT NULL,
    aco_role integer NOT NULL,
    aco_description character varying(255)
);
--
-- Name: accounts_aco_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE accounts_aco_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: accounts_aco_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE accounts_aco_pcode_seq OWNED BY accounts.aco_pcode;
--
-- Name: cards; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE cards (
    crd_psncode integer NOT NULL,
    crd_spccode integer NOT NULL,
    crd_extramural boolean DEFAULT false NOT NULL,
    crd_sclcode integer NOT NULL,
    crd_docname character varying(128),
    crd_docorganization character varying(255),
    crd_remanded boolean DEFAULT false NOT NULL,
    crd_remandreason character varying(128),
    crd_remandcommand character varying(128),
    crd_diplomenumber character varying(50),
    crd_appendixnumber character varying(50),
    crd_regnumber character varying(50),
    crd_comissiondirector character varying(128),
    crd_gosexam boolean NOT NULL,
    crd_diplomelength double precision,
    crd_diplometheme character varying(255),
    crd_diplomemark integer,
    crd_red boolean NOT NULL,
    crd_pcode integer NOT NULL,
    crd_bdate date NOT NULL,
    crd_edate date,
    crd_docdate date,
    crd_comissiondate date,
    crd_diplomedate date,
    crd_active boolean DEFAULT false NOT NULL,
    crd_grpcode integer,
    crd_plncode integer,
    crd_commercial boolean DEFAULT false NOT NULL
);
--
-- Name: card_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE card_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: card_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE card_pcode_seq OWNED BY cards.crd_pcode;
--
-- Name: card_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE card_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: cmarks; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE cmarks (
    cmk_subcode integer NOT NULL,
    cmk_theme character varying(255),
    cmk_crdcode integer NOT NULL,
    cmk_mark integer NOT NULL,
    cmk_pcode integer NOT NULL
);
--
-- Name: course_work_mark_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE course_work_mark_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: course_work_mark_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE course_work_mark_pcode_seq OWNED BY cmarks.cmk_pcode;
--
-- Name: course_work_mark_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE course_work_mark_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20)
);
--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);
--
-- Name: departmentprofiles; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE departmentprofiles (
    dpr_pcode integer NOT NULL,
    dpr_depcode integer NOT NULL,
    dpr_spccode integer NOT NULL,
    dpr_extramural boolean DEFAULT false NOT NULL
);
--
-- Name: departments; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE departments (
    dep_pcode integer NOT NULL,
    dep_name character varying(100) NOT NULL,
    dep_master character varying(128) NOT NULL,
    dep_secretar character varying(128) NOT NULL
);
--
-- Name: departments_dep_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE departments_dep_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: departments_dep_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE departments_dep_pcode_seq OWNED BY departments.dep_pcode;
--
-- Name: fmarks; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE fmarks (
    fmk_subcode integer NOT NULL,
    fmk_crdcode integer NOT NULL,
    fmk_mark integer NOT NULL,
    fmk_audload integer NOT NULL,
    fmk_maxload integer NOT NULL,
    fmk_pcode integer NOT NULL
);
--
-- Name: final_mark_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE final_mark_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: final_mark_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE final_mark_pcode_seq OWNED BY fmarks.fmk_pcode;
--
-- Name: final_mark_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE final_mark_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: finalpractics; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE finalpractics (
    fpc_pcode integer NOT NULL,
    fpc_name character varying(255),
    fpc_number integer DEFAULT 0 NOT NULL,
    fpc_length double precision NOT NULL,
    fpc_plncode integer NOT NULL
);
--
-- Name: finalpractics_fpc_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE finalpractics_fpc_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: finalpractics_fpc_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE finalpractics_fpc_pcode_seq OWNED BY finalpractics.fpc_pcode;
--
-- Name: gmarks; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE gmarks (
    gmk_subcode integer NOT NULL,
    gmk_crdcode integer NOT NULL,
    gmk_mark integer NOT NULL,
    gmk_pcode integer NOT NULL
);
--
-- Name: gos_mark_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE gos_mark_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: gos_mark_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE gos_mark_pcode_seq OWNED BY gmarks.gmk_pcode;
--
-- Name: gos_mark_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE gos_mark_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: gosexams; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE gosexams (
    gex_pcode integer NOT NULL,
    gex_subcode integer NOT NULL,
    gex_plncode integer NOT NULL
);
--
-- Name: gosexams_gex_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE gosexams_gex_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: gosexams_gex_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE gosexams_gex_pcode_seq OWNED BY gosexams.gex_pcode;
--
-- Name: groups; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE groups (
    grp_pcode integer NOT NULL,
    grp_name character varying(20) NOT NULL,
    grp_active boolean DEFAULT false NOT NULL,
    grp_spccode integer NOT NULL,
    grp_master character varying(150),
    grp_year integer NOT NULL,
    grp_extramural boolean DEFAULT false NOT NULL,
    grp_course integer DEFAULT 1 NOT NULL,
    grp_plncode integer NOT NULL,
    grp_commercial boolean DEFAULT false NOT NULL
);
--
-- Name: groups_grp_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE groups_grp_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: groups_grp_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE groups_grp_pcode_seq OWNED BY groups.grp_pcode;
--
-- Name: load; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE load (
    lod_pcode integer NOT NULL,
    lod_subcode integer NOT NULL,
    lod_exfcode integer,
    lod_course integer NOT NULL,
    lod_semester integer NOT NULL,
    lod_auditory integer DEFAULT 0 NOT NULL,
    lod_maximum integer DEFAULT 0 NOT NULL,
    lod_courseproj integer DEFAULT 0 NOT NULL
);
--
-- Name: load_lod_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE load_lod_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: load_lod_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE load_lod_pcode_seq OWNED BY load.lod_pcode;
--
-- Name: modules; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE modules (
    mod_pcode integer NOT NULL,
    mod_name character varying(255) NOT NULL,
    mod_plncode integer NOT NULL,
    mod_exfcode integer
);
--
-- Name: modules_mod_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE modules_mod_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: modules_mod_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE modules_mod_pcode_seq OWNED BY modules.mod_pcode;
--
-- Name: monthmarks; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE monthmarks (
    mmk_pcode integer NOT NULL,
    mmk_year integer NOT NULL,
    mmk_month integer NOT NULL,
    mmk_mark integer NOT NULL,
    mmk_subcode integer NOT NULL,
    mmk_psncode integer NOT NULL,
    mmk_crdcode integer NOT NULL
);
--
-- Name: monthmarks_mmk_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE monthmarks_mmk_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: monthmarks_mmk_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE monthmarks_mmk_pcode_seq OWNED BY monthmarks.mmk_pcode;
--
-- Name: persons; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE persons (
    psn_birthplace character varying(255),
    psn_male boolean NOT NULL,
    psn_foreign boolean NOT NULL,
    psn_pcode integer NOT NULL,
    psn_birthdate date DEFAULT ('now'::text)::date NOT NULL,
    psn_firstname character varying(50) NOT NULL,
    psn_middlename character varying(50) NOT NULL,
    psn_lastname character varying(50) NOT NULL
);
--
-- Name: person_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE person_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: person_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE person_pcode_seq OWNED BY persons.psn_pcode;
--
-- Name: person_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE person_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: plans; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE plans (
    pln_pcode integer NOT NULL,
    pln_spccode integer NOT NULL,
    pln_name character varying(255) NOT NULL,
    pln_description character varying(255),
    pln_extramural boolean DEFAULT false NOT NULL,
    pln_years integer DEFAULT 0 NOT NULL,
    pln_months integer DEFAULT 0 NOT NULL,
    pln_date date
);
--
-- Name: plans_pln_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE plans_pln_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: plans_pln_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE plans_pln_pcode_seq OWNED BY plans.pln_pcode;
--
-- Name: pmarks; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE pmarks (
    pmk_prccode integer,
    pmk_crdcode integer,
    pmk_mark integer,
    pmk_length double precision,
    pmk_pcode integer NOT NULL
);
--
-- Name: practic_mark_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE practic_mark_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: practic_mark_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE practic_mark_pcode_seq OWNED BY pmarks.pmk_pcode;
--
-- Name: practic_mark_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE practic_mark_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: practics; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE practics (
    prc_name character varying(255) NOT NULL,
    prc_pcode integer NOT NULL,
    prc_course integer DEFAULT 1 NOT NULL,
    prc_semester integer DEFAULT 1 NOT NULL,
    prc_length real DEFAULT 0 NOT NULL,
    prc_modcode integer,
    prc_plncode integer NOT NULL
);
--
-- Name: practic_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE practic_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: practic_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE practic_pcode_seq OWNED BY practics.prc_pcode;
--
-- Name: practic_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE practic_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: profiles_prf_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE profiles_prf_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: profiles_prf_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE profiles_prf_pcode_seq OWNED BY departmentprofiles.dpr_pcode;
--
-- Name: renamings; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE renamings (
    ren_oldname character varying(255) NOT NULL,
    ren_newname character varying(255) NOT NULL,
    ren_pcode integer NOT NULL,
    ren_date date DEFAULT ('now'::text)::date NOT NULL
);
--
-- Name: renaming_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE renaming_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: renaming_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE renaming_pcode_seq OWNED BY renamings.ren_pcode;
--
-- Name: renaming_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE renaming_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: schools; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE schools (
    scl_fullname character varying(255) NOT NULL,
    scl_shortname character varying(255) NOT NULL,
    scl_pcode integer NOT NULL,
    scl_place character varying(128) NOT NULL,
    scl_director character varying(128) NOT NULL,
    scl_current boolean DEFAULT false NOT NULL
);
--
-- Name: school_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE school_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: school_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE school_pcode_seq OWNED BY schools.scl_pcode;
--
-- Name: school_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE school_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: semestermarks; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE semestermarks (
    smk_pcode integer NOT NULL,
    smk_course integer NOT NULL,
    smk_semester integer NOT NULL,
    smk_mark integer NOT NULL,
    smk_psncode integer NOT NULL,
    smk_crdcode integer NOT NULL,
    smk_subcode integer NOT NULL,
    smk_exam boolean NOT NULL
);
--
-- Name: semestermarks_smk_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE semestermarks_smk_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: semestermarks_smk_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE semestermarks_smk_pcode_seq OWNED BY semestermarks.smk_pcode;
--
-- Name: sessions_ses_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE sessions_ses_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: sessions_ses_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE sessions_ses_pcode_seq OWNED BY clientsessions.cls_pcode;
--
-- Name: specialities; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE specialities (
    spc_fullname character varying(255) NOT NULL,
    spc_pcode integer NOT NULL,
    spc_key character varying(20) NOT NULL,
    spc_shortname character varying(10) NOT NULL,
    spc_specialization character varying(128) NOT NULL,
    spc_kvalification character varying(128) NOT NULL,
    spc_actual boolean DEFAULT false NOT NULL
);
--
-- Name: speciality_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE speciality_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: speciality_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE speciality_pcode_seq OWNED BY specialities.spc_pcode;
--
-- Name: speciality_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE speciality_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: subjects; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE subjects (
    sub_fullname character varying(255) NOT NULL,
    sub_pcode integer NOT NULL,
    sub_shortname character varying(30),
    sub_modcode integer,
    sub_plncode integer NOT NULL
);
--
-- Name: subject_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE subject_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: subject_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE subject_pcode_seq OWNED BY subjects.sub_pcode;
--
-- Name: subject_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE subject_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: test_sequence; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE test_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: weekmissings; Type: TABLE; Schema: public; Owner: glassfish; Tablespace: 
--
CREATE TABLE weekmissings (
    wms_pcode integer NOT NULL,
    wms_year integer NOT NULL,
    wms_month integer NOT NULL,
    wms_week integer NOT NULL,
    wms_legal integer NOT NULL,
    wms_illegal integer NOT NULL,
    wms_psncode integer NOT NULL,
    wms_crdcode integer NOT NULL
);
--
-- Name: weekmissings_wms_pcode_seq; Type: SEQUENCE; Schema: public; Owner: glassfish
--
CREATE SEQUENCE weekmissings_wms_pcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
--
-- Name: weekmissings_wms_pcode_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: glassfish
--
ALTER SEQUENCE weekmissings_wms_pcode_seq OWNED BY weekmissings.wms_pcode;
--
-- Name: aco_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY accounts ALTER COLUMN aco_pcode SET DEFAULT nextval('accounts_aco_pcode_seq'::regclass);
--
-- Name: crd_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cards ALTER COLUMN crd_pcode SET DEFAULT nextval('card_pcode_seq'::regclass);
--
-- Name: cls_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY clientsessions ALTER COLUMN cls_pcode SET DEFAULT nextval('sessions_ses_pcode_seq'::regclass);
--
-- Name: cmk_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cmarks ALTER COLUMN cmk_pcode SET DEFAULT nextval('course_work_mark_pcode_seq'::regclass);
--
-- Name: dpr_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY departmentprofiles ALTER COLUMN dpr_pcode SET DEFAULT nextval('profiles_prf_pcode_seq'::regclass);
--
-- Name: dep_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY departments ALTER COLUMN dep_pcode SET DEFAULT nextval('departments_dep_pcode_seq'::regclass);
--
-- Name: fpc_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY finalpractics ALTER COLUMN fpc_pcode SET DEFAULT nextval('finalpractics_fpc_pcode_seq'::regclass);
--
-- Name: fmk_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY fmarks ALTER COLUMN fmk_pcode SET DEFAULT nextval('final_mark_pcode_seq'::regclass);
--
-- Name: gmk_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY gmarks ALTER COLUMN gmk_pcode SET DEFAULT nextval('gos_mark_pcode_seq'::regclass);
--
-- Name: gex_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY gosexams ALTER COLUMN gex_pcode SET DEFAULT nextval('gosexams_gex_pcode_seq'::regclass);
--
-- Name: grp_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY groups ALTER COLUMN grp_pcode SET DEFAULT nextval('groups_grp_pcode_seq'::regclass);
--
-- Name: lod_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY load ALTER COLUMN lod_pcode SET DEFAULT nextval('load_lod_pcode_seq'::regclass);
--
-- Name: mod_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY modules ALTER COLUMN mod_pcode SET DEFAULT nextval('modules_mod_pcode_seq'::regclass);
--
-- Name: mmk_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY monthmarks ALTER COLUMN mmk_pcode SET DEFAULT nextval('monthmarks_mmk_pcode_seq'::regclass);
--
-- Name: psn_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY persons ALTER COLUMN psn_pcode SET DEFAULT nextval('person_pcode_seq'::regclass);
--
-- Name: pln_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY plans ALTER COLUMN pln_pcode SET DEFAULT nextval('plans_pln_pcode_seq'::regclass);
--
-- Name: pmk_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY pmarks ALTER COLUMN pmk_pcode SET DEFAULT nextval('practic_mark_pcode_seq'::regclass);
--
-- Name: prc_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY practics ALTER COLUMN prc_pcode SET DEFAULT nextval('practic_pcode_seq'::regclass);
--
-- Name: ren_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY renamings ALTER COLUMN ren_pcode SET DEFAULT nextval('renaming_pcode_seq'::regclass);
--
-- Name: scl_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY schools ALTER COLUMN scl_pcode SET DEFAULT nextval('school_pcode_seq'::regclass);
--
-- Name: smk_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY semestermarks ALTER COLUMN smk_pcode SET DEFAULT nextval('semestermarks_smk_pcode_seq'::regclass);
--
-- Name: spc_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY specialities ALTER COLUMN spc_pcode SET DEFAULT nextval('speciality_pcode_seq'::regclass);
--
-- Name: sub_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY subjects ALTER COLUMN sub_pcode SET DEFAULT nextval('subject_pcode_seq'::regclass);
--
-- Name: wms_pcode; Type: DEFAULT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY weekmissings ALTER COLUMN wms_pcode SET DEFAULT nextval('weekmissings_wms_pcode_seq'::regclass);
--
-- Name: accounts_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY accounts
    ADD CONSTRAINT accounts_pk PRIMARY KEY (aco_pcode);
--
-- Name: cards_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY cards
    ADD CONSTRAINT cards_pk PRIMARY KEY (crd_pcode);
--
-- Name: cmarks_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY cmarks
    ADD CONSTRAINT cmarks_pk PRIMARY KEY (cmk_pcode);
--
-- Name: departmentprofiles_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY departmentprofiles
    ADD CONSTRAINT departmentprofiles_pk PRIMARY KEY (dpr_pcode);
--
-- Name: departments_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY departments
    ADD CONSTRAINT departments_pk PRIMARY KEY (dep_pcode);
--
-- Name: finalpractics_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY finalpractics
    ADD CONSTRAINT finalpractics_pk PRIMARY KEY (fpc_pcode);
--
-- Name: fmarks_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY fmarks
    ADD CONSTRAINT fmarks_pk PRIMARY KEY (fmk_pcode);
--
-- Name: gmarks_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY gmarks
    ADD CONSTRAINT gmarks_pk PRIMARY KEY (gmk_pcode);
--
-- Name: gosexams_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY gosexams
    ADD CONSTRAINT gosexams_pk PRIMARY KEY (gex_pcode);
--
-- Name: groups_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_pk PRIMARY KEY (grp_pcode);
--
-- Name: load_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY load
    ADD CONSTRAINT load_pk PRIMARY KEY (lod_pcode);
--
-- Name: modules_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY modules
    ADD CONSTRAINT modules_pk PRIMARY KEY (mod_pcode);
--
-- Name: monthmarks_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY monthmarks
    ADD CONSTRAINT monthmarks_pk PRIMARY KEY (mmk_pcode);
--
-- Name: persons_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY persons
    ADD CONSTRAINT persons_pk PRIMARY KEY (psn_pcode);
--
-- Name: pk_databasechangeloglock; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY databasechangeloglock
    ADD CONSTRAINT pk_databasechangeloglock PRIMARY KEY (id);
--
-- Name: plans_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY plans
    ADD CONSTRAINT plans_pk PRIMARY KEY (pln_pcode);
--
-- Name: pmarks_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY pmarks
    ADD CONSTRAINT pmarks_pk PRIMARY KEY (pmk_pcode);
--
-- Name: practics_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY practics
    ADD CONSTRAINT practics_pk PRIMARY KEY (prc_pcode);
--
-- Name: renamings_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY renamings
    ADD CONSTRAINT renamings_pk PRIMARY KEY (ren_pcode);
--
-- Name: schools_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY schools
    ADD CONSTRAINT schools_pk PRIMARY KEY (scl_pcode);
--
-- Name: semestermarks_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY semestermarks
    ADD CONSTRAINT semestermarks_pk PRIMARY KEY (smk_pcode);
--
-- Name: sessions_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY clientsessions
    ADD CONSTRAINT sessions_pk PRIMARY KEY (cls_pcode);
--
-- Name: specialities_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY specialities
    ADD CONSTRAINT specialities_pk PRIMARY KEY (spc_pcode);
--
-- Name: subjects_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY subjects
    ADD CONSTRAINT subjects_pk PRIMARY KEY (sub_pcode);
--
-- Name: weekmissings_pk; Type: CONSTRAINT; Schema: public; Owner: glassfish; Tablespace: 
--
ALTER TABLE ONLY weekmissings
    ADD CONSTRAINT weekmissings_pk PRIMARY KEY (wms_pcode);
--
-- Name: cards_persons_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cards
    ADD CONSTRAINT cards_persons_fk FOREIGN KEY (crd_psncode) REFERENCES persons(psn_pcode) ON UPDATE CASCADE;
--
-- Name: cards_schools_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cards
    ADD CONSTRAINT cards_schools_fk FOREIGN KEY (crd_sclcode) REFERENCES schools(scl_pcode) ON UPDATE CASCADE;
--
-- Name: cards_specialities_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cards
    ADD CONSTRAINT cards_specialities_fk FOREIGN KEY (crd_spccode) REFERENCES specialities(spc_pcode) ON UPDATE CASCADE;
--
-- Name: cmarks_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cmarks
    ADD CONSTRAINT cmarks_cards_fk FOREIGN KEY (cmk_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: cmarks_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY cmarks
    ADD CONSTRAINT cmarks_subjects_fk FOREIGN KEY (cmk_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: departmentprofiles_departments_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY departmentprofiles
    ADD CONSTRAINT departmentprofiles_departments_fk FOREIGN KEY (dpr_depcode) REFERENCES departments(dep_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: departmentprofiles_specialities_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY departmentprofiles
    ADD CONSTRAINT departmentprofiles_specialities_fk FOREIGN KEY (dpr_spccode) REFERENCES specialities(spc_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: finalpractics_plans_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY finalpractics
    ADD CONSTRAINT finalpractics_plans_fk FOREIGN KEY (fpc_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: fmarks_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY fmarks
    ADD CONSTRAINT fmarks_cards_fk FOREIGN KEY (fmk_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: fmarks_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY fmarks
    ADD CONSTRAINT fmarks_subjects_fk FOREIGN KEY (fmk_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: gmarks_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY gmarks
    ADD CONSTRAINT gmarks_cards_fk FOREIGN KEY (gmk_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: gmarks_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY gmarks
    ADD CONSTRAINT gmarks_subjects_fk FOREIGN KEY (gmk_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: gosexams_plans_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY gosexams
    ADD CONSTRAINT gosexams_plans_fk FOREIGN KEY (gex_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: gosexams_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY gosexams
    ADD CONSTRAINT gosexams_subjects_fk FOREIGN KEY (gex_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: groups_specialities_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY groups
    ADD CONSTRAINT groups_specialities_fk FOREIGN KEY (grp_spccode) REFERENCES specialities(spc_pcode) ON UPDATE CASCADE;
--
-- Name: load_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY load
    ADD CONSTRAINT load_subjects_fk FOREIGN KEY (lod_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: modules_plans_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY modules
    ADD CONSTRAINT modules_plans_fk FOREIGN KEY (mod_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: monthmarks_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY monthmarks
    ADD CONSTRAINT monthmarks_cards_fk FOREIGN KEY (mmk_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: monthmarks_persons_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY monthmarks
    ADD CONSTRAINT monthmarks_persons_fk FOREIGN KEY (mmk_psncode) REFERENCES persons(psn_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: monthmarks_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY monthmarks
    ADD CONSTRAINT monthmarks_subjects_fk FOREIGN KEY (mmk_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: plans_specialities_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY plans
    ADD CONSTRAINT plans_specialities_fk FOREIGN KEY (pln_spccode) REFERENCES specialities(spc_pcode) ON UPDATE CASCADE;
--
-- Name: pmarks_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY pmarks
    ADD CONSTRAINT pmarks_cards_fk FOREIGN KEY (pmk_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: pmarks_practics_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY pmarks
    ADD CONSTRAINT pmarks_practics_fk FOREIGN KEY (pmk_prccode) REFERENCES practics(prc_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: practics_modules_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY practics
    ADD CONSTRAINT practics_modules_fk FOREIGN KEY (prc_modcode) REFERENCES modules(mod_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: practics_plans_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY practics
    ADD CONSTRAINT practics_plans_fk FOREIGN KEY (prc_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: semestermarks_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY semestermarks
    ADD CONSTRAINT semestermarks_cards_fk FOREIGN KEY (smk_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: semestermarks_persons_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY semestermarks
    ADD CONSTRAINT semestermarks_persons_fk FOREIGN KEY (smk_psncode) REFERENCES persons(psn_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: semestermarks_subjects_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY semestermarks
    ADD CONSTRAINT semestermarks_subjects_fk FOREIGN KEY (smk_subcode) REFERENCES subjects(sub_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: sessions_accounts_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY clientsessions
    ADD CONSTRAINT sessions_accounts_fk FOREIGN KEY (cls_acocode) REFERENCES accounts(aco_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: subjects_modules_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY subjects
    ADD CONSTRAINT subjects_modules_fk FOREIGN KEY (sub_modcode) REFERENCES modules(mod_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: subjects_plans_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY subjects
    ADD CONSTRAINT subjects_plans_fk FOREIGN KEY (sub_plncode) REFERENCES plans(pln_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: weekmissings_cards_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY weekmissings
    ADD CONSTRAINT weekmissings_cards_fk FOREIGN KEY (wms_crdcode) REFERENCES cards(crd_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: weekmissings_persons_fk; Type: FK CONSTRAINT; Schema: public; Owner: glassfish
--
ALTER TABLE ONLY weekmissings
    ADD CONSTRAINT weekmissings_persons_fk FOREIGN KEY (wms_psncode) REFERENCES persons(psn_pcode) ON UPDATE CASCADE ON DELETE CASCADE;
--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--
REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;
--
-- PostgreSQL database dump complete
--
