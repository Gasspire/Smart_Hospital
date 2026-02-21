package it.unict.smarthospital.service;

import java.rmi.RemoteException;

import it.unict.smarthospital.AuthenticatorHelper;
import it.unict.smarthospital.common.Interfaces.IAuthService;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.security.PolicyManager;
import it.unict.smarthospital.security.RolePolicy;

public class SecurityService {
    private static AuthenticatorHelper au;

    public SecurityService(){
        au = new AuthenticatorHelper();
    }

    public SessionDTO isTokenValid(String token)throws RemoteException{
        IAuthService auth = au.getService();
        SessionDTO utente = auth.getValidSessionDTO(token);
        
        if(utente == null){
            throw new RemoteException("Sessione scaduta o Token non valido!");
        }
        return utente;
    }

    public Boolean isAuthorize(SessionDTO utente, String action, String requiredobj) throws RemoteException{

        RolePolicy userPolicy = PolicyManager.getUserPolicy(utente.getRuolo());

        if(userPolicy.hasPermissionOnObject(action, requiredobj)){
            return true;
        }

        return false;
    }

}

