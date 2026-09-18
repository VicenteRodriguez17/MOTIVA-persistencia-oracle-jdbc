package model;

public class EquipeManutencao {

    private Integer id;
    private String nomeEquipe;
    private int quantidadeIntegrantes;

    public EquipeManutencao(String nomeEquipe, int quantidadeIntegrantes) {
        this.nomeEquipe = nomeEquipe;
        this.quantidadeIntegrantes = quantidadeIntegrantes;
    }

    public EquipeManutencao(Integer id, String nomeEquipe, int quantidadeIntegrantes) {
        this.id = id;
        this.nomeEquipe = nomeEquipe;
        this.quantidadeIntegrantes = quantidadeIntegrantes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public int getQuantidadeIntegrantes() {
        return quantidadeIntegrantes;
    }

    public void atenderTrecho(TrechoRodovia trecho) {
        System.out.println("Equipe '" + nomeEquipe
                + "' foi designada para o trecho: "
                + trecho);
    }

    @Override
    public String toString() {
        return String.format("Equipe: %s | Integrantes: %d",
                nomeEquipe,
                quantidadeIntegrantes);
    }
}
