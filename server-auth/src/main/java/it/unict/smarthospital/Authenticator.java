package it.unict.smarthospital;


import java.sql.PreparedStatement;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.List;

import it.unict.smarthospital.common.UserRole;
import it.unict.smarthospital.common.Interfaces.IAuthService;
import it.unict.smarthospital.common.dto.SessionDTO;


public class Authenticator implements IAuthService {
    private static DataBaseAuthHelper db;

    public Authenticator() {
        db = new DataBaseAuthHelper();
    }
    
    private User getUserByCredentials(String username, String pass) throws RemoteException{
        try (Connection conn = db.getConnection()) {
            String sql = "SELECT * FROM utenti WHERE username = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, pass);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                User utente = new User(rs.getString("username"), UserRole.valueOf(rs.getString("ruolo")));
                return utente;
            }
        } catch (Exception e) {
            throw new RemoteException("Il Database non risponde");
        }
        throw new RemoteException("Username o Password errati!");
    }

    @Override
    public SessionDTO trylogin(String username, String pass) throws RemoteException{
        User utente = getUserByCredentials(username, pass);//se arriviamo qui vuol dire che il db risponde, altrimenti arriverebbe l'eccezzione 
        UserRole ruolo_trovato = utente.getRole();
        SessionManager t = SessionManager.getInstance();
        try {
            SessionDTO token = t.tokenBuild(username, ruolo_trovato);
            return token;
        } catch (Exception e) {
            throw new RemoteException("Errore nella generazione del token: " + e.getMessage());
        }
    }

    @Override
    public void invalidateSession(String token) throws RemoteException{
        SessionManager t = SessionManager.getInstance();
        t.invalidateToken(token);
        return;
    }

    @Override
    public SessionDTO getValidSessionDTO(String token) throws RemoteException {
        SessionManager t = SessionManager.getInstance();
        return t.getValidTokenDTO(token);
    }

    @Override
    public void creaUtenteAuth(String username, String password, String ruolo) throws RemoteException {
        String sql = "INSERT INTO utenti (username, password, ruolo) VALUES (?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, ruolo);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RemoteException("Errore creazione utente su Auth Server: " + e.getMessage());
        }
    }
    
    @Override
    public void eliminaUtenteAuth(String username) throws RemoteException {
        SessionManager t = SessionManager.getInstance();
        t.invalidateUser(username);
        String sql = "DELETE FROM utenti WHERE username = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RemoteException("Errore eliminazione utente su Auth Server: " + e.getMessage());
        }
    }

    @Override
    public List<SessionDTO> getListaUtentiAuth() throws RemoteException {
        List<SessionDTO> lista = new java.util.ArrayList<>();
        String sql = "SELECT username, ruolo FROM utenti";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String u = rs.getString("username");
                String r = rs.getString("ruolo");
                lista.add(new SessionDTO(null, u, UserRole.valueOf(r),null));
            }
        } catch (Exception e) {
            throw new RemoteException("Errore lista utenti su Auth Server: " + e.getMessage());
        }
        return lista;
    }
}   
