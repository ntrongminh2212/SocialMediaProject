--
-- PostgreSQL database dump
--

-- Dumped from database version 15.2
-- Dumped by pg_dump version 15.2

-- Started on 2023-06-08 14:22:42

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 221 (class 1259 OID 41133)
-- Name: batch_job_execution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_job_execution (
    job_execution_id bigint NOT NULL,
    version bigint,
    job_instance_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone
);


ALTER TABLE public.batch_job_execution OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 41179)
-- Name: batch_job_execution_context; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_job_execution_context (
    job_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


ALTER TABLE public.batch_job_execution_context OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 41145)
-- Name: batch_job_execution_params; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_job_execution_params (
    job_execution_id bigint NOT NULL,
    parameter_name character varying(100) NOT NULL,
    parameter_type character varying(100) NOT NULL,
    parameter_value character varying(2500),
    identifying character(1) NOT NULL
);


ALTER TABLE public.batch_job_execution_params OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 41192)
-- Name: batch_job_execution_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.batch_job_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.batch_job_execution_seq OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 41126)
-- Name: batch_job_instance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_job_instance (
    job_instance_id bigint NOT NULL,
    version bigint,
    job_name character varying(100) NOT NULL,
    job_key character varying(32) NOT NULL
);


ALTER TABLE public.batch_job_instance OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 41193)
-- Name: batch_job_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.batch_job_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.batch_job_seq OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 41155)
-- Name: batch_step_execution; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_step_execution (
    step_execution_id bigint NOT NULL,
    version bigint NOT NULL,
    step_name character varying(100) NOT NULL,
    job_execution_id bigint NOT NULL,
    create_time timestamp without time zone NOT NULL,
    start_time timestamp without time zone,
    end_time timestamp without time zone,
    status character varying(10),
    commit_count bigint,
    read_count bigint,
    filter_count bigint,
    write_count bigint,
    read_skip_count bigint,
    write_skip_count bigint,
    process_skip_count bigint,
    rollback_count bigint,
    exit_code character varying(2500),
    exit_message character varying(2500),
    last_updated timestamp without time zone
);


ALTER TABLE public.batch_step_execution OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 41167)
-- Name: batch_step_execution_context; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.batch_step_execution_context (
    step_execution_id bigint NOT NULL,
    short_context character varying(2500) NOT NULL,
    serialized_context text
);


ALTER TABLE public.batch_step_execution_context OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 41191)
-- Name: batch_step_execution_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.batch_step_execution_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.batch_step_execution_seq OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 32890)
-- Name: comment_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.comment_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.comment_sequence OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 41194)
-- Name: flyway_schema_history; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.flyway_schema_history (
    installed_rank integer NOT NULL,
    version character varying(50),
    description character varying(200) NOT NULL,
    type character varying(20) NOT NULL,
    script character varying(1000) NOT NULL,
    checksum integer,
    installed_by character varying(100) NOT NULL,
    installed_on timestamp without time zone DEFAULT now() NOT NULL,
    execution_time integer NOT NULL,
    success boolean NOT NULL
);


ALTER TABLE public.flyway_schema_history OWNER TO postgres;

--
-- TOC entry 214 (class 1259 OID 32781)
-- Name: post_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.post_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.post_sequence OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 32885)
-- Name: tbl_comment; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_comment (
    comment_id bigint NOT NULL,
    content character varying(255),
    create_time timestamp(6) without time zone,
    update_time timestamp(6) without time zone,
    user_id bigint,
    post_id bigint
);


ALTER TABLE public.tbl_comment OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 32924)
-- Name: tbl_comment_reaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_comment_reaction (
    user_id bigint NOT NULL,
    reacted_time timestamp(6) without time zone,
    reaction character varying(255),
    comment_id bigint NOT NULL
);


ALTER TABLE public.tbl_comment_reaction OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 32853)
-- Name: tbl_post; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_post (
    post_id bigint NOT NULL,
    attachment_url character varying(255),
    creator_id bigint,
    status_content character varying(255),
    created_time timestamp(6) without time zone,
    updated_datetime timestamp(6) without time zone
);


ALTER TABLE public.tbl_post OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 32860)
-- Name: tbl_post_reaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_post_reaction (
    user_id bigint NOT NULL,
    reaction character varying(255),
    post_id bigint NOT NULL,
    reacted_time timestamp(6) without time zone
);


ALTER TABLE public.tbl_post_reaction OWNER TO postgres;

--
-- TOC entry 3398 (class 0 OID 41133)
-- Dependencies: 221
-- Data for Name: batch_job_execution; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.batch_job_execution (job_execution_id, version, job_instance_id, create_time, start_time, end_time, status, exit_code, exit_message, last_updated) FROM stdin;
1	2	1	2023-06-07 09:56:15.018142	2023-06-07 09:56:15.05614	2023-06-07 09:56:15.241141	COMPLETED	COMPLETED		2023-06-07 09:56:15.241141
2	2	2	2023-06-07 09:57:10.192433	2023-06-07 09:57:10.195431	2023-06-07 09:57:20.803377	COMPLETED	COMPLETED		2023-06-07 09:57:20.803377
3	2	3	2023-06-07 09:59:02.292926	2023-06-07 09:59:02.30394	2023-06-07 09:59:02.389941	COMPLETED	COMPLETED		2023-06-07 09:59:02.389941
4	2	4	2023-06-07 10:05:26.692864	2023-06-07 10:05:26.698874	2023-06-07 10:05:26.770876	COMPLETED	COMPLETED		2023-06-07 10:05:26.771875
5	2	5	2023-06-07 10:08:30.358422	2023-06-07 10:08:30.362422	2023-06-07 10:08:30.390421	COMPLETED	COMPLETED		2023-06-07 10:08:30.390421
6	2	6	2023-06-07 10:10:28.216215	2023-06-07 10:10:28.249216	2023-06-07 10:10:28.550216	FAILED	FAILED		2023-06-07 10:10:28.554217
7	2	7	2023-06-07 10:14:03.386893	2023-06-07 10:14:03.415917	2023-06-07 10:14:03.715892	FAILED	FAILED		2023-06-07 10:14:03.715892
8	2	8	2023-06-07 10:16:25.53107	2023-06-07 10:16:25.568061	2023-06-07 10:16:25.756063	COMPLETED	COMPLETED		2023-06-07 10:16:25.756063
9	2	9	2023-06-07 10:25:54.54394	2023-06-07 10:25:54.549953	2023-06-07 10:25:54.588949	COMPLETED	COMPLETED		2023-06-07 10:25:54.588949
10	2	10	2023-06-07 10:59:25.713702	2023-06-07 10:59:25.745702	2023-06-07 10:59:25.959704	COMPLETED	COMPLETED		2023-06-07 10:59:25.959704
11	2	11	2023-06-07 13:24:49.292066	2023-06-07 13:24:49.331069	2023-06-07 13:24:49.475074	FAILED	FAILED		2023-06-07 13:24:49.476074
12	2	12	2023-06-07 13:26:54.699316	2023-06-07 13:26:54.729315	2023-06-07 13:26:54.960322	COMPLETED	COMPLETED		2023-06-07 13:26:54.960322
\.


--
-- TOC entry 3402 (class 0 OID 41179)
-- Dependencies: 225
-- Data for Name: batch_job_execution_context; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.batch_job_execution_context (job_execution_id, short_context, serialized_context) FROM stdin;
1	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
2	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
3	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
4	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
5	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
6	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
7	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
8	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
9	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
10	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
11	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
12	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAANYmF0Y2gudmVyc2lvbnQABTUuMC4xeA==	\N
\.


--
-- TOC entry 3399 (class 0 OID 41145)
-- Dependencies: 222
-- Data for Name: batch_job_execution_params; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.batch_job_execution_params (job_execution_id, parameter_name, parameter_type, parameter_value, identifying) FROM stdin;
1	startAt	java.lang.Long	1686106575005	Y
2	startAt	java.lang.Long	1686106630187	Y
3	startAt	java.lang.Long	1686106736853	Y
4	startAt	java.lang.Long	1686107111232	Y
5	startAt	java.lang.Long	1686107309226	Y
6	startAt	java.lang.Long	1686107425352	Y
7	startAt	java.lang.Long	1686107643362	Y
8	startAt	java.lang.Long	1686107785495	Y
9	startAt	java.lang.Long	1686108354537	Y
10	startAt	java.lang.Long	1686110365687	Y
11	startAt	java.lang.Long	1686119089265	Y
12	startAt	java.lang.Long	1686119214674	Y
\.


--
-- TOC entry 3397 (class 0 OID 41126)
-- Dependencies: 220
-- Data for Name: batch_job_instance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.batch_job_instance (job_instance_id, version, job_name, job_key) FROM stdin;
1	0	postToCsvJob	b1c45d710907ff0a26e3a12fd64a4b78
2	0	postToCsvJob	5614eda12ad272d5ebd69f6a8ba85e31
3	0	postToCsvJob	69ebe06c8d74f74d78fc709e597b5908
4	0	postToCsvJob	efda0eee454f340058f726124acef09a
5	0	postToCsvJob	042d19a287addc617e38a4ce90122a8b
6	0	postToCsvJob	c5361571992145b79434e9ede6ef2d9a
7	0	postToCsvJob	d9370dbbf86a9f0c199ea36bdffe5b91
8	0	postToCsvJob	312bbf3611d7714cd3e2b7a54c6b39bf
9	0	postToCsvJob	f2d651f22b93c2e2c9ccf99475cb8d76
10	0	postToCsvJob	a6609235b5004c586c6a6ec739956d3b
11	0	postToCsvJob	4b3f5030c2ac2cecf082096257ac948a
12	0	postToCsvJob	a407d11645444d812219560a4838cf1a
\.


--
-- TOC entry 3400 (class 0 OID 41155)
-- Dependencies: 223
-- Data for Name: batch_step_execution; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.batch_step_execution (step_execution_id, version, step_name, job_execution_id, create_time, start_time, end_time, status, commit_count, read_count, filter_count, write_count, read_skip_count, write_skip_count, process_skip_count, rollback_count, exit_code, exit_message, last_updated) FROM stdin;
9	3	post-to-csv	9	2023-06-07 10:25:54.55694	2023-06-07 10:25:54.560972	2023-06-07 10:25:54.58294	COMPLETED	1	3	0	3	0	0	0	0	COMPLETED		2023-06-07 10:25:54.58294
1	3	post-to-csv	1	2023-06-07 09:56:15.07014	2023-06-07 09:56:15.076141	2023-06-07 09:56:15.23414	COMPLETED	1	3	3	0	0	0	0	0	COMPLETED		2023-06-07 09:56:15.235143
2	3	post-to-csv	2	2023-06-07 09:57:10.200433	2023-06-07 09:57:10.203432	2023-06-07 09:57:20.798378	COMPLETED	1	3	3	0	0	0	0	0	COMPLETED		2023-06-07 09:57:20.798378
10	3	post-to-csv	10	2023-06-07 10:59:25.758701	2023-06-07 10:59:25.763701	2023-06-07 10:59:25.952702	COMPLETED	1	3	0	3	0	0	0	0	COMPLETED		2023-06-07 10:59:25.952702
3	3	post-to-csv	3	2023-06-07 09:59:02.31294	2023-06-07 09:59:02.317939	2023-06-07 09:59:02.379939	COMPLETED	1	3	3	0	0	0	0	0	COMPLETED		2023-06-07 09:59:02.380962
11	2	post-to-csv	11	2023-06-07 13:24:49.342068	2023-06-07 13:24:49.346068	2023-06-07 13:24:49.466091	FAILED	0	3	0	0	0	0	0	1	FAILED	org.springframework.beans.NotReadablePropertyException: Invalid property 'creatorId' of bean class [com.example.postservice.dto.PostDTO]: Bean property 'creatorId' is not readable or has an invalid getter method: Does the return type of the getter match the parameter type of the setter?\r\n\tat org.springframework.beans.AbstractNestablePropertyAccessor.getPropertyValue(AbstractNestablePropertyAccessor.java:626)\r\n\tat org.springframework.beans.AbstractNestablePropertyAccessor.getPropertyValue(AbstractNestablePropertyAccessor.java:616)\r\n\tat org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor.extract(BeanWrapperFieldExtractor.java:56)\r\n\tat org.springframework.batch.item.file.transform.ExtractorLineAggregator.aggregate(ExtractorLineAggregator.java:51)\r\n\tat org.springframework.batch.item.file.FlatFileItemWriter.doWrite(FlatFileItemWriter.java:78)\r\n\tat org.springframework.batch.item.support.AbstractFileItemWriter.write(AbstractFileItemWriter.java:235)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.writeItems(SimpleChunkProcessor.java:203)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.doWrite(SimpleChunkProcessor.java:170)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.write(SimpleChunkProcessor.java:297)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.process(SimpleChunkProcessor.java:227)\r\n\tat org.springframework.batch.core.step.item.ChunkOrientedTasklet.execute(ChunkOrientedTasklet.java:75)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:389)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:313)\r\n\tat org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep$2.doInChunkContext(TaskletStep.java:256)\r\n\tat org.springframework.batch.core.scope.context.StepContextRepeatCallback.doInIteration(StepContextRepeatCallback.java:82)\r\n\tat org.springframework.batch.repeat.support.RepeatTemplate.getNextResult(RepeatTemplate.java:362)\r\n\tat org.springframework.batch.repeat.support.RepeatTemplate.executeInternal(RepeatTemplate.java:206)\r\n\tat org.springframework.batch.repeat.support.RepeatTemplate.iterate(RepeatTemplate.java:139)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep.doExecute(TaskletStep.java:241)\r\n\tat org.springframework.ba	2023-06-07 13:24:49.467072
4	3	post-to-csv	4	2023-06-07 10:05:26.706875	2023-06-07 10:05:26.711844	2023-06-07 10:05:26.762865	COMPLETED	1	3	3	0	0	0	0	0	COMPLETED		2023-06-07 10:05:26.763876
5	3	post-to-csv	5	2023-06-07 10:08:30.366427	2023-06-07 10:08:30.368423	2023-06-07 10:08:30.387421	COMPLETED	1	3	3	0	0	0	0	0	COMPLETED		2023-06-07 10:08:30.387421
6	2	post-to-csv	6	2023-06-07 10:10:28.262216	2023-06-07 10:10:28.266216	2023-06-07 10:10:28.537216	FAILED	0	3	0	0	0	0	0	1	FAILED	org.springframework.beans.NotReadablePropertyException: Invalid property 'Post ID' of bean class [com.example.postservice.entity.Post]: Bean property 'Post ID' is not readable or has an invalid getter method: Does the return type of the getter match the parameter type of the setter?\r\n\tat org.springframework.beans.AbstractNestablePropertyAccessor.getPropertyValue(AbstractNestablePropertyAccessor.java:626)\r\n\tat org.springframework.beans.AbstractNestablePropertyAccessor.getPropertyValue(AbstractNestablePropertyAccessor.java:616)\r\n\tat org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor.extract(BeanWrapperFieldExtractor.java:56)\r\n\tat org.springframework.batch.item.file.transform.ExtractorLineAggregator.aggregate(ExtractorLineAggregator.java:51)\r\n\tat org.springframework.batch.item.file.FlatFileItemWriter.doWrite(FlatFileItemWriter.java:78)\r\n\tat org.springframework.batch.item.support.AbstractFileItemWriter.write(AbstractFileItemWriter.java:235)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.writeItems(SimpleChunkProcessor.java:203)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.doWrite(SimpleChunkProcessor.java:170)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.write(SimpleChunkProcessor.java:297)\r\n\tat org.springframework.batch.core.step.item.SimpleChunkProcessor.process(SimpleChunkProcessor.java:227)\r\n\tat org.springframework.batch.core.step.item.ChunkOrientedTasklet.execute(ChunkOrientedTasklet.java:75)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:389)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep$ChunkTransactionCallback.doInTransaction(TaskletStep.java:313)\r\n\tat org.springframework.transaction.support.TransactionTemplate.execute(TransactionTemplate.java:140)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep$2.doInChunkContext(TaskletStep.java:256)\r\n\tat org.springframework.batch.core.scope.context.StepContextRepeatCallback.doInIteration(StepContextRepeatCallback.java:82)\r\n\tat org.springframework.batch.repeat.support.RepeatTemplate.getNextResult(RepeatTemplate.java:362)\r\n\tat org.springframework.batch.repeat.support.RepeatTemplate.executeInternal(RepeatTemplate.java:206)\r\n\tat org.springframework.batch.repeat.support.RepeatTemplate.iterate(RepeatTemplate.java:139)\r\n\tat org.springframework.batch.core.step.tasklet.TaskletStep.doExecute(TaskletStep.java:241)\r\n\tat org.springframework.batch.	2023-06-07 10:10:28.539217
12	3	post-to-csv	12	2023-06-07 13:26:54.742315	2023-06-07 13:26:54.746314	2023-06-07 13:26:54.94632	COMPLETED	1	3	0	3	0	0	0	0	COMPLETED		2023-06-07 13:26:54.947323
7	2	post-to-csv	7	2023-06-07 10:14:03.428894	2023-06-07 10:14:03.433893	2023-06-07 10:14:03.706894	FAILED	0	3	0	0	0	0	0	1	FAILED	java.lang.StackOverflowError\r\n\tat java.base/java.lang.Long.toString(Long.java:490)\r\n\tat java.base/java.lang.Long.toString(Long.java:1416)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Post.toString(Post.java:16)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Comment.toString(Comment.java:13)\r\n\tat java.base/java.lang.String.valueOf(String.java:4218)\r\n\tat java.base/java.lang.StringBuilder.append(StringBuilder.java:173)\r\n\tat java.base/java.util.AbstractCollection.toString(AbstractCollection.java:457)\r\n\tat org.hibernate.collection.spi.PersistentBag.toString(PersistentBag.java:585)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Post.toString(Post.java:16)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Comment.toString(Comment.java:13)\r\n\tat java.base/java.lang.String.valueOf(String.java:4218)\r\n\tat java.base/java.lang.StringBuilder.append(StringBuilder.java:173)\r\n\tat java.base/java.util.AbstractCollection.toString(AbstractCollection.java:457)\r\n\tat org.hibernate.collection.spi.PersistentBag.toString(PersistentBag.java:585)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Post.toString(Post.java:16)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Comment.toString(Comment.java:13)\r\n\tat java.base/java.lang.String.valueOf(String.java:4218)\r\n\tat java.base/java.lang.StringBuilder.append(StringBuilder.java:173)\r\n\tat java.base/java.util.AbstractCollection.toString(AbstractCollection.java:457)\r\n\tat org.hibernate.collection.spi.PersistentBag.toString(PersistentBag.java:585)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Post.toString(Post.java:16)\r\n\tat java.base/java.lang.StringConcatHelper.stringOf(StringConcatHelper.java:453)\r\n\tat com.example.postservice.entity.Comment.toString(Comment.java:13)\r\n\tat java.base/java.lang.String.valueOf(String.java:4218)\r\n\tat java.base/java.lang.StringBuilder.append(StringBuilder.java:173)\r\n\tat java.base/java.util.AbstractCollection.toString(AbstractCollection.java:457)\r\n\tat org.hibernate.collection.spi.PersistentBag.toString(PersistentBag.java:585)\r\n\tat j	2023-06-07 10:14:03.707894
8	3	post-to-csv	8	2023-06-07 10:16:25.586059	2023-06-07 10:16:25.591059	2023-06-07 10:16:25.748063	COMPLETED	1	3	0	3	0	0	0	0	COMPLETED		2023-06-07 10:16:25.749063
\.


--
-- TOC entry 3401 (class 0 OID 41167)
-- Dependencies: 224
-- Data for Name: batch_step_execution_context; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.batch_step_execution_context (step_execution_id, short_context, serialized_context) FROM stdin;
6	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAAdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
4	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
1	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
2	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
10	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAmx0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnNxAH4ABwAAAAAAAAADdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
5	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
3	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
7	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAAdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAAB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnEAfgAIdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
8	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAgF0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnNxAH4ABwAAAAAAAAADdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
9	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAmx0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnNxAH4ABwAAAAAAAAADdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
11	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAAdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAGt0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnNxAH4ABwAAAAAAAAAAdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
12	rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAAGdAAfUmVwb3NpdG9yeUl0ZW1SZWFkZXIucmVhZC5jb3VudHNyABFqYXZhLmxhbmcuSW50ZWdlchLioKT3gYc4AgABSQAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAEdAAgRmxhdEZpbGVJdGVtV3JpdGVyLmN1cnJlbnQuY291bnRzcgAOamF2YS5sYW5nLkxvbmc7i+SQzI8j3wIAAUoABXZhbHVleHEAfgAEAAAAAAAAAmB0ABpGbGF0RmlsZUl0ZW1Xcml0ZXIud3JpdHRlbnNxAH4ABwAAAAAAAAADdAARYmF0Y2gudGFza2xldFR5cGV0AD1vcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC5pdGVtLkNodW5rT3JpZW50ZWRUYXNrbGV0dAANYmF0Y2gudmVyc2lvbnQABTUuMC4xdAAOYmF0Y2guc3RlcFR5cGV0ADdvcmcuc3ByaW5nZnJhbWV3b3JrLmJhdGNoLmNvcmUuc3RlcC50YXNrbGV0LlRhc2tsZXRTdGVweA==	\N
\.


--
-- TOC entry 3406 (class 0 OID 41194)
-- Dependencies: 229
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.flyway_schema_history (installed_rank, version, description, type, script, checksum, installed_by, installed_on, execution_time, success) FROM stdin;
1	1	<< Flyway Baseline >>	BASELINE	<< Flyway Baseline >>	\N	postgres	2023-06-08 09:12:46.063513	0	t
\.


--
-- TOC entry 3394 (class 0 OID 32885)
-- Dependencies: 217
-- Data for Name: tbl_comment; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_comment (comment_id, content, create_time, update_time, user_id, post_id) FROM stdin;
1	Toi di comment dao	2023-05-24 15:55:02.417	2023-05-24 16:52:35.538	15	8
2	ok ok	2023-05-24 17:10:31.511	2023-05-24 17:10:31.511	15	8
3	Di comment dao	2023-05-26 09:30:40.512	2023-05-26 09:30:40.512	15	9
16	Di comment dao	2023-06-02 11:23:28.064	2023-06-02 11:23:28.064	15	14
\.


--
-- TOC entry 3396 (class 0 OID 32924)
-- Dependencies: 219
-- Data for Name: tbl_comment_reaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_comment_reaction (user_id, reacted_time, reaction, comment_id) FROM stdin;
16	2023-05-29 10:43:45.775	LIKE	1
16	2023-06-02 11:24:48.969	HAHA	16
\.


--
-- TOC entry 3392 (class 0 OID 32853)
-- Dependencies: 215
-- Data for Name: tbl_post; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_post (post_id, attachment_url, creator_id, status_content, created_time, updated_datetime) FROM stdin;
8	http://res.cloudinary.com/minh2212/image/upload/v1684831014/socialmedia/post/w4ew5td4omkuqxppii7j.webp	15	dang bai so 2	2023-05-23 15:36:54.603	2023-05-23 15:36:54.603
14	http://res.cloudinary.com/minh2212/image/upload/v1685679111/socialmedia/post/xae4hzwc3ahpzpevol79.jpg	19	hoa huong duong	2023-06-02 11:11:52.278	2023-06-02 11:11:52.278
9	http://res.cloudinary.com/minh2212/image/upload/v1685068164/socialmedia/post/evmzezmnuxmgeptu74eg.jpg	17	dang bai 123	2023-06-05 09:29:25.502	2023-06-05 09:29:25.502
\.


--
-- TOC entry 3393 (class 0 OID 32860)
-- Dependencies: 216
-- Data for Name: tbl_post_reaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_post_reaction (user_id, reaction, post_id, reacted_time) FROM stdin;
15	LIKE	9	2023-05-26 15:32:08.619
16	HAHA	8	2023-05-29 10:20:16.972
18	LIKE	14	2023-06-02 11:24:05.662
18	LIKE	9	2023-06-05 13:35:10.155
\.


--
-- TOC entry 3412 (class 0 OID 0)
-- Dependencies: 227
-- Name: batch_job_execution_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.batch_job_execution_seq', 12, true);


--
-- TOC entry 3413 (class 0 OID 0)
-- Dependencies: 228
-- Name: batch_job_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.batch_job_seq', 12, true);


--
-- TOC entry 3414 (class 0 OID 0)
-- Dependencies: 226
-- Name: batch_step_execution_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.batch_step_execution_seq', 12, true);


--
-- TOC entry 3415 (class 0 OID 0)
-- Dependencies: 218
-- Name: comment_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.comment_sequence', 16, true);


--
-- TOC entry 3416 (class 0 OID 0)
-- Dependencies: 214
-- Name: post_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.post_sequence', 14, true);


--
-- TOC entry 3237 (class 2606 OID 41185)
-- Name: batch_job_execution_context batch_job_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_execution_context
    ADD CONSTRAINT batch_job_execution_context_pkey PRIMARY KEY (job_execution_id);


--
-- TOC entry 3231 (class 2606 OID 41139)
-- Name: batch_job_execution batch_job_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_execution
    ADD CONSTRAINT batch_job_execution_pkey PRIMARY KEY (job_execution_id);


--
-- TOC entry 3227 (class 2606 OID 41130)
-- Name: batch_job_instance batch_job_instance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_instance
    ADD CONSTRAINT batch_job_instance_pkey PRIMARY KEY (job_instance_id);


--
-- TOC entry 3235 (class 2606 OID 41173)
-- Name: batch_step_execution_context batch_step_execution_context_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_step_execution_context
    ADD CONSTRAINT batch_step_execution_context_pkey PRIMARY KEY (step_execution_id);


--
-- TOC entry 3233 (class 2606 OID 41161)
-- Name: batch_step_execution batch_step_execution_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_step_execution
    ADD CONSTRAINT batch_step_execution_pkey PRIMARY KEY (step_execution_id);


--
-- TOC entry 3239 (class 2606 OID 41201)
-- Name: flyway_schema_history flyway_schema_history_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.flyway_schema_history
    ADD CONSTRAINT flyway_schema_history_pk PRIMARY KEY (installed_rank);


--
-- TOC entry 3229 (class 2606 OID 41132)
-- Name: batch_job_instance job_inst_un; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_instance
    ADD CONSTRAINT job_inst_un UNIQUE (job_name, job_key);


--
-- TOC entry 3223 (class 2606 OID 32889)
-- Name: tbl_comment tbl_comment_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_comment
    ADD CONSTRAINT tbl_comment_pkey PRIMARY KEY (comment_id);


--
-- TOC entry 3225 (class 2606 OID 32928)
-- Name: tbl_comment_reaction tbl_comment_reaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_comment_reaction
    ADD CONSTRAINT tbl_comment_reaction_pkey PRIMARY KEY (comment_id, user_id);


--
-- TOC entry 3219 (class 2606 OID 32859)
-- Name: tbl_post tbl_post_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_post
    ADD CONSTRAINT tbl_post_pkey PRIMARY KEY (post_id);


--
-- TOC entry 3221 (class 2606 OID 32864)
-- Name: tbl_post_reaction tbl_post_reaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_post_reaction
    ADD CONSTRAINT tbl_post_reaction_pkey PRIMARY KEY (post_id, user_id);


--
-- TOC entry 3240 (class 1259 OID 41202)
-- Name: flyway_schema_history_s_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX flyway_schema_history_s_idx ON public.flyway_schema_history USING btree (success);


--
-- TOC entry 3242 (class 2606 OID 32891)
-- Name: tbl_comment fki7k73l5d2j9cvam2bkepym80k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_comment
    ADD CONSTRAINT fki7k73l5d2j9cvam2bkepym80k FOREIGN KEY (post_id) REFERENCES public.tbl_post(post_id);


--
-- TOC entry 3241 (class 2606 OID 32865)
-- Name: tbl_post_reaction fkipjga85ygv84ybp2rtwj6o88q; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_post_reaction
    ADD CONSTRAINT fkipjga85ygv84ybp2rtwj6o88q FOREIGN KEY (post_id) REFERENCES public.tbl_post(post_id);


--
-- TOC entry 3243 (class 2606 OID 32929)
-- Name: tbl_comment_reaction fklpge5k0ulsh8gt7droy0ymbw2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_comment_reaction
    ADD CONSTRAINT fklpge5k0ulsh8gt7droy0ymbw2 FOREIGN KEY (comment_id) REFERENCES public.tbl_comment(comment_id);


--
-- TOC entry 3248 (class 2606 OID 41186)
-- Name: batch_job_execution_context job_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_execution_context
    ADD CONSTRAINT job_exec_ctx_fk FOREIGN KEY (job_execution_id) REFERENCES public.batch_job_execution(job_execution_id);


--
-- TOC entry 3245 (class 2606 OID 41150)
-- Name: batch_job_execution_params job_exec_params_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_execution_params
    ADD CONSTRAINT job_exec_params_fk FOREIGN KEY (job_execution_id) REFERENCES public.batch_job_execution(job_execution_id);


--
-- TOC entry 3246 (class 2606 OID 41162)
-- Name: batch_step_execution job_exec_step_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_step_execution
    ADD CONSTRAINT job_exec_step_fk FOREIGN KEY (job_execution_id) REFERENCES public.batch_job_execution(job_execution_id);


--
-- TOC entry 3244 (class 2606 OID 41140)
-- Name: batch_job_execution job_inst_exec_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_job_execution
    ADD CONSTRAINT job_inst_exec_fk FOREIGN KEY (job_instance_id) REFERENCES public.batch_job_instance(job_instance_id);


--
-- TOC entry 3247 (class 2606 OID 41174)
-- Name: batch_step_execution_context step_exec_ctx_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.batch_step_execution_context
    ADD CONSTRAINT step_exec_ctx_fk FOREIGN KEY (step_execution_id) REFERENCES public.batch_step_execution(step_execution_id);


-- Completed on 2023-06-08 14:22:42

--
-- PostgreSQL database dump complete
--

