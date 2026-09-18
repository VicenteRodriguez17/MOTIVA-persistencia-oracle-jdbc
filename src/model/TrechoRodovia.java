package model;

public abstract class TrechoRodovia {

    private Integer id;
    private int kmInicial;
    private int kmFinal;
    private double alturaVegetacao;

    public TrechoRodovia(int kmInicial, int kmFinal, double alturaVegetacao) {
        this.kmInicial = kmInicial;
        this.kmFinal = kmFinal;
        this.alturaVegetacao = alturaVegetacao;
    }

    public abstract void crescerVegetacao();

    public boolean precisaRocada() {
        return alturaVegetacao >= 0.4;
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

    public int getKmInicial() {
        return kmInicial;
    }

    public int getKmFinal() {
        return kmFinal;
    }

    public double getAlturaVegetacao() {
        return alturaVegetacao;
    }

    public void setAlturaVegetacao(double alturaVegetacao) {
        this.alturaVegetacao = alturaVegetacao;
    }
}
