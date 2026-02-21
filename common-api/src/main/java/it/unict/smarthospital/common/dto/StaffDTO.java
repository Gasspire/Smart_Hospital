package it.unict.smarthospital.common.dto;

import java.io.Serializable;

import it.unict.smarthospital.common.UserRole;

public class StaffDTO implements Serializable{
    private String staff_id;
    private String nome;
    private UserRole ruolo;
    private String reparto;


    public StaffDTO(String staff_id, String nome, UserRole ruolo, String reparto) {
        this.staff_id = staff_id;
        this.nome = nome;
        this.ruolo = ruolo;
        this.reparto = reparto;
    }
    
    public String getStaff_id() {
        return staff_id;
    }
    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public UserRole getRuolo() {
        return ruolo;
    }
    public void setRuolo(UserRole ruolo) {
        this.ruolo = ruolo;
    }
    public String getReparto() {
        return reparto;
    }
    public void setReparto(String reparto) {
        this.reparto = reparto;
    }




    
}
