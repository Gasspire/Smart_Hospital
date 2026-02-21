package it.unict.smarthospital;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import it.unict.smarthospital.common.UserRole;
import it.unict.smarthospital.common.dto.*;


public class SessionManager {
    private static final String allChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
    private static final int validity = 3;
    private static final int tokenLength = 30;
    private static final SessionManager instance = new SessionManager();
    private static DataBaseAuthHelper db;
    


    private SessionManager(){
        db = new DataBaseAuthHelper();
    }

    public static SessionManager getInstance(){
        return instance;
    }

    public SessionDTO tokenBuild(final String user, final UserRole ruolo) throws Exception{ 
        String token = generateToken();
        Instant now = Instant.now();
        Instant scadenza = now.plus(validity, ChronoUnit.HOURS);
        try (Connection conn = db.getConnection()) {
            PreparedStatement prstm = conn.prepareStatement("INSERT INTO sessioni_attive (token, username, ruolo, data_scadenza) VALUES (?,?,?,?);");
            prstm.setString(1, token);
            prstm.setString(2, user);
            prstm.setString(3, ruolo.toString());
            prstm.setTimestamp(4, Timestamp.from(scadenza));
            prstm.executeUpdate();
            
            SessionDTO t = new SessionDTO(token, user, ruolo, scadenza);
            System.out.println("Ho generato il Token!");
            return t;
        }
    }

    private String generateToken() {
        int index;
        String result = "";
        for (int i = 0; i < tokenLength; i++) {
            index = (int) (allChars.length() * Math.random());
            result = result.concat(allChars.substring(index, index + 1));
        }
        return result;
    }
    
    public void invalidateToken(String token){
        try (Connection conn = db.getConnection()) {
            PreparedStatement prstm = conn.prepareStatement("DELETE from sessioni_attive where token = ?");
            prstm.setString(1, token);
            prstm.executeUpdate();
        } catch (Exception e) {
            return;
        }
    }

    public SessionDTO getValidTokenDTO(String token) throws RemoteException {
        try (Connection conn = db.getConnection()) {
            PreparedStatement prstm = conn.prepareStatement("SELECT * from sessioni_attive where token = ? AND data_scadenza > ?");
            prstm.setString(1, token);
            Timestamp now = Timestamp.from(Instant.now());
            prstm.setTimestamp(2, now);
            ResultSet rs = prstm.executeQuery();

            if (rs.next()) {
                String tk_token = rs.getString("token");
                String username = rs.getString("username");
                
                String ruoloStr = rs.getString("ruolo");
                UserRole ruolo = UserRole.valueOf(ruoloStr); 

                Timestamp ts = rs.getTimestamp("data_scadenza");
                Instant data_scadenza = (ts != null) ? ts.toInstant() : null;

                return new SessionDTO(tk_token, username, ruolo, data_scadenza);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            throw new RemoteException("Non è stato possibile recuperare il token: " + e.getMessage());
        }
    }

    public void invalidateUser(String user){
        try (Connection conn = db.getConnection()) {
            PreparedStatement prstm = conn.prepareStatement("DELETE from sessioni_attive where username = ?");
            prstm.setString(1, user);
            prstm.executeUpdate();
        } catch (Exception e) {
            return;
        }
    }
}
