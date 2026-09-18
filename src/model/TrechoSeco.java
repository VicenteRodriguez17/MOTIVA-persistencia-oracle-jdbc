package model;

public class TrechoSeco extends TrechoRodovia {

    public TrechoSeco(int kmInicial, int kmFinal, double alturaVegetacao) {
        super(kmInicial, kmFinal, alturaVegetacao);
    }

    @Override
    public void crescerVegetacao() {
        // Trecho seco cresce mais devagar
        setAlturaVegetacao(getAlturaVegetacao() + 0.05);
    }
}
