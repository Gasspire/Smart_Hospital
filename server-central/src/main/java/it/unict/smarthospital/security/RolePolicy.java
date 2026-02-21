package it.unict.smarthospital.security;

import java.util.List;

public class RolePolicy {
    private final String idRole;
    private final List<Permission> ps;

    public RolePolicy(String name, List<Permission> permis){
        idRole = name;
        ps = permis;
    }

    public boolean hasPermission(String p){
        return ps.stream().anyMatch(perm -> perm.idPerm().equals(p));
    }

    public boolean hasPermissionOnObject(String p, String obj){
        return ps.stream().anyMatch(perm -> perm.idPerm().equals(p) && perm.protectionObj().equals(obj));
    }

    public String name(){return idRole;}
}
