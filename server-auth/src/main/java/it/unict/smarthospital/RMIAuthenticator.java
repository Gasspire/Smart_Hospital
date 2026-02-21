package it.unict.smarthospital;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.unict.smarthospital.common.Interfaces.IAuthService;

public class RMIAuthenticator {
    public static void main(String[] args) {
        try {
            Authenticator obj = new Authenticator();

            IAuthService stub = (IAuthService) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.createRegistry(1499);
            registry.rebind("Authenticator", stub);

            System.out.println("[SERVER] RMI Authenticathor Server on port 1499");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
