package it.unict.smarthospital.common.dto;

import java.io.Serializable;

public class LiveStatsDTO implements Serializable {
    private String reparto;
    private int pazientiAttuali;
    private int capienzaMassima; 
    private double mediaSoddisfazione;

    public LiveStatsDTO(String reparto, int pazientiAttuali, int capienzaMassima, double mediaSoddisfazione) {
        this.reparto = reparto;
        this.pazientiAttuali = pazientiAttuali;
        this.capienzaMassima = capienzaMassima;
        this.mediaSoddisfazione = mediaSoddisfazione;
    }

    public String getReparto() { return reparto; }
    public int getPazientiAttuali() { return pazientiAttuali; }
    public int getCapienzaMassima() { return capienzaMassima; }
    public double getMediaSoddisfazione() { return mediaSoddisfazione; }
    
    public int getPercentualeOccupazione() {
        if (capienzaMassima == 0) return 0;
        return (pazientiAttuali * 100) / capienzaMassima;
    }
}