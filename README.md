# MOTIVA — Sprint 3 (Persistência com Oracle + JDBC)

Evolução do sistema MOTIVA (Sprints 1 e 2) adicionando persistência de
dados em Oracle usando JDBC puro (sem frameworks/ORM), conforme pedido
no enunciado da Sprint 3.

## O que foi feito

- Mantidas todas as classes de domínio já criadas nas Sprints 1 e 2
  (`TrechoRodovia`/`TrechoUmido`/`TrechoSeco`, `EquipeManutencao`,
  `IntervencaoOperacional`/`RocadaMecanizada`/`RocadaManual`/`Pulverizacao`,
  `MonitoravelViaIoT`), apenas acrescentando um campo `id` (usado pelo
  JDBC) e um método `getDetalhe()` em `IntervencaoOperacional` para
  facilitar a persistência do dado específico de cada subtipo.
- Criado o pacote `db` com `ConexaoBD` (Singleton).
- Criado o pacote `dao` com os 4 DAOs pedidos: `EquipeManutencaoDAO`,
  `TrechoRodoviaDAO`, `IntervencaoOperacionalDAO` e
  `RelatorioPrioridadeDAO`. Todos seguem o mesmo padrão: construtor
  padrão, métodos `inserir/buscarPorId/listarTodas/atualizar/deletar`,
  queries SQL como constantes, e um `record` interno representando a
  linha da tabela.
- `GeradorRelatorio` evoluído: continua imprimindo no console
  exatamente como antes e, ao final, salva o histórico do relatório no
  banco via `RelatorioPrioridadeDAO.salvarRelatorio(...)`, além de
  persistir cada trecho e cada intervenção gerada.
- `Main.java` demonstra: conexão, CRUD de equipe, CRUD de trechos, CRUD
  de intervenções, geração do relatório com persistência, consulta ao
  histórico de relatórios e o fechamento da conexão.

## Estrutura do projeto

```
MOTIVA-Sprint3/
├── src/
│   ├── db/        ConexaoBD.java
│   ├── dao/       EquipeManutencaoDAO, TrechoRodoviaDAO,
│   │               IntervencaoOperacionalDAO, RelatorioPrioridadeDAO
│   ├── model/     TrechoRodovia, TrechoUmido, TrechoSeco,
│   │               EquipeManutencao, IntervencaoOperacional,
│   │               RocadaMecanizada, RocadaManual, Pulverizacao,
│   │               MonitoravelViaIoT
│   ├── service/   GeradorRelatorio.java
│   └── main/      Main.java
├── sql/
│   ├── seu-script-criacao.sql
│   └── seu-script-dados.sql
├── lib/           (coloque aqui o ojdbc17.jar)
└── README.md
```

## 1. Configurar o banco Oracle

1. Acesse o Oracle do laboratório (SQL Developer, DBeaver, ou o
   terminal `sqlplus`).
2. Rode `sql/seu-script-criacao.sql` primeiro (cria as 4 tabelas).
3. Depois rode `sql/seu-script-dados.sql` (insere alguns dados de
   teste, independentes dos que o `Main.java` insere em tempo de
   execução).
4. Se o Oracle do laboratório for mais antigo que a versão 12c e a
   sintaxe `GENERATED ALWAYS AS IDENTITY` der erro, leia o comentário
   no final do `seu-script-criacao.sql` — ele explica como trocar para
   o padrão `SEQUENCE + TRIGGER`.

## 2. Configurar o driver JDBC

1. Baixe o `ojdbc17.jar` (compatível com Java 17+) no site da Oracle
   ou copie o que já está disponível no laboratório.
2. Coloque o arquivo dentro da pasta `lib/` deste projeto.
3. Se estiver usando Eclipse/IntelliJ: clique com o botão direito no
   projeto → Build Path / Project Structure → Add External JAR → aponte
   para `lib/ojdbc17.jar`.

## 3. Configurar a conexão

Abra `src/db/ConexaoBD.java` e ajuste as constantes no topo da classe
com os dados reais do seu laboratório:

```java
private static final String HOST = "host";
private static final String PORTA = "1521";
private static final String SERVICO = "servico";
private static final String USUARIO = "SEU_USUARIO";
private static final String SENHA = "SUA_SENHA";
```

## 4. Compilar e rodar pelo terminal (sem IDE)

A partir da pasta raiz do projeto (`MOTIVA-Sprint3/`):

```bash
# Compilar
javac -cp "lib/ojdbc17.jar" -d bin $(find src -name "*.java")

# Rodar
java -cp "bin:lib/ojdbc17.jar" main.Main
```

No Windows (PowerShell/cmd), troque o `:` por `;` no classpath do
comando `java`:

```bash
java -cp "bin;lib/ojdbc17.jar" main.Main
```

## 5. Rodar pelo Eclipse

1. Importe a pasta `src/` para dentro do seu projeto Eclipse (ou crie
   um projeto novo "Sprint3" e copie as pastas `db`, `dao`, `model`,
   `service`, `main` para dentro do `src`).
2. Adicione `lib/ojdbc17.jar` ao Build Path (veja passo 2).
3. Clique com o botão direito em `Main.java` → Run As → Java
   Application.

## 6. Subir para o GitHub

Como as Sprints 1 e 2 já têm cada uma seu próprio repositório Git,
siga o mesmo padrão para a Sprint 3. Rodando a partir da pasta
`MOTIVA-Sprint3/`:

```bash
git init
git add .
git commit -m "Sprint 3 - persistencia com Oracle e JDBC"
```

Depois crie um repositório vazio no GitHub (botão "New repository",
sem README/gitignore) e conecte:

```bash
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/Sprint03.git
git push -u origin main
```

Se preferir manter tudo no mesmo repositório das Sprints anteriores,
basta copiar as pastas `src/`, `sql/` e este `README.md` para dentro
do repositório já existente, e então:

```bash
git add .
git commit -m "Sprint 3 - persistencia com Oracle e JDBC"
git push
```

> Dica: antes do primeiro commit, crie um arquivo `.gitignore` com as
> linhas `bin/` e `lib/*.jar` para não versionar os `.class` compilados
> nem o driver Oracle (que não deve ser redistribuído publicamente).

## Checklist do enunciado

- [x] `seu-script-criacao.sql` com tabelas baseadas nas suas classes
- [x] `seu-script-dados.sql` com dados de teste
- [ ] Banco de dados configurado e script executado *(depende do seu
      Oracle — siga a seção 1)*
- [ ] `ConexaoBD.java` configurado com credenciais corretas *(ajuste a
      seção 3 com seus dados)*
- [x] DAOs criados para as 4 entidades
- [x] DAO de Relatório criado e integrado ao `GeradorRelatorio`
- [x] `GeradorRelatorio.java` evoluído para salvar histórico
- [x] `Main.java` demonstrando todas as operações
- [ ] Testes executados com sucesso *(rode o projeto com suas
      credenciais para confirmar)*
- [x] README com instruções de execução

## Problemas comuns

| Problema | Solução |
|---|---|
| `Driver not found` | Confirme se `ojdbc17.jar` está no classpath (pasta `lib/`) |
| `ORA-01017: invalid username/password` | Revise `USUARIO`/`SENHA` em `ConexaoBD` |
| `ORA-00942: table or view does not exist` | Rode `seu-script-criacao.sql` antes de tudo |
| `ORA-02292: integrity constraint violated` | Há registros dependentes (FK) — ex.: apagar um trecho que já tem intervenções vinculadas |
| `Connection closed` | Garanta que `conectar()` foi chamado antes de qualquer operação |
