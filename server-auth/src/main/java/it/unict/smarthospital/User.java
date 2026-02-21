package it.unict.smarthospital;

import it.unict.smarthospital.common.UserRole;

public class User {
    private String username;
    private UserRole role;

    


    public User(String username, UserRole role) {
        this.username = username;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole ruolo) {
        this.role = ruolo;
    }
   
}
