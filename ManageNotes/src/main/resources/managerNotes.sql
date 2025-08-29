--
-- PostgreSQL database dump
--

-- Dumped from database version 17.5 (Ubuntu 17.5-1.pgdg24.04+1)
-- Dumped by pg_dump version 17.5 (Ubuntu 17.5-1.pgdg24.04+1)

-- Started on 2025-08-29 18:31:57 WAT

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 243 (class 1255 OID 33830)
-- Name: ensure_grade_integrity(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.ensure_grade_integrity() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Ensure semester is set (default to semester 1 if null)
    IF NEW.id_semester IS NULL THEN
        NEW.id_semester := 1;
    END IF;
    
    -- Ensure student exists
    IF NOT EXISTS (SELECT 1 FROM students WHERE id = NEW.id_students) THEN
        RAISE EXCEPTION 'Student with ID % does not exist', NEW.id_students;
    END IF;
    
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.ensure_grade_integrity() OWNER TO postgres;

--
-- TOC entry 242 (class 1255 OID 33809)
-- Name: validate_student_user_consistency(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.validate_student_user_consistency() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- For grades table, ensure id_students references a valid student record
    IF TG_TABLE_NAME = 'grades' THEN
        IF NOT EXISTS (
            SELECT 1 FROM students WHERE id = NEW.id_students
        ) THEN
            RAISE EXCEPTION 'Invalid student ID: % does not exist in students table', NEW.id_students;
        END IF;
    END IF;
    
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.validate_student_user_consistency() OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 17326)
-- Name: departments_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.departments_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.departments_seq OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 219 (class 1259 OID 17226)
-- Name: departments; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.departments (
    id bigint DEFAULT nextval('public.departments_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    name character varying(100) NOT NULL
);


ALTER TABLE public.departments OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 17327)
-- Name: grade_claims_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grade_claims_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grade_claims_seq OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 17231)
-- Name: grade_claims; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grade_claims (
    id bigint DEFAULT nextval('public.grade_claims_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    rejection_reason character varying(255),
    resolved_at timestamp(6) without time zone,
    status character varying(255),
    cause text,
    description text,
    period_label character varying(255),
    requested_score double precision NOT NULL,
    teacher_comment text,
    created_by_id bigint,
    grade_id bigint NOT NULL,
    semester_id bigint,
    student_id bigint NOT NULL,
    CONSTRAINT grade_claims_status_check CHECK (((status)::text = ANY ((ARRAY['PENDING'::character varying, 'APPROVED'::character varying, 'REJECTED'::character varying])::text[])))
);


ALTER TABLE public.grade_claims OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 25598)
-- Name: grade_report; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grade_report (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    gpa double precision,
    id_semesters bigint,
    id_students bigint,
    pdf_path character varying(255),
    status character varying(255)
);


ALTER TABLE public.grade_report OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 17328)
-- Name: grade_report_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grade_report_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grade_report_seq OWNER TO postgres;

--
-- TOC entry 230 (class 1259 OID 17329)
-- Name: grades_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grades_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grades_seq OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 17246)
-- Name: grades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grades (
    id bigint DEFAULT nextval('public.grades_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    comments character varying(255),
    max_value double precision,
    period_type character varying(255),
    grade_type character varying(255),
    value double precision,
    id_users bigint,
    id_semester bigint,
    id_students bigint,
    id_subject bigint
);


ALTER TABLE public.grades OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 17254)
-- Name: grading_window; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.grading_window (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    color character varying(255),
    end_date date,
    is_active boolean,
    name character varying(255),
    order_index integer,
    short_name character varying(255),
    start_date date,
    period_label character varying(255),
    id_semester bigint,
    period_type smallint,
    CONSTRAINT grading_window_period_type_check CHECK (((period_type >= 0) AND (period_type <= 3)))
);


ALTER TABLE public.grading_window OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 17330)
-- Name: grading_window_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.grading_window_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.grading_window_seq OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 25605)
-- Name: report_record; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.report_record (
    id bigint NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    academic_year character varying(255),
    class_id bigint,
    download_url character varying(255),
    faculty character varying(255),
    generated_by bigint,
    gpa double precision,
    pdf_path character varying(255),
    report_type character varying(50),
    semester_id bigint,
    status character varying(255),
    student_id bigint,
    subject_id bigint,
    university_name character varying(255)
);


ALTER TABLE public.report_record OWNER TO postgres;

--
-- TOC entry 232 (class 1259 OID 17331)
-- Name: report_record_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.report_record_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.report_record_seq OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 17332)
-- Name: semesters_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.semesters_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.semesters_seq OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 17279)
-- Name: semesters; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.semesters (
    id bigint DEFAULT nextval('public.semesters_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    active boolean,
    end_date date,
    name character varying(255),
    order_index integer,
    start_date date
);


ALTER TABLE public.semesters OWNER TO postgres;

--
-- TOC entry 234 (class 1259 OID 17333)
-- Name: student_info_requests_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.student_info_requests_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.student_info_requests_seq OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 17334)
-- Name: students_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.students_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.students_seq OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 17292)
-- Name: students; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.students (
    id bigint DEFAULT nextval('public.students_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    cycle character varying(255),
    date_of_birth date,
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    level character varying(255),
    matricule character varying(255),
    place_of_birth character varying(255),
    speciality character varying(255),
    CONSTRAINT students_cycle_check CHECK (((cycle)::text = ANY ((ARRAY['BACHELOR'::character varying, 'MASTER'::character varying, 'PHD'::character varying])::text[]))),
    CONSTRAINT students_level_check CHECK (((level)::text = ANY ((ARRAY['LEVEL1'::character varying, 'LEVEL2'::character varying, 'LEVEL3'::character varying, 'LEVEL4'::character varying, 'LEVEL5'::character varying])::text[])))
);


ALTER TABLE public.students OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 17335)
-- Name: subject_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.subject_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.subject_seq OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 17301)
-- Name: subject; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.subject (
    id bigint DEFAULT nextval('public.subject_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    active boolean,
    code character varying(255),
    credits numeric(38,2),
    cycle character varying(255),
    description character varying(500),
    id_teacher bigint,
    level character varying(255),
    name character varying(255),
    department_id bigint,
    id_semester bigint,
    CONSTRAINT subject_cycle_check CHECK (((cycle)::text = ANY ((ARRAY['BACHELOR'::character varying, 'MASTER'::character varying, 'PHD'::character varying])::text[]))),
    CONSTRAINT subject_level_check CHECK (((level)::text = ANY ((ARRAY['LEVEL1'::character varying, 'LEVEL2'::character varying, 'LEVEL3'::character varying, 'LEVEL4'::character varying, 'LEVEL5'::character varying])::text[])))
);


ALTER TABLE public.subject OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 17398)
-- Name: user_levels; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_levels (
    user_id bigint NOT NULL,
    level character varying(255)
);


ALTER TABLE public.user_levels OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 17336)
-- Name: users_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_seq OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 17310)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id bigint DEFAULT nextval('public.users_seq'::regclass) NOT NULL,
    creation_date timestamp(6) with time zone NOT NULL,
    last_modified_date timestamp(6) with time zone,
    active boolean,
    department character varying(100),
    email character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    must_change_password boolean,
    password character varying(255),
    phone character varying(30),
    role character varying(255),
    username character varying(255),
    CONSTRAINT users_role_check CHECK (((role)::text = ANY ((ARRAY['ADMIN'::character varying, 'STUDENT'::character varying, 'TEACHER'::character varying])::text[])))
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 33804)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    START WITH 62
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 3548 (class 0 OID 17226)
-- Dependencies: 219
-- Data for Name: departments; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.departments (id, creation_date, last_modified_date, name) FROM stdin;
1	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Computer Science
2	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Mathematics
3	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Physics
4	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Engineering
5	2025-08-25 14:15:05.477309+01	2025-08-25 14:15:05.477309+01	Business Administration
56	2025-08-28 18:53:57.22909+01	2025-08-28 18:53:57.22909+01	Robotique
106	2025-08-28 18:55:16.745879+01	2025-08-28 18:55:16.745879+01	sp
156	2025-08-28 18:55:34.167388+01	2025-08-28 18:55:34.167388+01	de
206	2025-08-28 18:56:04.509106+01	2025-08-28 18:56:04.509106+01	tr
\.


--
-- TOC entry 3549 (class 0 OID 17231)
-- Dependencies: 220
-- Data for Name: grade_claims; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grade_claims (id, creation_date, last_modified_date, rejection_reason, resolved_at, status, cause, description, period_label, requested_score, teacher_comment, created_by_id, grade_id, semester_id, student_id) FROM stdin;
1	2025-08-26 11:07:07.1319+01	2025-08-26 11:07:07.1319+01	\N	\N	PENDING	Calculation Error	I believe there was an error in the calculation of my grade	CC #1	12	\N	13	3	1	2
2	2025-08-26 11:07:07.1319+01	2025-08-26 11:07:07.1319+01	\N	\N	PENDING	Missing Points	Some of my answers were not graded properly	CC #2	10	\N	16	5	1	5
3	2025-08-24 11:07:07.1319+01	2025-08-26 11:07:07.1319+01	\N	\N	APPROVED	Bonus Points	I completed the bonus question which was not counted	CC #1	17	Reviewed and approved. Bonus points added.	12	1	1	1
4	2025-08-25 11:07:07.1319+01	2025-08-26 11:07:07.1319+01	\N	\N	REJECTED	Partial Credit	I should receive partial credit for my approach	CC #2	11	Grade is correct as per marking scheme. No partial credit applicable.	13	4	1	2
5	2025-08-29 13:48:01.051435+01	2025-08-29 13:53:15.377622+01	\N	2025-08-29 13:53:15.331459	APPROVED	I ipassed in all the questions	I got a proof of my result	CC_1	30	\N	20	1	1	1
55	2025-08-29 15:30:54.455146+01	2025-08-29 15:30:54.455146+01	\N	\N	PENDING	Error in counting	I passed everything and i ihave the proofs	CC_1	30	\N	20	14	1	9
105	2025-08-29 15:40:40.673508+01	2025-08-29 15:40:40.673508+01	\N	\N	PENDING	Error in counting	Please i do have proofs	CC_1	30	\N	22	515	1	11
\.


--
-- TOC entry 3568 (class 0 OID 25598)
-- Dependencies: 239
-- Data for Name: grade_report; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grade_report (id, creation_date, last_modified_date, gpa, id_semesters, id_students, pdf_path, status) FROM stdin;
1	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	16.5	1	12	/reports/STU2024001_S1_2024.pdf	PASSED
2	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	17.2	1	15	/reports/STU2024004_S1_2024.pdf	PASSED
3	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	11.8	1	14	/reports/STU2024003_S1_2024.pdf	PASSED
4	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	13.5	1	27	/reports/STU2023001_S1_2024.pdf	PASSED
5	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	8.2	1	13	/reports/STU2024002_S1_2024.pdf	FAILED
6	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	5.8	1	16	/reports/STU2024005_S1_2024.pdf	FAILED
7	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	15.8	1	42	/reports/STU2022001_S1_2024.pdf	PASSED
8	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	14.2	1	43	/reports/STU2022002_S1_2024.pdf	PASSED
9	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	16.2	1	52	/reports/STU2021001_S1_2024.pdf	PASSED
10	2025-08-26 10:58:29.666612+01	2025-08-26 10:58:29.666612+01	14.8	1	53	/reports/STU2021002_S1_2024.pdf	PASSED
\.


--
-- TOC entry 3550 (class 0 OID 17246)
-- Dependencies: 221
-- Data for Name: grades; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grades (id, creation_date, last_modified_date, comments, max_value, period_type, grade_type, value, id_users, id_semester, id_students, id_subject) FROM stdin;
164	2025-08-27 12:57:59.713231+01	2025-08-27 12:57:59.713231+01	Average work	20	CC_1	CC_1	13.5	4	1	6	3
3	2025-08-26 10:18:59.769387+01	2025-08-26 10:18:59.769387+01	Needs improvement	20	CC_1	CC_1	8.5	2	1	2	1
5	2025-08-26 10:18:59.769387+01	2025-08-26 10:18:59.769387+01	Average work	20	CC_1	CC_1	12	4	1	3	3
2	2025-08-26 10:18:59.769387+01	2025-08-26 10:18:59.769387+01	Good understanding	20	CC_2	CC_1	15	2	1	1	1
4	2025-08-26 10:18:59.769387+01	2025-08-26 10:18:59.769387+01	Slight progress	20	CC_2	CC_1	9	2	1	2	1
6	2025-08-26 16:56:18.342055+01	2025-08-26 16:56:18.342055+01	Good work	20	CC_1	CC_1	14.5	3	1	1	2
7	2025-08-26 16:56:18.342055+01	2025-08-26 16:56:18.342055+01	Needs improvement	20	CC_1	CC_1	11	3	1	2	2
10	2025-08-26 17:18:47.197828+01	2025-08-26 17:18:47.197828+01	Good	20	CC_1	CC_1	13	2	1	11	1
11	2025-08-26 17:18:47.197828+01	2025-08-26 17:18:47.197828+01	Very good	20	CC_1	CC_1	15.5	2	1	10	1
13	2025-08-26 17:18:47.197828+01	2025-08-26 17:18:47.197828+01	Good work	20	CC_1	CC_1	14	3	1	25	15
14	2025-08-27 08:31:32.207163+01	2025-08-27 08:31:32.207163+01	Good work	20	CC_1	CC_1	15	2	1	9	1
18	2025-08-27 08:34:11.49584+01	2025-08-27 08:34:11.49584+01	Average	20	CC_1	CC_1	13	3	1	1	2
26	2025-08-27 08:34:11.49584+01	2025-08-27 08:34:11.49584+01	Satisfactory	20	CC_1	CC_1	12.5	4	1	4	3
30	2025-08-27 08:34:11.49584+01	2025-08-27 08:34:11.49584+01	Good understanding	20	CC_1	CC_1	14	6	1	5	12
39	2025-08-27 08:34:53.72684+01	2025-08-27 08:34:53.72684+01	Good progress	20	CC_1	CC_1	13.5	3	1	11	2
43	2025-08-27 08:34:53.72684+01	2025-08-27 08:34:53.72684+01	Average work	20	CC_1	CC_1	13	4	1	12	3
17	2025-08-27 08:34:11.49584+01	2025-08-27 17:24:45.396158+01	Note CC S1 mise à jour	20	CC_1	CC_1	16.5	2	1	1	11
36	2025-08-27 08:34:53.72684+01	2025-08-27 17:24:45.396064+01	Note CC S1 mise à jour	20	CC_1	CC_1	15	2	1	8	11
265	2025-08-28 12:08:51.231284+01	2025-08-28 12:09:43.100789+01	Note CC S1 mise à jour	30	CC_1	CC_1	20	2	1	13	11
365	2025-08-28 12:27:22.272509+01	2025-08-28 19:23:07.889815+01	Note CC S1 mise à jour	30	CC_1	CC_1	14	2	1	2	11
1	2025-08-26 10:18:59.769387+01	2025-08-29 13:53:15.382584+01	Excellent work	20	CC_1	CC_1	30	2	1	1	1
515	2025-08-29 15:37:37.224925+01	2025-08-29 17:41:27.726212+01	Note CC S1 mise à jour	30	CC_1	CC_1	5	2	1	11	11
465	2025-08-29 15:37:37.125725+01	2025-08-29 17:41:27.808353+01	Note CC S1 mise à jour	30	CC_1	CC_1	5	2	1	9	11
62	2025-08-27 12:29:04.660874+01	2025-08-27 12:29:04.660874+01	Good work	20	CC_1	CC_1	14	2	1	6	1
47	2025-08-27 08:45:59.025952+01	2025-08-27 08:45:59.025952+01	Excellent	20	CC_1	CC_1	16.5	3	1	26	15
50	2025-08-27 08:45:59.025952+01	2025-08-27 08:45:59.025952+01	Satisfactory	20	CC_1	CC_1	12.5	3	1	14	2
53	2025-08-27 12:07:27.844556+01	2025-08-27 12:07:27.844556+01	Good work	20	CC_1	CC_1	14.5	4	1	1	3
54	2025-08-27 12:07:27.844556+01	2025-08-27 12:07:27.844556+01	Very good	20	CC_1	CC_1	15	6	1	1	12
55	2025-08-27 12:07:59.203762+01	2025-08-27 12:07:59.203762+01	Good work	20	CC_1	CC_1	14	4	1	2	3
56	2025-08-27 12:07:59.203762+01	2025-08-27 12:07:59.203762+01	Very good	20	CC_1	CC_1	15.5	6	1	3	12
57	2025-08-27 12:07:59.203762+01	2025-08-27 12:07:59.203762+01	Average	20	CC_1	CC_1	13	4	1	4	3
58	2025-08-27 12:07:59.203762+01	2025-08-27 12:07:59.203762+01	Excellent	20	CC_1	CC_1	16	3	1	5	2
60	2025-08-27 12:07:59.203762+01	2025-08-27 12:07:59.203762+01	Very good	20	CC_1	CC_1	15	2	1	21	4
61	2025-08-27 12:07:59.203762+01	2025-08-27 12:07:59.203762+01	Average	20	CC_1	CC_1	13.5	3	1	26	5
51	2025-08-27 08:45:59.025952+01	2025-08-27 17:24:45.3964+01	Note CC S1 mise à jour	20	CC_1	CC_1	17	2	1	15	11
63	2025-08-27 12:29:04.660874+01	2025-08-27 17:24:45.396049+01	Note CC S1 mise à jour	20	CC_1	CC_1	15.5	2	1	6	11
315	2025-08-28 12:09:43.078132+01	2025-08-28 12:09:43.078132+01	Note CC S1 ajoutée	30	CC_1	CC_1	30	2	1	13	11
415	2025-08-28 19:17:29.311098+01	2025-08-28 19:22:31.575068+01	Note CC S1 mise à jour	30	CC_1	CC_1	8	2	1	10	11
\.


--
-- TOC entry 3551 (class 0 OID 17254)
-- Dependencies: 222
-- Data for Name: grading_window; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.grading_window (id, creation_date, last_modified_date, color, end_date, is_active, name, order_index, short_name, start_date, period_label, id_semester, period_type) FROM stdin;
2	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#45B7D1	2026-02-15	f	Session Normale 1	2	SN_1	2026-01-15	SN_1	1	2
3	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#4ECDC4	2026-04-22	f	Continuous Assessment 2	3	CC_2	2026-03-22	CC_2	2	1
4	2025-08-25 14:15:05.491655+01	2025-08-25 14:15:05.491655+01	#96CEB4	2026-06-01	f	Session Normale 2	4	SN_2	2026-05-15	SN_2	2	3
1	2025-08-25 14:15:05.491655+01	2025-08-29 17:45:35.243337+01	#FF6B6B	2025-11-13	t	Continuous Assessment 1	1	CC_1	2025-08-05	CC_1	1	0
\.


--
-- TOC entry 3569 (class 0 OID 25605)
-- Dependencies: 240
-- Data for Name: report_record; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.report_record (id, creation_date, last_modified_date, academic_year, class_id, download_url, faculty, generated_by, gpa, pdf_path, report_type, semester_id, status, student_id, subject_id, university_name) FROM stdin;
1	2025-08-26 10:58:29.680502+01	2025-08-26 10:58:29.680502+01	2024-2025	\N	/api/reports/download/1	Faculty of Science	1	16.5	/transcripts/STU2024001_transcript.pdf	SEMESTER_TRANSCRIPT	1	COMPLETED	12	1	University of Excellence
2	2025-08-26 10:58:29.680502+01	2025-08-26 10:58:29.680502+01	2024-2025	\N	/api/reports/download/2	Faculty of Science	1	8.2	/transcripts/STU2024002_transcript.pdf	SEMESTER_TRANSCRIPT	1	COMPLETED	13	1	University of Excellence
3	2025-08-26 10:58:29.680502+01	2025-08-26 10:58:29.680502+01	2024-2025	\N	/api/reports/download/3	Faculty of Science	1	17.2	/transcripts/STU2024004_transcript.pdf	SEMESTER_TRANSCRIPT	1	COMPLETED	15	1	University of Excellence
4	2025-08-26 10:58:29.680502+01	2025-08-26 10:58:29.680502+01	2024-2025	\N	/api/reports/download/4	Faculty of Engineering	1	15.8	/transcripts/STU2022001_annual.pdf	ANNUAL_TRANSCRIPT	1	COMPLETED	42	9	University of Excellence
5	2025-08-26 10:58:29.680502+01	2025-08-26 10:58:29.680502+01	2024-2025	\N	/api/reports/download/5	Faculty of Computer Science	1	16.2	/transcripts/STU2021001_annual.pdf	ANNUAL_TRANSCRIPT	1	COMPLETED	52	13	University of Excellence
\.


--
-- TOC entry 3552 (class 0 OID 17279)
-- Dependencies: 223
-- Data for Name: semesters; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.semesters (id, creation_date, last_modified_date, active, end_date, name, order_index, start_date) FROM stdin;
1	2025-08-25 14:15:05.490253+01	2025-08-29 16:07:20.415783+01	t	2026-02-23	1	1	2025-09-05
2	2025-08-25 14:15:05.490253+01	2025-08-29 16:07:39.783406+01	f	2026-06-02	2	2	2026-03-15
\.


--
-- TOC entry 3553 (class 0 OID 17292)
-- Dependencies: 224
-- Data for Name: students; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.students (id, creation_date, last_modified_date, cycle, date_of_birth, email, first_name, last_name, level, matricule, place_of_birth, speciality) FROM stdin;
1	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-03-15	alice.cooper@student.university.edu	Alice	Cooper	LEVEL1	STU2024001	Yaoundé	Computer Science
2	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-07-22	bob.martin@student.university.edu	Bob	Martin	LEVEL1	STU2024002	Douala	Mathematics
3	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-01-10	carol.white@student.university.edu	Carol	White	LEVEL1	STU2024003	Bamenda	Physics
4	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-04-12	david.green@student.university.edu	David	Green	LEVEL1	STU2024004	Yaoundé	Computer Science
5	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-08-30	eva.black@student.university.edu	Eva	Black	LEVEL1	STU2024005	Douala	Mathematics
6	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-02-14	frank.blue@student.university.edu	Frank	Blue	LEVEL1	STU2024006	Bamenda	Physics
7	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-05-20	grace.red@student.university.edu	Grace	Red	LEVEL1	STU2024007	Yaoundé	Business Administration
8	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-09-15	henry.yellow@student.university.edu	Henry	Yellow	LEVEL1	STU2024008	Douala	Computer Science
9	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-01-15	iris.purple@student.university.edu	Iris	Purple	LEVEL1	STU2024009	Yaoundé	Mathematics
10	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-05-22	jack.orange@student.university.edu	Jack	Orange	LEVEL1	STU2024010	Douala	Physics
11	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-03-08	kate.brown@student.university.edu	Kate	Brown	LEVEL1	STU2024011	Bamenda	Business Administration
12	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-06-18	leo.gray@student.university.edu	Leo	Gray	LEVEL1	STU2024012	Yaoundé	Computer Science
13	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-10-25	mia.silver@student.university.edu	Mia	Silver	LEVEL1	STU2024013	Douala	Mathematics
14	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-04-03	noah.gold@student.university.edu	Noah	Gold	LEVEL1	STU2024014	Bamenda	Physics
15	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2005-07-11	olivia.pink@student.university.edu	Olivia	Pink	LEVEL1	STU2024015	Yaoundé	Business Administration
16	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-03-15	paul.cyan@student.university.edu	Paul	Cyan	LEVEL2	STU2023001	Yaoundé	Computer Science
17	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-07-22	quinn.magenta@student.university.edu	Quinn	Magenta	LEVEL2	STU2023002	Douala	Mathematics
18	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-01-10	ruby.lime@student.university.edu	Ruby	Lime	LEVEL2	STU2023003	Bamenda	Physics
19	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-04-12	sam.teal@student.university.edu	Sam	Teal	LEVEL2	STU2023004	Yaoundé	Computer Science
20	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-08-30	tina.coral@student.university.edu	Tina	Coral	LEVEL2	STU2023005	Douala	Mathematics
21	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-02-14	uma.ivory@student.university.edu	Uma	Ivory	LEVEL2	STU2023006	Bamenda	Physics
22	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-05-20	victor.jade@student.university.edu	Victor	Jade	LEVEL2	STU2023007	Yaoundé	Business Administration
23	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-09-15	wendy.ruby@student.university.edu	Wendy	Ruby	LEVEL2	STU2023008	Douala	Computer Science
24	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-01-15	xander.amber@student.university.edu	Xander	Amber	LEVEL2	STU2023009	Yaoundé	Mathematics
25	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-05-22	yara.pearl@student.university.edu	Yara	Pearl	LEVEL2	STU2023010	Douala	Physics
26	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-03-08	zoe.onyx@student.university.edu	Zoe	Onyx	LEVEL2	STU2023011	Bamenda	Business Administration
27	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-06-18	adam.copper@student.university.edu	Adam	Copper	LEVEL2	STU2023012	Yaoundé	Computer Science
28	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-10-25	bella.bronze@student.university.edu	Bella	Bronze	LEVEL2	STU2023013	Douala	Mathematics
29	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-04-03	chris.steel@student.university.edu	Chris	Steel	LEVEL2	STU2023014	Bamenda	Physics
30	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2004-07-11	diana.platinum@student.university.edu	Diana	Platinum	LEVEL2	STU2023015	Yaoundé	Business Administration
31	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-03-15	ethan.diamond@student.university.edu	Ethan	Diamond	LEVEL3	STU2022001	Yaoundé	Computer Science
32	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-07-22	fiona.emerald@student.university.edu	Fiona	Emerald	LEVEL3	STU2022002	Douala	Mathematics
33	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-01-10	george.sapphire@student.university.edu	George	Sapphire	LEVEL3	STU2022003	Bamenda	Physics
34	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-04-12	hannah.topaz@student.university.edu	Hannah	Topaz	LEVEL3	STU2022004	Yaoundé	Engineering
35	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-08-30	ian.garnet@student.university.edu	Ian	Garnet	LEVEL3	STU2022005	Douala	Computer Science
36	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-02-14	julia.opal@student.university.edu	Julia	Opal	LEVEL3	STU2022006	Bamenda	Mathematics
37	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-05-20	kevin.quartz@student.university.edu	Kevin	Quartz	LEVEL3	STU2022007	Yaoundé	Physics
38	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-09-15	luna.agate@student.university.edu	Luna	Agate	LEVEL3	STU2022008	Douala	Engineering
39	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-01-15	max.jasper@student.university.edu	Max	Jasper	LEVEL3	STU2022009	Yaoundé	Computer Science
40	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	BACHELOR	2003-05-22	nina.turquoise@student.university.edu	Nina	Turquoise	LEVEL3	STU2022010	Douala	Business Administration
41	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-03-15	oscar.obsidian@student.university.edu	Oscar	Obsidian	LEVEL4	STU2021001	Yaoundé	Computer Science
42	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-07-22	penny.peridot@student.university.edu	Penny	Peridot	LEVEL4	STU2021002	Douala	Engineering
43	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-01-10	quinn.citrine@student.university.edu	Quinn	Citrine	LEVEL4	STU2021003	Bamenda	Computer Science
44	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-04-12	rose.amethyst@student.university.edu	Rose	Amethyst	LEVEL4	STU2021004	Yaoundé	Engineering
45	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-08-30	steve.beryl@student.university.edu	Steve	Beryl	LEVEL4	STU2021005	Douala	Computer Science
46	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-02-14	tara.carnelian@student.university.edu	Tara	Carnelian	LEVEL4	STU2021006	Bamenda	Engineering
47	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-05-20	ulrich.fluorite@student.university.edu	Ulrich	Fluorite	LEVEL4	STU2021007	Yaoundé	Computer Science
48	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-09-15	vera.hematite@student.university.edu	Vera	Hematite	LEVEL4	STU2021008	Douala	Engineering
49	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-01-15	will.labradorite@student.university.edu	Will	Labradorite	LEVEL4	STU2021009	Yaoundé	Computer Science
50	2025-08-26 10:16:28.18294+01	2025-08-26 10:16:28.18294+01	MASTER	2002-05-22	zara.malachite@student.university.edu	Zara	Malachite	LEVEL4	STU2021010	Douala	Engineering
252	2025-08-28 09:10:31.043769+01	2025-08-28 09:10:31.043769+01	MASTER	\N	lenguesandra@gmail.com	Sonia	Tchuenteu	LEVEL5	13S08183	\N	Business Administration
\.


--
-- TOC entry 3554 (class 0 OID 17301)
-- Dependencies: 225
-- Data for Name: subject; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.subject (id, creation_date, last_modified_date, active, code, credits, cycle, description, id_teacher, level, name, department_id, id_semester) FROM stdin;
11	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS102	4.00	BACHELOR	OOP concepts using Java	2	LEVEL1	Object-Oriented Programming	1	2
2	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	MATH101	3.00	BACHELOR	Differential and integral calculus	3	LEVEL1	Calculus I	2	1
3	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	PHYS101	3.00	BACHELOR	Mechanics and thermodynamics	4	LEVEL1	Physics I	3	1
6	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	PHYS201	3.00	BACHELOR	Electric fields, magnetic fields, and circuits	4	LEVEL2	Electricity and Magnetism	3	1
14	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS202	4.00	BACHELOR	Database design and SQL	7	LEVEL2	Database Systems	1	2
16	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	PHYS202	3.00	BACHELOR	Quantum and relativity basics	9	LEVEL2	Modern Physics	3	2
18	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	ENG302	3.00	BACHELOR	Fluid flow and dynamics	10	LEVEL3	Fluid Mechanics	4	2
19	2025-08-25 14:32:02.759507+01	2025-08-28 18:53:57.345778+01	t	CS402	4.00	MASTER	Neural networks and AI	2	LEVEL4	Deep Learning	56	2
12	2025-08-25 14:32:02.759507+01	2025-08-28 18:55:16.800496+01	t	MATH102	3.00	BACHELOR	Advanced calculus and series	8	LEVEL1	Calculus II	106	2
20	2025-08-25 14:32:02.759507+01	2025-08-28 18:55:34.176771+01	t	ENG402	3.00	MASTER	Automatic control theory	5	LEVEL4	Control Systems	156	2
171	2025-08-29 17:25:53.856135+01	2025-08-29 17:26:10.686728+01	t	COS1032	6.00	BACHELOR	The study of the cosmos	2	LEVEL3	Cosmology	1	1
13	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	PHYS102	3.00	BACHELOR	Waves and optics	\N	LEVEL1	Physics II	3	2
17	2025-08-25 14:32:02.759507+01	2025-08-25 14:32:02.759507+01	t	CS302	4.00	BACHELOR	Software development lifecycle	\N	LEVEL3	Software Engineering	1	2
1	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	CS101	4.00	BACHELOR	Basic programming concepts using Python	\N	LEVEL1	Introduction to Programming	1	1
7	2025-08-25 14:15:05.493697+01	2025-08-28 18:56:04.518983+01	t	CS301	4.00	BACHELOR	Algorithm design and analysis	\N	LEVEL3	Algorithms	206	1
9	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	CS401	4.00	MASTER	Supervised and unsupervised learning algorithms	\N	LEVEL4	Machine Learning	1	1
5	2025-08-25 14:15:05.493697+01	2025-08-25 14:15:05.493697+01	t	MATH201	3.00	BACHELOR	Vectors, matrices, and linear transformations	\N	LEVEL2	Linear Algebra	2	1
4	2025-08-25 14:15:05.493697+01	2025-08-29 12:29:17.545103+01	t	CS201	4.00	BACHELOR	Arrays, linked lists, trees, and graphs	2	LEVEL2	Data Structures	1	1
8	2025-08-25 14:15:05.493697+01	2025-08-28 09:01:37.617389+01	t	ENG301	3.00	BACHELOR	Heat transfer and energy systems	5	LEVEL3	Thermodynamics	2	1
10	2025-08-25 14:15:05.493697+01	2025-08-29 12:29:17.55657+01	t	ENG401	3.00	MASTER	Test update - 2025-08-27 16:09:41.495343+01	\N	LEVEL4	Advanced Mechanics	2	1
15	2025-08-25 14:32:02.759507+01	2025-08-28 09:01:37.603432+01	t	MATH202	3.00	BACHELOR	Probability and statistics	3	LEVEL2	Statistics	3	2
\.


--
-- TOC entry 3567 (class 0 OID 17398)
-- Dependencies: 238
-- Data for Name: user_levels; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_levels (user_id, level) FROM stdin;
2	LEVEL1
2	LEVEL2
2	LEVEL3
2	LEVEL4
3	LEVEL1
3	LEVEL2
4	LEVEL1
4	LEVEL2
5	LEVEL3
5	LEVEL4
6	LEVEL1
6	LEVEL2
7	LEVEL2
7	LEVEL3
7	LEVEL4
8	LEVEL1
8	LEVEL2
9	LEVEL2
9	LEVEL3
10	LEVEL3
10	LEVEL4
11	LEVEL1
11	LEVEL2
11	LEVEL3
113	LEVEL1
113	LEVEL2
113	LEVEL3
\.


--
-- TOC entry 3555 (class 0 OID 17310)
-- Dependencies: 226
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, creation_date, last_modified_date, active, department, email, first_name, last_name, must_change_password, password, phone, role, username) FROM stdin;
11	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:37.698248+01	t	Business Administration	jthomas@university.edu	Jennifer	Thomas	f	$2a$10$kbq2EAXpcyYFB4Z5FxAp1OQuf1gH5kLlV6.0M1HZVfTjDBvpVKoQW	+237123456789	TEACHER	prof.thomas
12	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:37.875202+01	t	\N	alice.cooper@student.university.edu	Alice	Cooper	f	$2a$10$m1yNiLvTZXLFDWGI3YY0aOeroO744IIc9CsRWF5gOESAZpeor8i5m	\N	STUDENT	STU2024001
14	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.193241+01	t	\N	carol.white@student.university.edu	Carol	White	f	$2a$10$6QOSgtWRsHlO/k4.vS.dSOmu59.cwLxV4Hrootfj74bJxqwu.2IvG	\N	STUDENT	STU2024003
15	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.342208+01	t	\N	david.green@student.university.edu	David	Green	f	$2a$10$J74HdusRqu33dHgw7yjzQOF9/Re01sXG8XVb21f8C1FZljRv3GJ3y	\N	STUDENT	STU2024004
16	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.505948+01	t	\N	eva.black@student.university.edu	Eva	Black	f	$2a$10$oOmKR.v6KhpqqBP5sGAcMO4V4IDIIXQhao8R06w8F1QUv7Ohrr1sC	\N	STUDENT	STU2024005
17	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.65767+01	t	\N	frank.blue@student.university.edu	Frank	Blue	f	$2a$10$7nXo6VfGbjAnv.6L24B6BusxbQFmCwnhw01S7W4W86YmjV7qqVeH2	\N	STUDENT	STU2024006
18	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.819708+01	t	\N	grace.red@student.university.edu	Grace	Red	f	$2a$10$O9ZzcY4d1MyiFd9xWzOQ9uG1uy.nd5yQwSBid9IbC7ZZgg3hCHQwC	\N	STUDENT	STU2024007
19	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.967771+01	t	\N	henry.yellow@student.university.edu	Henry	Yellow	f	$2a$10$r1aZNMddo9SwLWAUSV0Hg.r9lqF29CCYa6ku7N.Hg7/QDpis7TgM2	\N	STUDENT	STU2024008
20	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:39.134874+01	t	\N	iris.purple@student.university.edu	Iris	Purple	f	$2a$10$Ctg4FFLBqdVb28c.AXGQyOd6Lj5M.zbCViXX9dgRwlbBYMoxXTmF6	\N	STUDENT	STU2024009
21	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:39.282732+01	t	\N	jack.orange@student.university.edu	Jack	Orange	f	$2a$10$x4/xV3797ioWNg52o896jeRltenFB4I8B0bNfF9E7htunqWUmUjhq	\N	STUDENT	STU2024010
22	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:39.442528+01	t	\N	kate.brown@student.university.edu	Kate	Brown	f	$2a$10$YGjSnfc6M7OlKz4xP35KuO9Xpd5CwpkFjodZTZb6zVjpe5C4CSbFC	\N	STUDENT	STU2024011
23	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:39.59076+01	t	\N	leo.gray@student.university.edu	Leo	Gray	f	$2a$10$OjnO5SsLT4TqWnXTHaK2reFT.EAFo4I6DlTtWdUdWMXa.ob72P4h.	\N	STUDENT	STU2024012
24	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:39.760299+01	t	\N	mia.silver@student.university.edu	Mia	Silver	f	$2a$10$j8InGP9gP46GtIwzqIE7/O7crKvsco3R5NpnjM3k7STFdF3hlqdy6	\N	STUDENT	STU2024013
25	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:39.911133+01	t	\N	noah.gold@student.university.edu	Noah	Gold	f	$2a$10$e8GwZPv6AULrAvfgiVhkDesvo8vl.F8KWXhGVaolfo7kbYm/gXO02	\N	STUDENT	STU2024014
26	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:40.089907+01	t	\N	olivia.pink@student.university.edu	Olivia	Pink	f	$2a$10$i.UQH5wL4RUD95pabpUhIuGNmx4dGxO/prBF3jaJzqMug7Ro32q7G	\N	STUDENT	STU2024015
27	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:40.26004+01	t	\N	paul.cyan@student.university.edu	Paul	Cyan	f	$2a$10$75rn6OvXFzw9rG0y5tMh9uXeoiKqsojnHlBWZiJBFgQhLcJnmnACm	\N	STUDENT	STU2023001
31	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:40.884736+01	t	\N	tina.coral@student.university.edu	Tina	Coral	f	$2a$10$WBDEaHMP9duj2pR9XZJ7eOoCKDJM4EY/qnFkep4MIT12o5AofcwLO	\N	STUDENT	STU2023005
32	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:41.066558+01	t	\N	uma.ivory@student.university.edu	Uma	Ivory	f	$2a$10$1UpiFIjTnMhhZoncpiLLWuoy2FBK4g7/2xHnWRFLyZ7YNE95gipDy	\N	STUDENT	STU2023006
33	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:41.211932+01	t	\N	victor.jade@student.university.edu	Victor	Jade	f	$2a$10$co1.xSkprwr7PABwXCtP5Oi3xPkGmcP7HfPsUCKskO489pIey4ffi	\N	STUDENT	STU2023007
34	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:41.376308+01	t	\N	wendy.ruby@student.university.edu	Wendy	Ruby	f	$2a$10$g0u0823oXk/wdoHm/jEhtu.hcG93fPpq1Lnwa60WFofnSub2Hr4PW	\N	STUDENT	STU2023008
35	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:41.523714+01	t	\N	xander.amber@student.university.edu	Xander	Amber	f	$2a$10$MaI1xqMmvJknaIhWVpKgbuOZgggPxkn/SjWZQ2yH4Y45.lEMgsge2	\N	STUDENT	STU2023009
36	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:41.680139+01	t	\N	yara.pearl@student.university.edu	Yara	Pearl	f	$2a$10$aR7DMq0IyzrAFy3yVLhVTO5cu69DXAsdaX5C7pkHt6x2zeLXF67YW	\N	STUDENT	STU2023010
37	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:41.827006+01	t	\N	zoe.onyx@student.university.edu	Zoe	Onyx	f	$2a$10$6JXWakkovseKY8N2fIM/JORweRJj4m/z4SSAmihCVAQdyj6mHpdU6	\N	STUDENT	STU2023011
38	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.003795+01	t	\N	adam.copper@student.university.edu	Adam	Copper	f	$2a$10$gCieyt01oRC1NChXmXxkues8M9ijQ4pA9nNiOAWNP6VdGL7tZajv6	\N	STUDENT	STU2023012
39	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.149389+01	t	\N	bella.bronze@student.university.edu	Bella	Bronze	f	$2a$10$d4HXem1IAKK9Ur7iGi1FMOynQ1cMAEWYGO2JSoAW9ng4CyDk8m7/G	\N	STUDENT	STU2023013
40	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.307738+01	t	\N	chris.steel@student.university.edu	Chris	Steel	f	$2a$10$iRPOYRNDEsJBp2X7sls2mOLw.IgkyDXRg4bGEF3LaIgx18pgUnzxO	\N	STUDENT	STU2023014
41	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.453943+01	t	\N	diana.platinum@student.university.edu	Diana	Platinum	f	$2a$10$bp0XPsm.qRSaT4qB4DR0EuNM61U9uA17fOCs4r63HZTquQMRlPI9e	\N	STUDENT	STU2023015
42	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.615447+01	t	\N	ethan.diamond@student.university.edu	Ethan	Diamond	f	$2a$10$H/C.vLdeTnEUQ0Td07mg4eYuI9vNc0mqCOESKkxROhInVllo6g6XS	\N	STUDENT	STU2022001
43	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.763043+01	t	\N	fiona.emerald@student.university.edu	Fiona	Emerald	f	$2a$10$kR1IjETGkLr1Ik40tM6t.unfaEG1W/TlmOEb4sCPEpZUBb9lfrri6	\N	STUDENT	STU2022002
44	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:42.928324+01	t	\N	george.sapphire@student.university.edu	George	Sapphire	f	$2a$10$nu03qPGX5CUhNACvuDFV/OBfATFFPj.9mwvw0j3i15E5VToBJ9uYG	\N	STUDENT	STU2022003
45	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:43.074443+01	t	\N	hannah.topaz@student.university.edu	Hannah	Topaz	f	$2a$10$TMx.hmzaed7AOulKDEWxb.HCWNOnycc4zH7AuY9LHex917R.XsSBG	\N	STUDENT	STU2022004
46	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:43.241301+01	t	\N	ian.garnet@student.university.edu	Ian	Garnet	f	$2a$10$hcWqgC0XkEgzjxqXARJs7uw2HZIQrqBKZz2QC2ZmNo/laMrn8E3Lm	\N	STUDENT	STU2022005
47	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:43.390085+01	t	\N	julia.opal@student.university.edu	Julia	Opal	f	$2a$10$YGUmNlJaOVUnJSuKpnt5pejszeh0p/dfMOXuzBahrxXmJY7sMZ8KC	\N	STUDENT	STU2022006
48	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:43.556932+01	t	\N	kevin.quartz@student.university.edu	Kevin	Quartz	f	$2a$10$BFVJfg2ve65xRaXFppw0Z.lrOUwxvE2v.Qj4qo3OQp.jDoQeorGEG	\N	STUDENT	STU2022007
49	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:43.703067+01	t	\N	luna.agate@student.university.edu	Luna	Agate	f	$2a$10$DkvUQnDX3x1tYFxfbKuD5eYHaH6y2.gInchMD4w5Wuu60s2rnDauS	\N	STUDENT	STU2022008
50	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:43.864996+01	t	\N	max.jasper@student.university.edu	Max	Jasper	f	$2a$10$czmQZdCxcuEImM81W5eqoOOeOEpVO2SGEA4qFoSSFx/w03FcLCZfm	\N	STUDENT	STU2022009
51	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.01289+01	t	\N	nina.turquoise@student.university.edu	Nina	Turquoise	f	$2a$10$ma5v.iPEIMlk8/2TvizoYuofSZ79rkQDCGO7WE7qkZSPP.SZeUFMu	\N	STUDENT	STU2022010
2	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:36.182184+01	t	Computer Science	mjohnson@university.edu	Michael	Johnson	f	$2a$10$ZI.J0amX9OVa2wjcMgkuYuLFqCNL8Q6cwdutLPDOxwuX7fKpDwnfu	+237123456789	TEACHER	prof.johnson
3	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:36.365114+01	t	Mathematics	swilliams@university.edu	Sarah	Williams	f	$2a$10$ouRVzp.tsiGd1KKpSOe8Uevlm9d35ohCrMl/sUPLWLJlMZjrO54PW	+237123456789	TEACHER	prof.williams
4	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:36.554628+01	t	Physics	dbrown@university.edu	David	Brown	f	$2a$10$M/iFd2Um1K3fqGrjOEhS9OQl4nfxVNXiioJafRzqIrFeF5HdAuQr6	+237123456789	TEACHER	prof.brown
5	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:36.705666+01	t	Engineering	edavis@university.edu	Emily	Davis	f	$2a$10$QcKF/esjhIGgOkKqKPitEeRFcrbqB1SNEOPHCvzu9xPWp7NgX.38u	+237123456789	TEACHER	prof.davis
6	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:36.87563+01	t	Business Administration	rwilson@university.edu	Robert	Wilson	f	$2a$10$F/AW9maGUo5BOGZvlo0lOeDIMqbXCdEhu8enhAchx1PC5Viml2pwy	+237123456789	TEACHER	prof.wilson
7	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:37.029458+01	t	Computer Science	mgarcia@university.edu	Maria	Garcia	f	$2a$10$LtbPP0rGMdE0yZPOrWzHweh2Bw4c8YDy6bkNy/P5UUDqtHpG3vZtq	+237123456789	TEACHER	prof.garcia
8	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:37.203055+01	t	Mathematics	cmartinez@university.edu	Carlos	Martinez	f	$2a$10$AhXMVKeGMPdynh9DvmjAyOHwz0/DBgRwtNTHTmzXCO6KgjGbABfCS	+237123456789	TEACHER	prof.martinez
9	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:37.351813+01	t	Physics	landerson@university.edu	Lisa	Anderson	f	$2a$10$Osac0raxRjxXspDDXOGQ4OPaBtrmmQOsl6JpRyJCvnXdrhaXvygZK	+237123456789	TEACHER	prof.anderson
10	2025-08-26 10:16:28.15487+01	2025-08-26 11:11:37.519863+01	t	Engineering	jtaylor@university.edu	James	Taylor	f	$2a$10$w1gKkuOK/dib.PX0EOTIg.pThEQCNJofyQRt7tJ.Ka2zaFKnnISli	+237123456789	TEACHER	prof.taylor
13	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:38.030197+01	t	\N	bob.martin@student.university.edu	Bob	Martin	f	$2a$10$mXBxBjauNOS.0X60TcI1suiZNTOs.gLKGAm.3171ZpO7xuj3aBTRi	\N	STUDENT	STU2024002
52	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.184021+01	t	\N	oscar.obsidian@student.university.edu	Oscar	Obsidian	f	$2a$10$ZmXbOTnq87saSA5xV70HhOeTmVjBLrLnx/FwmDx/2UHkN0kVJYIr.	\N	STUDENT	STU2021001
53	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.331984+01	t	\N	penny.peridot@student.university.edu	Penny	Peridot	f	$2a$10$XAJoLSPQeAg1y.RviMim.uITxsQFYbfRnH.PhWrMRDjFNNPJghWIG	\N	STUDENT	STU2021002
54	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.503208+01	t	\N	quinn.citrine@student.university.edu	Quinn	Citrine	f	$2a$10$LaZQET6BxaLCvMu10pZeWuTMJJv8ad8I/zujnnEW/Qbw4oyz5Albe	\N	STUDENT	STU2021003
55	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.650666+01	t	\N	rose.amethyst@student.university.edu	Rose	Amethyst	f	$2a$10$hrfVCJ7UpofsYVRaqhYFhugaSgBE7l.bTIhK6PoZqWQ8M49QDWDS6	\N	STUDENT	STU2021004
56	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.82348+01	t	\N	steve.beryl@student.university.edu	Steve	Beryl	f	$2a$10$HwS.LNDhXj3oI8UaWT1xOeNfN7n3g6HceSimeBEzCGYU0Q3E7hDhm	\N	STUDENT	STU2021005
57	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:44.977716+01	t	\N	tara.carnelian@student.university.edu	Tara	Carnelian	f	$2a$10$xJmi5r4/HBjVnjVhfLWqAuZgF2VCgXyaYOR5soAaQZ11uaPaxafRe	\N	STUDENT	STU2021006
58	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:45.147038+01	t	\N	ulrich.fluorite@student.university.edu	Ulrich	Fluorite	f	$2a$10$nMR8Yprjwm5JFasDNnYuF.nLTMnk6JKRu0iP.Dob2xtmU.10w0kEy	\N	STUDENT	STU2021007
59	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:45.292289+01	t	\N	vera.hematite@student.university.edu	Vera	Hematite	f	$2a$10$rIKaE8VdlbtmS.TCfLYhSOPM9A2YvYiEROP.MPRn.oFQ2xw0EMhZC	\N	STUDENT	STU2021008
60	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:45.456598+01	t	\N	will.labradorite@student.university.edu	Will	Labradorite	f	$2a$10$zZnSBUkelYLoLfvL.5CZ5ubNym8PkqqRQgRGOzGzNt28CcubyR5cq	\N	STUDENT	STU2021009
61	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:45.636121+01	t	\N	zara.malachite@student.university.edu	Zara	Malachite	f	$2a$10$nt6IERlisWcOTSYUhCv6vOfutFkuk3yI8Uv54MjhAwxv2quk7Bk.C	\N	STUDENT	STU2021010
113	2025-08-27 09:09:56.870027+01	2025-08-27 17:23:04.639965+01	t	Biochemistry	etoundiMaphose@gmail.com	Etoundi	Maphose	f	$2a$10$2p63aIl1ZKQA6kVJVNJJzuH4OxJUDspG9AZX7.7BgVOlbzapmdmga	+237123456789	TEACHER	Prof.Etoundi
1	2025-08-26 10:16:28.141099+01	2025-08-29 18:15:38.053701+01	t	Administration	admin@university.edu	System	Administrator	f	$2a$10$FMPnoYze5GQGYmvXzUXUReMmDnhQ7Rum5xeGomStP74vufuJLb4IO	+237123456789	ADMIN	admin
28	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:40.412861+01	t	\N	quinn.magenta@student.university.edu	Quinn	Magenta	f	$2a$10$krjQy/aOW62jUm38RbHid.4ItBr0HhSQ39palcxuhV7RUSdt5bWlW	\N	STUDENT	STU2023002
29	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:40.558394+01	t	\N	ruby.lime@student.university.edu	Ruby	Lime	f	$2a$10$P8fO82JqpL.OOWSUO7XBMOrls22bhcn28sJq5nYsYX3XUuHqQcIkO	\N	STUDENT	STU2023003
30	2025-08-26 10:16:28.177803+01	2025-08-26 11:11:40.720438+01	t	\N	sam.teal@student.university.edu	Sam	Teal	f	$2a$10$gUZ6gVPuQRtWx49EzjW28ujaA6kXHQAEd1kxz9TftG.BM7h9F/LSy	\N	STUDENT	STU2023004
62	2025-08-26 23:12:45.660795+01	2025-08-26 23:12:45.660795+01	t	\N	jinidi6226@mobilesm.com	Kemit	Sure	t	$2a$10$9VbrK65IrxpHf5eoiyc7yuzi6eMNLjuJg0eQvZkQ8UAW2qdUpR//e	\N	STUDENT	sonia
115	2025-08-28 09:10:31.003603+01	2025-08-28 09:10:31.003603+01	t	\N	lenguesandra@gmail.com	Sonia	Tchuenteu	t	$2a$10$QzpnV2C6TBYqvh79K6df0.okNKFMaCfADM3trATFAo/bS0Eg74C0u	\N	STUDENT	13S08183
\.


--
-- TOC entry 3576 (class 0 OID 0)
-- Dependencies: 227
-- Name: departments_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.departments_seq', 356, true);


--
-- TOC entry 3577 (class 0 OID 0)
-- Dependencies: 228
-- Name: grade_claims_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grade_claims_seq', 105, true);


--
-- TOC entry 3578 (class 0 OID 0)
-- Dependencies: 229
-- Name: grade_report_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grade_report_seq', 10, true);


--
-- TOC entry 3579 (class 0 OID 0)
-- Dependencies: 230
-- Name: grades_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grades_seq', 515, true);


--
-- TOC entry 3580 (class 0 OID 0)
-- Dependencies: 231
-- Name: grading_window_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.grading_window_seq', 1, false);


--
-- TOC entry 3581 (class 0 OID 0)
-- Dependencies: 232
-- Name: report_record_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.report_record_seq', 5, true);


--
-- TOC entry 3582 (class 0 OID 0)
-- Dependencies: 233
-- Name: semesters_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.semesters_seq', 103, true);


--
-- TOC entry 3583 (class 0 OID 0)
-- Dependencies: 234
-- Name: student_info_requests_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.student_info_requests_seq', 1, false);


--
-- TOC entry 3584 (class 0 OID 0)
-- Dependencies: 235
-- Name: students_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.students_seq', 253, false);


--
-- TOC entry 3585 (class 0 OID 0)
-- Dependencies: 236
-- Name: subject_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.subject_seq', 271, true);


--
-- TOC entry 3586 (class 0 OID 0)
-- Dependencies: 241
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 115, true);


--
-- TOC entry 3587 (class 0 OID 0)
-- Dependencies: 237
-- Name: users_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_seq', 116, false);


--
-- TOC entry 3360 (class 2606 OID 17230)
-- Name: departments departments_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT departments_pkey PRIMARY KEY (id);


--
-- TOC entry 3364 (class 2606 OID 17238)
-- Name: grade_claims grade_claims_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT grade_claims_pkey PRIMARY KEY (id);


--
-- TOC entry 3384 (class 2606 OID 25604)
-- Name: grade_report grade_report_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_report
    ADD CONSTRAINT grade_report_pkey PRIMARY KEY (id);


--
-- TOC entry 3366 (class 2606 OID 17253)
-- Name: grades grades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT grades_pkey PRIMARY KEY (id);


--
-- TOC entry 3368 (class 2606 OID 17261)
-- Name: grading_window grading_window_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_window
    ADD CONSTRAINT grading_window_pkey PRIMARY KEY (id);


--
-- TOC entry 3386 (class 2606 OID 25611)
-- Name: report_record report_record_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.report_record
    ADD CONSTRAINT report_record_pkey PRIMARY KEY (id);


--
-- TOC entry 3370 (class 2606 OID 17283)
-- Name: semesters semesters_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.semesters
    ADD CONSTRAINT semesters_pkey PRIMARY KEY (id);


--
-- TOC entry 3372 (class 2606 OID 17300)
-- Name: students students_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT students_pkey PRIMARY KEY (id);


--
-- TOC entry 3376 (class 2606 OID 17309)
-- Name: subject subject_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT subject_pkey PRIMARY KEY (id);


--
-- TOC entry 3374 (class 2606 OID 17323)
-- Name: students uk_1psraxut9bwn2why6ex4xf1en; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.students
    ADD CONSTRAINT uk_1psraxut9bwn2why6ex4xf1en UNIQUE (matricule);


--
-- TOC entry 3362 (class 2606 OID 17319)
-- Name: departments uk_j6cwks7xecs5jov19ro8ge3qk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.departments
    ADD CONSTRAINT uk_j6cwks7xecs5jov19ro8ge3qk UNIQUE (name);


--
-- TOC entry 3380 (class 2606 OID 17325)
-- Name: users uk_r43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk_r43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- TOC entry 3378 (class 2606 OID 33845)
-- Name: subject uk_teacher_level; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT uk_teacher_level UNIQUE (id_teacher, level);


--
-- TOC entry 3382 (class 2606 OID 17317)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 3401 (class 2620 OID 33810)
-- Name: grades check_student_consistency; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER check_student_consistency BEFORE INSERT OR UPDATE ON public.grades FOR EACH ROW EXECUTE FUNCTION public.validate_student_user_consistency();


--
-- TOC entry 3402 (class 2620 OID 33831)
-- Name: grades ensure_grade_integrity_trigger; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER ensure_grade_integrity_trigger BEFORE INSERT OR UPDATE ON public.grades FOR EACH ROW EXECUTE FUNCTION public.ensure_grade_integrity();


--
-- TOC entry 3387 (class 2606 OID 17342)
-- Name: grade_claims fk2kbul9dstw4uo15gc5tomfkni; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fk2kbul9dstw4uo15gc5tomfkni FOREIGN KEY (grade_id) REFERENCES public.grades(id);


--
-- TOC entry 3391 (class 2606 OID 17367)
-- Name: grades fk384e8h2qimc9qlnh970ytgdr5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fk384e8h2qimc9qlnh970ytgdr5 FOREIGN KEY (id_students) REFERENCES public.students(id);


--
-- TOC entry 3398 (class 2606 OID 17392)
-- Name: subject fk4b66tj7yip7jmo922vvy6bw4y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT fk4b66tj7yip7jmo922vvy6bw4y FOREIGN KEY (id_semester) REFERENCES public.semesters(id);


--
-- TOC entry 3388 (class 2606 OID 17337)
-- Name: grade_claims fk8b4k9pw7j018vpks0ns5wmsp0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fk8b4k9pw7j018vpks0ns5wmsp0 FOREIGN KEY (created_by_id) REFERENCES public.users(id);


--
-- TOC entry 3392 (class 2606 OID 33825)
-- Name: grades fk_grades_semester; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fk_grades_semester FOREIGN KEY (id_semester) REFERENCES public.semesters(id) ON DELETE SET NULL;


--
-- TOC entry 3393 (class 2606 OID 33820)
-- Name: grades fk_grades_students; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fk_grades_students FOREIGN KEY (id_students) REFERENCES public.students(id) ON DELETE CASCADE;


--
-- TOC entry 3389 (class 2606 OID 17352)
-- Name: grade_claims fkc1rbjpi3wpf5ihn0h81pygtnc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fkc1rbjpi3wpf5ihn0h81pygtnc FOREIGN KEY (student_id) REFERENCES public.students(id);


--
-- TOC entry 3394 (class 2606 OID 17357)
-- Name: grades fkfrqv6jj26ycmq0uqihsmofd9w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkfrqv6jj26ycmq0uqihsmofd9w FOREIGN KEY (id_users) REFERENCES public.users(id);


--
-- TOC entry 3395 (class 2606 OID 17372)
-- Name: grades fkippbjcacm6nkn7hon47r53l99; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fkippbjcacm6nkn7hon47r53l99 FOREIGN KEY (id_subject) REFERENCES public.subject(id);


--
-- TOC entry 3397 (class 2606 OID 17377)
-- Name: grading_window fkmkan866y61akk4qim8e74d3dj; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grading_window
    ADD CONSTRAINT fkmkan866y61akk4qim8e74d3dj FOREIGN KEY (id_semester) REFERENCES public.semesters(id);


--
-- TOC entry 3399 (class 2606 OID 17387)
-- Name: subject fkqym877gemkcwuhmjmbuokvf6f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.subject
    ADD CONSTRAINT fkqym877gemkcwuhmjmbuokvf6f FOREIGN KEY (department_id) REFERENCES public.departments(id);


--
-- TOC entry 3400 (class 2606 OID 17401)
-- Name: user_levels fkr5aqf5bnqm78uhohv89f203v2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_levels
    ADD CONSTRAINT fkr5aqf5bnqm78uhohv89f203v2 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- TOC entry 3396 (class 2606 OID 17362)
-- Name: grades fks0yeww9160sohy3wpgmt7dve; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grades
    ADD CONSTRAINT fks0yeww9160sohy3wpgmt7dve FOREIGN KEY (id_semester) REFERENCES public.semesters(id);


--
-- TOC entry 3390 (class 2606 OID 17347)
-- Name: grade_claims fkteaqiirgxcnndhnx4ljcoc60h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.grade_claims
    ADD CONSTRAINT fkteaqiirgxcnndhnx4ljcoc60h FOREIGN KEY (semester_id) REFERENCES public.semesters(id);


-- Completed on 2025-08-29 18:31:58 WAT

--
-- PostgreSQL database dump complete
--

