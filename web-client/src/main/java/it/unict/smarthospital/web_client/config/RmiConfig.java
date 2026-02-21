package it.unict.smarthospital.web_client.config;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.springframework.context.annotation.Configuration;

import it.unict.smarthospital.common.Interfaces.IAuthService;
import it.unict.smarthospital.common.Interfaces.IDataService;


@Configuration
public class RmiConfig {

    private IAuthService authService; 
    private IDataService dataService; 

    public IAuthService getAuthService() {
        try {
            if (authService == null) {
                System.out.println("Connessione RMI con Authenticator in corso...");
                Registry registry = LocateRegistry.getRegistry("localhost", 1499);
                authService = (IAuthService) registry.lookup("Authenticator");
            }
            return authService;
        } catch (Exception e) {
            System.err.println("Errore RMI: " + e.getMessage());
            return null;
        }
    }

    public IDataService getDataService(){
        try{
            if (dataService == null) {
                System.out.println("Connessione RMI con DataService in corso...");
                Registry registry = LocateRegistry.getRegistry("localhost", 1599);
                dataService = (IDataService) registry.lookup("DataHandler");
            }
            return dataService;
        }
        catch (Exception e){
            System.err.println("Errore RMI: " + e.getMessage());
            return null;
        }
    }
}