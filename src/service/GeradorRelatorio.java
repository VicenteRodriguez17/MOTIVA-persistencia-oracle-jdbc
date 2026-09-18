package service;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.RelatorioPrioridadeDAO;
import dao.TrechoRodoviaDAO;
import model.IntervencaoOperacional;
import model.MonitoravelViaIoT;
import model.Pulverizacao;
import model.RocadaManual;
import model.RocadaMecanizada;
import model.TrechoRodovia;

public class GeradorRelatorio {

    private final TrechoRodoviaDAO trechoDAO = new TrechoRodoviaDAO();
    private final IntervencaoOperacionalDAO intervencaoDAO = new IntervencaoOperacionalDAO();
    private final EquipeManutencaoDAO equipeDAO = new EquipeManutencaoDAO();
    private final RelatorioPrioridadeDAO relatorioDAO = new RelatorioPrioridadeDAO();

    public void gerarRelatorio(TrechoRodovia[] trechos) {
        System.out.println("========================================");
        System.out.println("   RELATÓRIO DE PRIORIDADE DE ROÇADA   ");
        System.out.println("========================================");

        int qtUrgente = 0;
        int qtCritico = 0;
        int qtAtencao = 0;
        int qtNormal = 0;
        StringBuilder resumo = new StringBuilder();

        for (TrechoRodovia trecho : trechos) {

            // Atualiza sensor IoT se o trecho tiver a tecnologia
            if (trecho instanceof MonitoravelViaIoT) {
                ((MonitoravelViaIoT) trecho).transmitirDadosSensor();
            }

            // 1. Garante que o trecho está persistido antes de vincular intervenções a ele
            if (trecho.getId() == null) {
                trechoDAO.inserir(trecho);
            }

            System.out.println("----------------------------------------");
            System.out.println("Trecho: KM " + trecho.getKmInicial() + " até KM " + trecho.getKmFinal());
            System.out.println("Tipo: " + trecho.getTipo());
            System.out.printf("Altura da vegetação: %.2f m%n", trecho.getAlturaVegetacao());

            if (trecho.precisaRocada()) {
                IntervencaoOperacional intervencao;
                String nivel;

                if (trecho.getAlturaVegetacao() >= 0.8) {
                    nivel = "URGENTE";
                    System.out.println("PRIORIDADE URGENTE - Roçada Mecanizada necessária!");
                    intervencao = new RocadaMecanizada("Equipe A", "Urgente", "Trator");
                    qtUrgente++;
                } else if (trecho.getAlturaVegetacao() >= 0.6) {
                    nivel = "CRÍTICO";
                    System.out.println("PRIORIDADE CRÍTICA - Roçada Manual necessária!");
                    intervencao = new RocadaManual("Equipe C", "Esta semana", "Roçadeira costal");
                    qtCritico++;
                } else {
                    nivel = "ATENÇÃO";
                    System.out.println("PRIORIDADE DE ATENÇÃO - Pulverização necessária!");
                    intervencao = new Pulverizacao("Equipe B", "Este mês", "Herbicida");
                    qtAtencao++;
                }

                intervencao.executarServico();

                // 2. Vincula a uma equipe já cadastrada (se existir) e persiste a intervenção
                EquipeManutencaoDAO.EquipeRecord equipeRec = equipeDAO.buscarPorNome(intervencao.getResponsavel());
                Integer equipeId = (equipeRec != null) ? equipeRec.id() : null;
                intervencaoDAO.inserir(intervencao, trecho.getId(), equipeId);

                resumo.append(String.format("KM %d-%d: %s (%s)%n",
                        trecho.getKmInicial(), trecho.getKmFinal(), nivel, intervencao.getTipo()));
            } else {
                System.out.println("OK - Sem necessidade de intervenção.");
                qtNormal++;
            }
        }

        System.out.println("========================================");
        System.out.println("         FIM DO RELATÓRIO               ");
        System.out.println("========================================");

        // 3. NOVO: salva o histórico do relatório no banco
        relatorioDAO.salvarRelatorio(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo.toString());
    }
}
