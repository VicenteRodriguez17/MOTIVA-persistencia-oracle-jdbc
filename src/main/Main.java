package main;

import java.util.List;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.RelatorioPrioridadeDAO;
import dao.TrechoRodoviaDAO;
import db.ConexaoBD;
import model.EquipeManutencao;
import model.RocadaManual;
import model.TrechoRodovia;
import model.TrechoSeco;
import model.TrechoUmido;
import service.GeradorRelatorio;

public class Main {

    public static void main(String[] args) {

        // 1. Testar conexão
        ConexaoBD conexao = ConexaoBD.getInstancia();
        conexao.conectar();

        // 2. Testar CRUD de Equipe
        System.out.println("\n=== TESTE CRUD - EQUIPE DE MANUTENÇÃO ===");
        EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();

        EquipeManutencao equipeA = new EquipeManutencao("Equipe A", 4);
        EquipeManutencao equipeB = new EquipeManutencao("Equipe B", 3);
        EquipeManutencao equipeC = new EquipeManutencao("Equipe C", 5);
        daoEquipe.inserir(equipeA);
        daoEquipe.inserir(equipeB);
        daoEquipe.inserir(equipeC);

        System.out.println("Equipes cadastradas:");
        daoEquipe.listarTodas().forEach(System.out::println);

        System.out.println("Buscando equipe por ID (" + equipeA.getId() + "): "
                + daoEquipe.buscarPorId(equipeA.getId()));

        daoEquipe.atualizar(new EquipeManutencaoDAO.EquipeRecord(equipeA.getId(), "Equipe Alpha", 6));
        System.out.println("Equipe após atualizar(): " + daoEquipe.buscarPorId(equipeA.getId()));

        // 3. Testar Trechos
        System.out.println("\n=== TESTE CRUD - TRECHOS DE RODOVIA ===");
        TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();

        TrechoRodovia[] trechos = new TrechoRodovia[5];
        trechos[0] = new TrechoUmido(0, 10, 0.3);
        trechos[1] = new TrechoSeco(10, 20, 0.2);
        trechos[2] = new TrechoUmido(20, 30, 0.9);
        trechos[3] = new TrechoSeco(30, 40, 0.6);
        trechos[4] = new TrechoUmido(40, 50, 0.1);

        // Simula crescimento da vegetação e já persiste cada trecho
        for (TrechoRodovia trecho : trechos) {
            trecho.crescerVegetacao();
            daoTrecho.inserir(trecho);
        }

        System.out.println("Trechos cadastrados:");
        daoTrecho.listarTodas().forEach(System.out::println);

        System.out.println("Buscando trecho por ID (" + trechos[0].getId() + "): "
                + daoTrecho.buscarPorId(trechos[0].getId()));

        // 4. Testar Intervenções Operacionais (CRUD isolado, com limpeza no final)
        System.out.println("\n=== TESTE CRUD - INTERVENÇÕES OPERACIONAIS ===");
        IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();

        RocadaManual intervencaoTeste = new RocadaManual("Equipe C", "Teste", "Roçadeira costal");
        daoIntervencao.inserir(intervencaoTeste, trechos[0].getId(), equipeC.getId());
        System.out.println("Intervenção inserida: " + daoIntervencao.buscarPorId(intervencaoTeste.getId()));

        daoIntervencao.atualizar(new IntervencaoOperacionalDAO.IntervencaoRecord(
                intervencaoTeste.getId(), "Equipe C", "Atualizado", "RocadaManual",
                "Roçadeira costal", trechos[0].getId(), equipeC.getId()));
        System.out.println("Intervenção após atualizar(): " + daoIntervencao.buscarPorId(intervencaoTeste.getId()));

        daoIntervencao.deletar(intervencaoTeste.getId());
        System.out.println("Intervenção de teste removida (deletar()).");

        // 5. Gerar relatório com persistência
        System.out.println("\n=== GERAÇÃO DE RELATÓRIO (COM PERSISTÊNCIA) ===");
        GeradorRelatorio gerador = new GeradorRelatorio();
        gerador.gerarRelatorio(trechos);

        // 6. Consultar histórico de relatórios
        System.out.println("\n=== HISTÓRICO DE RELATÓRIOS SALVOS ===");
        RelatorioPrioridadeDAO daoRelatorio = new RelatorioPrioridadeDAO();
        List<RelatorioPrioridadeDAO.RelatorioRecord> historico = daoRelatorio.listarTodas();
        historico.forEach(System.out::println);

        // 7. Fechar conexão
        conexao.desconectar();
    }
}
