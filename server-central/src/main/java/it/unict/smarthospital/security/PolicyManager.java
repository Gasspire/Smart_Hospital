package it.unict.smarthospital.security;

import java.util.List;
import java.util.Map;

import it.unict.smarthospital.common.UserRole;

public class PolicyManager {
    //Permessi
    public static final String PERM_READ = "READ";
    public static final String PERM_WRITE = "WRITE";

    public static final String OBJ_CARTELLE_TUTTE = "CARTELLE_TUTTE";
    public static final String OBJ_CARTELLA_PROPRIA = "CARTELLA_PROPRIA";
    public static final String OBJ_ESAMI = "ESAMI";
    public static final String OBJ_TURNI = "TURNI";

    public static final String OBJ_STATISTICHE = "STATISTICHE";
    public static final String OBJ_SYS_ADMIN = "SYS_ADMIN";

    //Mappa dei permessi
    private static final Map<UserRole, RolePolicy> policyMap;

    //Inizializzazione dei permessi
    static{
        RolePolicy policyMedico = new RolePolicy("PolicyMedico",List.of(
            new Permission(PERM_READ, OBJ_CARTELLE_TUTTE),
            new Permission(PERM_WRITE, OBJ_CARTELLE_TUTTE),
            new Permission(PERM_READ, OBJ_ESAMI),
            new Permission(PERM_WRITE, OBJ_ESAMI),
            new Permission(PERM_READ, OBJ_TURNI)
            
        ));

        RolePolicy policyInfermiere = new RolePolicy("PolicyInfermiere",List.of(
            new Permission(PERM_READ, OBJ_CARTELLE_TUTTE),
            new Permission(PERM_READ, OBJ_ESAMI),
            new Permission(PERM_READ, OBJ_TURNI)
        ));

        RolePolicy policyPaziente = new RolePolicy("PolicyPaziente", List.of(
            new Permission(PERM_READ, OBJ_CARTELLA_PROPRIA),
            new Permission(PERM_WRITE, OBJ_CARTELLA_PROPRIA)
        
        ));

        RolePolicy policyAdmin = new RolePolicy("PolicyAdmin", List.of(
            // Può leggere le statistiche
            new Permission(PERM_READ, OBJ_STATISTICHE),
            new Permission(PERM_READ, OBJ_SYS_ADMIN),
            new Permission(PERM_WRITE, OBJ_SYS_ADMIN),
            // Può scrivere sulle cartelle (necessario per creare la cartella clinica iniziale del paziente)
            new Permission(PERM_WRITE, OBJ_CARTELLE_TUTTE)
        ));

        policyMap = Map.of(
            UserRole.doctor, policyMedico,
            UserRole.nurse, policyInfermiere,
            UserRole.patient, policyPaziente,
            UserRole.admin, policyAdmin
        );
    }


    public static RolePolicy getUserPolicy(UserRole ruoloenum){
        return policyMap.getOrDefault(ruoloenum, new RolePolicy("GUEST",  List.of()));
    }


}   
