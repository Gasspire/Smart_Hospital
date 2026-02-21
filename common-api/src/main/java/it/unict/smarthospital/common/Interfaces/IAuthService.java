package it.unict.smarthospital.common.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import it.unict.smarthospital.common.dto.SessionDTO;

public interface IAuthService extends Remote{
    public SessionDTO trylogin(String user, String pass) throws RemoteException;
    public void invalidateSession(String token) throws RemoteException;
    public SessionDTO getValidSessionDTO(String token) throws RemoteException;




    public void creaUtenteAuth(String username, String password, String ruolo) throws RemoteException;
    public void eliminaUtenteAuth(String username) throws RemoteException;
    public List<SessionDTO> getListaUtentiAuth() throws RemoteException;
}
