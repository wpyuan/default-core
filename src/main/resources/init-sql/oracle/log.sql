create table TY_API_LOG
(
    ID                   NUMBER(22) not null
        constraint TY_API_LOG_PK
        primary key,
    API_CODE             VARCHAR2(300),
    API_DESC             VARCHAR2(300),
    URL                  VARCHAR2(300),
    METHOD               VARCHAR2(300),
    IP                   VARCHAR2(300),
    REQUEST_HEADERS      CLOB,
    REQUEST_QUERY        CLOB,
    REQUEST_BODY         CLOB,
    IS_SUCCESS           NUMBER(1),
    RESPONSE_CONTENT     CLOB,
    EXCEPTION_STACK      CLOB,
    CONSUME_TIME         NUMBER(22),
    IS_INNER             NUMBER(1),
    REQUEST_CONTENT_TYPE VARCHAR2(300),
    REQUEST_DATE         DATE,
    RESPONSE_CODE        VARCHAR2(300)
)
    /

comment on table TY_API_LOG is '系统统一接口日志'
/

comment on column TY_API_LOG.API_CODE is '接口代码'
/

comment on column TY_API_LOG.API_DESC is '接口描述'
/

comment on column TY_API_LOG.URL is '请求地址'
/

comment on column TY_API_LOG.METHOD is '请求方法'
/

comment on column TY_API_LOG.IP is '请求者IP地址'
/

comment on column TY_API_LOG.REQUEST_HEADERS is '请求头部信息'
/

comment on column TY_API_LOG.REQUEST_QUERY is '请求参数Query部分'
/

comment on column TY_API_LOG.REQUEST_BODY is '请求参数Body部分'
/

comment on column TY_API_LOG.IS_SUCCESS is '是否成功'
/

comment on column TY_API_LOG.RESPONSE_CONTENT is '返回内容'
/

comment on column TY_API_LOG.EXCEPTION_STACK is '异常堆栈信息'
/

comment on column TY_API_LOG.CONSUME_TIME is '耗时'
/

comment on column TY_API_LOG.IS_INNER is '是否系统内部接口，系统内部接口：true，外部接口：false'
/

comment on column TY_API_LOG.REQUEST_CONTENT_TYPE is '请求的ContentType'
/

comment on column TY_API_LOG.REQUEST_DATE is '请求时间'
/

comment on column TY_API_LOG.RESPONSE_CODE is '请求外部接口的返回状态码，当IS_INNER字段为false时有值'
/

