package it.unict.smarthospital.common.dto;

import java.io.Serializable;

public class StatisticheDTO implements Serializable {
    private String reparto;
    private int week_num;
    private int letti_disponibili;
    private int richieste_totali;
    private int richieste_accettate;
    private int soddisfazione_pazienti;

    public StatisticheDTO(String reparto, int week_num, int letti_disponibili, int richieste_totali, int richieste_accettate, int soddisfazione_pazienti) {
        this.reparto = reparto;
        this.week_num = week_num;
        this.letti_disponibili = letti_disponibili;
        this.richieste_totali = richieste_totali;
        this.richieste_accettate = richieste_accettate;
        this.soddisfazione_pazienti = soddisfazione_pazienti;
    }

    public String getReparto() { return reparto; }
    public void setReparto(String reparto) { this.reparto = reparto; }

    public int getWeek_num() { return week_num; }
    public void setWeek_num(int week_num) { this.week_num = week_num; }

    public int getLetti_disponibili() { return letti_disponibili; }
    public void setLetti_disponibili(int letti_disponibili) { this.letti_disponibili = letti_disponibili; }

    public int getRichieste_totali() { return richieste_totali; }
    public void setRichieste_totali(int richieste_totali) { this.richieste_totali = richieste_totali; }

    public int getRichieste_accettate() { return richieste_accettate; }
    public void setRichieste_accettate(int richieste_accettate) { this.richieste_accettate = richieste_accettate; }

    public int getSoddisfazione_pazienti() { return soddisfazione_pazienti; }
    public void setSoddisfazione_pazienti(int soddisfazione_pazienti) { this.soddisfazione_pazienti = soddisfazione_pazienti; }
}