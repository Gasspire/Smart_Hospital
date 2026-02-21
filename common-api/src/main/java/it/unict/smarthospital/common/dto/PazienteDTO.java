package it.unict.smarthospital.common.dto;

import java.io.Serializable;
import java.time.Instant;

public class PazienteDTO implements Serializable{
    private String paziente_id;
    private String nome_completo;
    private Integer eta;
    private Instant data_arrivo;
    private Instant data_dimissione;
    private String reparto;
    private Integer soddisfazione;





    public PazienteDTO(String paziente_id, String nome_completo, Integer eta, Instant data_arrivo,
            Instant data_dimissione, String reparto, Integer soddisfazione) {
        this.paziente_id = paziente_id;
        this.nome_completo = nome_completo;
        this.eta = eta;
        this.data_arrivo = data_arrivo;
        this.data_dimissione = data_dimissione;
        this.reparto = reparto;
        this.soddisfazione = soddisfazione;
    }
    public String getPaziente_id() {
        return paziente_id;
    }
    public void setPaziente_id(String paziente_id) {
        this.paziente_id = paziente_id;
    }
    public String getNome_completo() {
        return nome_completo;
    }
    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }
    public Integer getEta() {
        return eta;
    }
    public void setEta(Integer eta) {
        this.eta = eta;
    }
    public Instant getData_arrivo() {
        return data_arrivo;
    }
    public void setData_arrivo(Instant data_arrivo) {
        this.data_arrivo = data_arrivo;
    }
    public Instant getData_dimissione() {
        return data_dimissione;
    }
    public void setData_dimissione(Instant data_dimissione) {
        this.data_dimissione = data_dimissione;
    }
    public String getReparto() {
        return reparto;
    }
    public void setReparto(String reparto) {
        this.reparto = reparto;
    }
    public Integer getSoddisfazione() {
        return soddisfazione;
    }
    public void setSoddisfazione(Integer soddisfazione) {
        this.soddisfazione = soddisfazione;
    }
    
    


    
}
