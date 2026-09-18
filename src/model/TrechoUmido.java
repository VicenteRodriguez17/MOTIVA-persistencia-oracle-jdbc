package model;

public class TrechoUmido extends TrechoRodovia implements MonitoravelViaIoT {

    public TrechoUmido(int kmInicial, int kmFinal, double alturaVegetacao) {
        super(kmInicial, kmFinal, alturaVegetacao);
    }

    @Override
    public void crescerVegetacao() {
        // Trecho úmido cresce mais rápido
        setAlturaVegetacao(getAlturaVegetacao() + 0.15);
    }

    @Override
    public void transmitirDadosSensor() {
        System.out.printf("[IoT] Trecho úmido KM %d - %d | Altura atual: %.2f m%n",
                getKmInicial(), getKmFinal(), getAlturaVegetacao());
    }
}
