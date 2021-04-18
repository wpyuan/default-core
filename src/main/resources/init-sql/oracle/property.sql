create table PROPERTY
(
    ID          VARCHAR2(300) not null
        constraint PROPERTY_PK
        primary key,
    CONFIG_ID   VARCHAR2(300) not null,
    VALUE       VARCHAR2(300) not null,
    DESCRIPTION VARCHAR2(300),
    ORDER_SEQ   NUMBER(22) default 10,
    REMARK      VARCHAR2(1000),
    IS_ENABLE   NUMBER(1) default 1
) /

comment on table PROPERTY is ''属性值''
/

comment on column PROPERTY.CONFIG_ID is ''所属系统配置ID，取自CONFIG表.ID''
/

comment on column PROPERTY.VALUE is ''值''
/

comment on column PROPERTY.DESCRIPTION is ''描述''
/

comment on column PROPERTY.ORDER_SEQ is ''排序号''
/

comment on column PROPERTY.REMARK is ''备注''
/

comment on column PROPERTY.IS_ENABLE is ''是否启用''
/

create
unique index PROPERTY_VALUE_UINDEX
    on PROPERTY (VALUE)
/

