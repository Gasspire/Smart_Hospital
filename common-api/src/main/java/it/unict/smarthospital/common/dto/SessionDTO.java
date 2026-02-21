package it.unict.smarthospital.common.dto;

import java.io.Serializable;
import java.time.Instant;

import it.unict.smarthospital.common.UserRole;

public class SessionDTO implements Serializable{
    private String token;
    private String username;
    private UserRole ruolo;
    private Instant scadenza;
    
    public SessionDTO(String payload, String username, UserRole ruolo, Instant scadenza) {
        this.token = payload;
        this.username = username;
        this.scadenza = scadenza;
        this.ruolo = ruolo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String payload) {
        this.token = payload;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Instant getScadenza() {
        return scadenza;
    }

    public void setScadenza(Instant scadenza) {
        this.scadenza = scadenza;
    }

    public UserRole getRuolo() {
        return ruolo;
    }

    public void setRuolo(UserRole ruolo) {
        this.ruolo = ruolo;
    }

    


}
