package it.unict.smarthospital;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import it.unict.smarthospital.common.Interfaces.IDataService;

public class RMIDataHandler {
    public static void main(String[] args) {
        try {
            DataHandler obj = new DataHandler();

            IDataService stub = (IDataService) UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.createRegistry(1599);
            registry.rebind("DataHandler", stub);

            System.out.println("[SERVER] RMI Data Server on port 1599");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
