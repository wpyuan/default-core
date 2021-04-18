create table CONFIG
(
    ID          VARCHAR2(300) not null
        constraint CONFIG_PK
        primary key,
    CODE        VARCHAR2(300) not null,
    DESCRIPTION VARCHAR2(1000),
    IS_ENABLE   NUMBER(1) default 1
)
    /

comment on table CONFIG is '系统配置'
/

comment on column CONFIG.CODE is '代码'
/

comment on column CONFIG.DESCRIPTION is '描述'
/

comment on column CONFIG.IS_ENABLE is '是否启用'
/

create unique index CONFIG_CODE_UINDEX
    on CONFIG (CODE)
/

