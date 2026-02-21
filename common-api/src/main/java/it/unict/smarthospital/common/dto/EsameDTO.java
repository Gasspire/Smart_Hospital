package it.unict.smarthospital.common.dto;

import java.io.Serializable;
import java.time.Instant;

public class EsameDTO implements Serializable{
    private String paziente_id;
    private String id_esame;
    private String nome_esame;
    private String reparto;
    private Instant data_esame;
    private String esito;

    



    public EsameDTO(String paziente_id, String id_esame, String nome_esame, String reparto, Instant data_esame, String esito) {
        this.paziente_id = paziente_id;
        this.id_esame = id_esame;
        this.nome_esame = nome_esame;
        this.reparto = reparto;
        this.data_esame = data_esame;
        this.esito = esito;
    }
    
    public String getPaziente_id() {
        return paziente_id;
    }
    public void setPaziente_id(String paziente_id) {
        this.paziente_id = paziente_id;
    }

    public String getNome_esame() {
        return nome_esame;
    }

    public void setNome_esame(String nome_esame) {
        this.nome_esame = nome_esame;
    }
    public String getReparto() {
        return reparto;
    }
    public void setReparto(String reparto) {
        this.reparto = reparto;
    }
    public Instant getData_esame() {
        return data_esame;
    }
    public void setData_esame(Instant data_esame) {
        this.data_esame = data_esame;
    }
    public String getEsito() {
        return esito;
    }
    public void setEsito(String esito) {
        this.esito = esito;
    }

    public String getId_esame() {
        return id_esame;
    }

    public void setId_esame(String id_esame) {
        this.id_esame = id_esame;
    }


    
}
