package it.unict.smarthospital.service;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.unict.smarthospital.AuthenticatorHelper;
import it.unict.smarthospital.DataBaseDatiHelper;
import it.unict.smarthospital.common.dto.LiveStatsDTO;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.common.dto.StatisticheDTO;
import it.unict.smarthospital.security.PolicyManager;

public class AdminService {
    private static DataBaseDatiHelper db;
    private static AuthenticatorHelper au;
    private SecurityService s;
    
    public AdminService() {
        db = new DataBaseDatiHelper();
        au = new AuthenticatorHelper(); 
        s = new SecurityService();
    }

    public List<StatisticheDTO> getStatisticheReparti(String token) throws RemoteException {
        SessionDTO admin = s.isTokenValid(token);
        
        if (!s.isAuthorize(admin, PolicyManager.PERM_READ, PolicyManager.OBJ_STATISTICHE)) {
            throw new RemoteException("Accesso negato: Non hai i permessi per leggere le statistiche.");
        }

        List<StatisticheDTO> lista = new ArrayList<>();
        int currentWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        String sql = "SELECT * FROM servizi_settimanali WHERE week_num = ?";

        try (Connection conn = db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql); 
            ps.setInt(1, currentWeek); 
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(new StatisticheDTO(
                    rs.getString("reparto"),
                    rs.getInt("week_num"),
                    rs.getInt("letti_disponibili"),
                    rs.getInt("richieste_totali"),
                    rs.getInt("richieste_accettate"),
                    rs.getInt("soddisfazione_pazienti")
                ));
            }
        } 
        catch (SQLException e) {
            throw new RemoteException("Errore lettura Statistiche: " + e.getMessage());
        }
        return lista;
    }
    
    public void creaPaziente(String id, String nome, int eta, String reparto, String password, String token) throws RemoteException {
        SessionDTO admin = s.isTokenValid(token);
        
        if (!s.isAuthorize(admin, PolicyManager.PERM_WRITE, PolicyManager.OBJ_SYS_ADMIN)) {
            throw new RemoteException("Accesso negato: Non puoi creare utenti.");
        }
        if (!s.isAuthorize(admin, PolicyManager.PERM_WRITE, PolicyManager.OBJ_CARTELLE_TUTTE)) {
            throw new RemoteException("Accesso negato: Non puoi creare cartelle cliniche.");
        }

        au.getService().creaUtenteAuth(id, password, "patient");

        String sqlPaziente = "INSERT INTO pazienti (paziente_id, nome_completo, eta, data_arrivo, reparto, livello_soddisfazione) VALUES (?, ?, ?, NOW(), ?, 100)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlPaziente)) {
            ps.setString(1, id);
            ps.setString(2, nome);
            ps.setInt(3, eta);
            ps.setString(4, reparto);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Errore creazione cartella clinica: " + e.getMessage());
        }
    }

    public List<SessionDTO> getListaUtenti(String token) throws RemoteException {
        SessionDTO admin = s.isTokenValid(token);
        
        if (!s.isAuthorize(admin, PolicyManager.PERM_READ, PolicyManager.OBJ_SYS_ADMIN)) {
            throw new RemoteException("Accesso negato: Solo gli amministratori possono vedere la lista utenti.");
        }

        return au.getService().getListaUtentiAuth();
    }


    public void creaUtente(String username, String password, String ruolo, String nomeCompleto, String token) throws RemoteException {
        SessionDTO admin = s.isTokenValid(token);
        
        if (!s.isAuthorize(admin, PolicyManager.PERM_WRITE, PolicyManager.OBJ_SYS_ADMIN)) {
            throw new RemoteException("Accesso negato: Non hai i permessi per creare utenti.");
        }

        au.getService().creaUtenteAuth(username, password, ruolo);

        if (!ruolo.equalsIgnoreCase("admin") && !ruolo.equalsIgnoreCase("patient")) {
            String sqlStaff = "INSERT INTO staff (staff_id, nome, ruolo, reparto) VALUES (?, ?, ?, 'general_medicine')";
            try (Connection conn = db.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sqlStaff)) {
                ps.setString(1, username);
                ps.setString(2, nomeCompleto);
                ps.setString(3, ruolo);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw new RemoteException("Errore creazione anagrafica staff: " + e.getMessage());
            }
        }
    }
    
    public void eliminaUtente(String username, String token) throws RemoteException {
        SessionDTO admin = s.isTokenValid(token);
        
        if (!s.isAuthorize(admin, PolicyManager.PERM_WRITE, PolicyManager.OBJ_SYS_ADMIN)) {
            throw new RemoteException("Accesso negato: Non hai i permessi per eliminare utenti.");
        }

        try (Connection conn = db.getConnection()) {
            
            String sqlStaff = "DELETE FROM staff WHERE staff_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlStaff)) {
                ps.setString(1, username);
                ps.executeUpdate();
            }

            String sqlPaziente = "DELETE FROM pazienti WHERE paziente_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlPaziente)) {
                ps.setString(1, username);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RemoteException("Errore durante l'eliminazione dei dati dal database: " + e.getMessage());
        }

        au.getService().eliminaUtenteAuth(username);
        System.out.println("[ADMIN] Utente " + username + " eliminato completamente.");
    }

    public List<LiveStatsDTO> getLiveStats(String token) throws RemoteException {
        SessionDTO admin = s.isTokenValid(token);
        if (!s.isAuthorize(admin, PolicyManager.PERM_READ, PolicyManager.OBJ_STATISTICHE)) {
            throw new RemoteException("Accesso negato alle statistiche live.");
        }

        List<LiveStatsDTO> lista = new ArrayList<>();
        
        String sql = "SELECT reparto, COUNT(*) as occupati, AVG(livello_soddisfazione) as media " +
                     "FROM pazienti WHERE data_dimissione IS NULL GROUP BY reparto";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String reparto = rs.getString("reparto");
                int occupati = rs.getInt("occupati");
                double media = rs.getDouble("media");
                
                int capienza = getCapienzaReparto(reparto);

                lista.add(new LiveStatsDTO(reparto, occupati, capienza, media));
            }
        } catch (SQLException e) {
            throw new RemoteException("Errore Live Stats: " + e.getMessage());
        }
        return lista;
    }


    private int getCapienzaReparto(String reparto) {
        switch (reparto.toLowerCase()) {
            case "general_medicine": return 50;
            case "surgery": return 30;
            case "icu": return 15; 
            case "emergency": return 40;
            default: return 20;
        }
    }
}