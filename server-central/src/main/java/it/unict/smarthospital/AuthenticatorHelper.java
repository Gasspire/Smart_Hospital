package it.unict.smarthospital;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import it.unict.smarthospital.common.Interfaces.IAuthService;

public class AuthenticatorHelper {
    
    private IAuthService authService; 
    private static final String AUTH_HOST = "localhost";
    private static final int AUTH_PORT = 1499;
    private static final String SERVICE_NAME = "Authenticator";

    public IAuthService getService() throws RemoteException {
        if (authService != null) {
            return authService;
        }
        return connect();
    }

    private IAuthService connect() throws RemoteException {
        try {
            System.out.println("[AUTH-HELPER] Ricerca Server Auth...");
            Registry registry = LocateRegistry.getRegistry(AUTH_HOST, AUTH_PORT);
            authService = (IAuthService) registry.lookup(SERVICE_NAME);
            System.out.println("[AUTH-HELPER] Connesso!");
            return authService;
        } catch (NotBoundException e) {
            throw new RemoteException("Il servizio Auth non è registrato nel registry.");
        }
    }
}