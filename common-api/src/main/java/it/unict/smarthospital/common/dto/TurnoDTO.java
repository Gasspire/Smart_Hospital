package it.unict.smarthospital.common.dto;

import java.io.Serializable;

import it.unict.smarthospital.common.UserRole;

public class TurnoDTO implements Serializable{
    private Integer week_num;
    private String staff_id;
    private String nome_staff;
    private UserRole ruolo;
    private String reparto;
    private Boolean presente;


    



    public TurnoDTO(Integer week_num, String staff_id, String nome_staff, UserRole ruolo, String reparto,
            Boolean presente) {
        this.week_num = week_num;
        this.staff_id = staff_id;
        this.nome_staff = nome_staff;
        this.ruolo = ruolo;
        this.reparto = reparto;
        this.presente = presente;
    }
    
    public Integer getWeek_num() {
        return week_num;
    }
    public void setWeek_num(Integer week_num) {
        this.week_num = week_num;
    }
    public String getStaff_id() {
        return staff_id;
    }
    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }
    public String getNome_staff() {
        return nome_staff;
    }
    public void setNome_staff(String nome_staff) {
        this.nome_staff = nome_staff;
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
    public Boolean getPresente() {
        return presente;
    }
    public void setPresente(Boolean presente) {
        this.presente = presente;
    }



    
}
