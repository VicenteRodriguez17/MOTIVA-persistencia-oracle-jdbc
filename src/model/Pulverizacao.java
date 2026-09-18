package model;

public class Pulverizacao extends IntervencaoOperacional {

    private String tipoProduto;

    public Pulverizacao(String responsavel, String dataAgendamento, String tipoProduto) {
        super(responsavel, dataAgendamento);
        this.tipoProduto = tipoProduto;
    }

    @Override
    public void executarServico() {
        System.out.println("Executando pulverização com produto: " + tipoProduto);
        exibirDetalhes();
    }

    @Override
    public String getDetalhe() {
        return tipoProduto;
    }

    public String getTipoProduto() {
        return tipoProduto;
    }
}
