package it.unict.smarthospital.service;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.unict.smarthospital.DataBaseDatiHelper;
import it.unict.smarthospital.common.dto.EsameDTO;
import it.unict.smarthospital.common.dto.PazienteDTO;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.security.PolicyManager;

public class PazienteService {
    private static DataBaseDatiHelper db;
    private SecurityService s;
    
    public PazienteService() {
        db = new DataBaseDatiHelper();
        s = new SecurityService();
    }



    public PazienteDTO ccPaziente(String id_paziente, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);
        if(!s.isAuthorize(utente, PolicyManager.PERM_READ, PolicyManager.OBJ_CARTELLE_TUTTE)){
            if(!s.isAuthorize(utente, PolicyManager.PERM_READ ,PolicyManager.OBJ_CARTELLA_PROPRIA ) || !id_paziente.equals(utente.getUsername())){
                throw new RemoteException("Errore, non sei abilitato a leggere la cartella");
            }
        }

        String sql = "SELECT * FROM pazienti WHERE paziente_id = ?";

        try (Connection conn = db.getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id_paziente);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                Timestamp tsArr = rs.getTimestamp("data_arrivo");
                Timestamp tsDim = rs.getTimestamp("data_dimissione");

                return new PazienteDTO(
                    rs.getString("paziente_id"), 
                    rs.getString("nome_completo"),
                    rs.getInt("eta"),
                    (tsArr != null) ? tsArr.toInstant() : null,
                    (tsDim != null) ? tsDim.toInstant() : null,
                    rs.getString("reparto"),
                    rs.getInt("livello_soddisfazione")
                );
            }   
            else{
                throw new RemoteException("Qualcosa è andato storto nella ricerca della cartella");
            }

        } catch (Exception e) {
            throw new RemoteException("Errore DB: "+e.getMessage());
        }
    }

    public List<PazienteDTO> PazientiReparto(String repartoRichiesto, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);
        if(!s.isAuthorize(utente, PolicyManager.PERM_READ, PolicyManager.OBJ_CARTELLE_TUTTE)){
            throw new RemoteException("Non sei abilitato a fare questa operazione!");
        };

        List<PazienteDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM pazienti WHERE reparto = ? AND data_dimissione IS NULL";

        try (Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, repartoRichiesto);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Timestamp tsArr = rs.getTimestamp("data_arrivo");
                lista.add(new PazienteDTO(
                    rs.getString("paziente_id"),
                    rs.getString("nome_completo"),
                    rs.getInt("eta"),
                    (tsArr != null) ? tsArr.toInstant() : null,
                    null,
                    rs.getString("reparto"),
                    rs.getInt("livello_soddisfazione")
                ));
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore DB: " + e.getMessage());
        }
        return lista;
    }


    public List<EsameDTO> esamiPaziente(String id_paziente, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);
        if(!s.isAuthorize(utente, PolicyManager.PERM_READ, PolicyManager.OBJ_ESAMI)){
            if(!s.isAuthorize(utente, PolicyManager.PERM_READ ,PolicyManager.OBJ_CARTELLA_PROPRIA ) || !id_paziente.equals(utente.getUsername())){
                throw new RemoteException("Errore, non sei abilitato a leggere la cartella");
            }
        }
        List<EsameDTO> lista = new ArrayList<>();
        String sql = "SELECT * FROM esami WHERE paziente_id = ?";
        
        try (Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id_paziente);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Timestamp ts = rs.getTimestamp("data_esame");
                lista.add(new EsameDTO(
                    rs.getString("paziente_id"),
                    rs.getString("id_esame"),
                    rs.getString("nome_esame"),
                    rs.getString("reparto"),
                    (ts != null) ? ts.toInstant() : null,
                    rs.getString("esito")
                ));
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore DB Esami: " + e.getMessage());
        }
        return lista;
    }


    public EsameDTO esamePaziente(String id_paziente, String id_esame, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);
        if(!s.isAuthorize(utente, PolicyManager.PERM_READ, PolicyManager.OBJ_ESAMI)){
            if(!s.isAuthorize(utente, PolicyManager.PERM_READ ,PolicyManager.OBJ_CARTELLA_PROPRIA ) || !id_paziente.equals(utente.getUsername())){
                throw new RemoteException("Errore, non sei abilitato a fare questa operazione");
            }
        }

        String sql = "SELECT * FROM esami WHERE id_esame = ? AND paziente_id = ?";

        try (Connection conn = db.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id_esame);
            ps.setString(2, id_paziente);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Timestamp ts = rs.getTimestamp("data_esame");


                return new EsameDTO(
                    rs.getString("paziente_id"),
                    rs.getString("id_esame"),
                    rs.getString("nome_esame"),
                    rs.getString("reparto"),
                    (ts != null) ? ts.toInstant() : null,
                    rs.getString("esito")
                );
            }
            else{
                throw new RemoteException("Qualcosa è andato storto nella ricerca dell'esame");
            }
        } catch (Exception e) {
            throw new RemoteException("Errore DB Esami: " + e.getMessage());
        }
    }

    public void aggiornaSoddisfazione(int livello, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);

        if (!s.isAuthorize(utente, PolicyManager.PERM_WRITE, PolicyManager.OBJ_CARTELLA_PROPRIA)) {
            throw new RemoteException("Non hai i permessi per modificare i dati.");
        }

        String sql = "UPDATE pazienti SET livello_soddisfazione = ? WHERE paziente_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            if (livello < 0) livello = 0;
            if (livello > 100) livello = 100;

            ps.setInt(1, livello);
            ps.setString(2, utente.getUsername()); 
            
            ps.executeUpdate();
            System.out.println("[AUDIT] Paziente " + utente.getUsername() + " ha aggiornato soddisfazione a " + livello);

        } catch (SQLException e) {
            throw new RemoteException("Errore aggiornamento soddisfazione: " + e.getMessage());
        }
    }
    
}
