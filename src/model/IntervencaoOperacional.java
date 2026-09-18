package model;

public abstract class IntervencaoOperacional {

    private Integer id;
    private String responsavel;
    private String dataAgendamento;

    public IntervencaoOperacional(String responsavel, String dataAgendamento) {
        this.responsavel = responsavel;
        this.dataAgendamento = dataAgendamento;
    }

    public abstract void executarServico();

    /**
     * Retorna o detalhe específico de cada tipo de intervenção
     * (equipamento, ferramenta ou produto utilizado), usado para
     * persistência no banco de dados.
     */
    public abstract String getDetalhe();

    public void exibirDetalhes() {
        System.out.println("Responsável: " + responsavel);
        System.out.println("Data agendada: " + dataAgendamento);
    }

    public String getTipo() {
        return this.getClass().getSimpleName();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public String getDataAgendamento() {
        return dataAgendamento;
    }
}
