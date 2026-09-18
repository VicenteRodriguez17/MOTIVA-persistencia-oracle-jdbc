-- ============================================================
-- MOTIVA - Sprint 3
-- Script de criação das tabelas (Oracle)
-- Baseado nas classes: EquipeManutencao, TrechoRodovia
-- (TrechoUmido/TrechoSeco), IntervencaoOperacional
-- (RocadaMecanizada/RocadaManual/Pulverizacao) e RelatorioPrioridade
-- ============================================================

-- Descomente as linhas abaixo apenas se precisar recriar tudo do zero
-- DROP TABLE INTERVENCAO_OPERACIONAL CASCADE CONSTRAINTS;
-- DROP TABLE RELATORIO_PRIORIDADE CASCADE CONSTRAINTS;
-- DROP TABLE TRECHO_RODOVIA CASCADE CONSTRAINTS;
-- DROP TABLE EQUIPE_MANUTENCAO CASCADE CONSTRAINTS;

CREATE TABLE EQUIPE_MANUTENCAO (
    ID                 NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOME_EQUIPE        VARCHAR2(100) NOT NULL,
    QTD_INTEGRANTES    NUMBER(3) NOT NULL
);

CREATE TABLE TRECHO_RODOVIA (
    ID                 NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    KM_INICIAL         NUMBER(6) NOT NULL,
    KM_FINAL           NUMBER(6) NOT NULL,
    ALTURA_VEGETACAO   NUMBER(5,2) NOT NULL,
    TIPO               VARCHAR2(20) NOT NULL, -- 'TrechoUmido' ou 'TrechoSeco'
    CONSTRAINT CK_TRECHO_TIPO CHECK (TIPO IN ('TrechoUmido', 'TrechoSeco'))
);

CREATE TABLE INTERVENCAO_OPERACIONAL (
    ID                 NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    RESPONSAVEL        VARCHAR2(100) NOT NULL,
    DATA_AGENDAMENTO   VARCHAR2(50) NOT NULL,
    TIPO               VARCHAR2(20) NOT NULL, -- 'RocadaMecanizada', 'RocadaManual' ou 'Pulverizacao'
    DETALHE            VARCHAR2(100),
    TRECHO_ID          NUMBER NOT NULL,
    EQUIPE_ID          NUMBER,
    CONSTRAINT CK_INTERVENCAO_TIPO CHECK (TIPO IN ('RocadaMecanizada', 'RocadaManual', 'Pulverizacao')),
    CONSTRAINT FK_INTERVENCAO_TRECHO FOREIGN KEY (TRECHO_ID) REFERENCES TRECHO_RODOVIA (ID),
    CONSTRAINT FK_INTERVENCAO_EQUIPE FOREIGN KEY (EQUIPE_ID) REFERENCES EQUIPE_MANUTENCAO (ID)
);

CREATE TABLE RELATORIO_PRIORIDADE (
    ID                 NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    DATA_GERACAO       TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    QT_URGENTE         NUMBER(4) DEFAULT 0 NOT NULL,
    QT_CRITICO         NUMBER(4) DEFAULT 0 NOT NULL,
    QT_ATENCAO         NUMBER(4) DEFAULT 0 NOT NULL,
    QT_NORMAL          NUMBER(4) DEFAULT 0 NOT NULL,
    RESUMO             VARCHAR2(4000)
);

COMMIT;

-- ------------------------------------------------------------
-- OBS: "GENERATED ALWAYS AS IDENTITY" exige Oracle 12c ou mais
-- recente (a maioria dos laboratórios usa Oracle 19c/21c XE).
-- Se o Oracle do seu laboratório for mais antigo (11g) e essa
-- sintaxe der erro, troque cada "GENERATED ALWAYS AS IDENTITY"
-- por "NUMBER" e crie, para cada tabela, uma SEQUENCE + um
-- TRIGGER "BEFORE INSERT" que preencha o ID a partir da
-- sequência (padrão clássico de auto-incremento no Oracle).
-- ------------------------------------------------------------
