package model;

public class RocadaMecanizada extends IntervencaoOperacional {

    private String tipoEquipamento;

    public RocadaMecanizada(String responsavel, String dataAgendamento, String tipoEquipamento) {
        super(responsavel, dataAgendamento);
        this.tipoEquipamento = tipoEquipamento;
    }

    @Override
    public void executarServico() {
        System.out.println("Executando roçada mecanizada com equipamento: " + tipoEquipamento);
        exibirDetalhes();
    }

    @Override
    public String getDetalhe() {
        return tipoEquipamento;
    }

    public String getTipoEquipamento() {
        return tipoEquipamento;
    }
}
