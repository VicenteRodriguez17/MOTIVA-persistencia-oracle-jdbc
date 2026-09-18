package model;

public class RocadaManual extends IntervencaoOperacional {

    private String ferramentaUtilizada;

    public RocadaManual(String responsavel, String dataAgendamento, String ferramentaUtilizada) {
        super(responsavel, dataAgendamento);
        this.ferramentaUtilizada = ferramentaUtilizada;
    }

    @Override
    public void executarServico() {
        System.out.println("Executando roçada manual com ferramenta: " + ferramentaUtilizada);
        exibirDetalhes();
    }

    @Override
    public String getDetalhe() {
        return ferramentaUtilizada;
    }

    public String getFerramentaUtilizada() {
        return ferramentaUtilizada;
    }
}
