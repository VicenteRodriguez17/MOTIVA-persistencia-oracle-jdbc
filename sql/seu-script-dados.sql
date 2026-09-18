-- ============================================================
-- MOTIVA - Sprint 3
-- Script de dados de teste (Oracle)
-- Execute DEPOIS do seu-script-criacao.sql
-- ============================================================

INSERT INTO EQUIPE_MANUTENCAO (NOME_EQUIPE, QTD_INTEGRANTES) VALUES ('Equipe A', 4);
INSERT INTO EQUIPE_MANUTENCAO (NOME_EQUIPE, QTD_INTEGRANTES) VALUES ('Equipe B', 3);
INSERT INTO EQUIPE_MANUTENCAO (NOME_EQUIPE, QTD_INTEGRANTES) VALUES ('Equipe C', 5);

INSERT INTO TRECHO_RODOVIA (KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO, TIPO) VALUES (0, 10, 0.45, 'TrechoUmido');
INSERT INTO TRECHO_RODOVIA (KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO, TIPO) VALUES (10, 20, 0.25, 'TrechoSeco');
INSERT INTO TRECHO_RODOVIA (KM_INICIAL, KM_FINAL, ALTURA_VEGETACAO, TIPO) VALUES (20, 30, 1.05, 'TrechoUmido');

-- TRECHO_ID = 3 corresponde ao terceiro trecho inserido acima (KM 20-30, altura 1.05 -> urgente)
INSERT INTO INTERVENCAO_OPERACIONAL (RESPONSAVEL, DATA_AGENDAMENTO, TIPO, DETALHE, TRECHO_ID, EQUIPE_ID)
VALUES ('Equipe A', 'Urgente', 'RocadaMecanizada', 'Trator', 3, 1);

INSERT INTO RELATORIO_PRIORIDADE (QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO)
VALUES (1, 0, 1, 1, 'Relatorio de teste gerado manualmente para validar o script de dados.');

COMMIT;

-- Consultas rápidas para conferir se tudo entrou certo:
-- SELECT * FROM EQUIPE_MANUTENCAO;
-- SELECT * FROM TRECHO_RODOVIA;
-- SELECT * FROM INTERVENCAO_OPERACIONAL;
-- SELECT * FROM RELATORIO_PRIORIDADE;
