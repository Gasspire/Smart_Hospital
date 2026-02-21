package it.unict.smarthospital.service;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.unict.smarthospital.DataBaseDatiHelper;
import it.unict.smarthospital.common.UserRole;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.common.dto.TurnoDTO;
import it.unict.smarthospital.security.PolicyManager;

public class StaffService {
    private static DataBaseDatiHelper db;
    private SecurityService s;
    
    public StaffService() {
        db = new DataBaseDatiHelper();
        s = new SecurityService();
    }


    public void dimettiPaziente(String id_paziente, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);

        if (!s.isAuthorize(utente, PolicyManager.PERM_WRITE, PolicyManager.OBJ_CARTELLE_TUTTE)) {
            throw new RemoteException("Non hai i permessi per dimettere i pazienti.");
        }

        String sql = "UPDATE pazienti SET data_dimissione = ? WHERE paziente_id = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setTimestamp(1, Timestamp.from(Instant.now()));
            ps.setString(2, id_paziente);
            
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RemoteException("Paziente non trovato o dimissione fallita.");
            }
            
            System.out.println("[AUDIT] " + utente.getUsername() + " ha dimesso " + id_paziente);

        } catch (SQLException e) {
            throw new RemoteException("Errore DB Dimissione: " + e.getMessage());
        }
    }

    public void prenotaEsame(String id_paziente, String nome_esame, String reparto, Instant data, String token)
            throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);

        if (!s.isAuthorize(utente, PolicyManager.PERM_WRITE, PolicyManager.OBJ_ESAMI)) {
            throw new RemoteException("Non hai i permessi per prescrivere esami.");
        }

        String sql = "INSERT INTO esami (paziente_id, nome_esame, reparto, data_esame, esito) VALUES (?, ?, ?, ?, NULL)";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, id_paziente);
            ps.setString(2, nome_esame);
            ps.setString(3, reparto);
            
            if (data == null) {
                ps.setTimestamp(4, Timestamp.from(Instant.now()));
            } else {
                ps.setTimestamp(4, Timestamp.from(data));
            }

            ps.executeUpdate();
            System.out.println("[AUDIT] " + utente.getUsername() + " ha prescritto " + nome_esame + " per " + id_paziente);

        } catch (SQLException e) {
            throw new RemoteException("Errore DB Creazione Esame: " + e.getMessage());
        }
    }

        public void aggiornaEsito(String id_esame, String esito, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);

        if (!s.isAuthorize(utente, PolicyManager.PERM_WRITE, PolicyManager.OBJ_ESAMI)) {
            throw new RemoteException("Non hai i permessi per modificare gli esiti.");
        }

        String sql = "UPDATE esami SET esito = ? WHERE id_esame = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, esito);
            ps.setString(2, id_esame);
            
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RemoteException("Esame non trovato o impossibile aggiornare.");
            }
            
            System.out.println("[AUDIT] " + utente.getUsername() + " ha aggiornato l'esito dell'esame " + id_esame);

        } catch (SQLException e) {
            throw new RemoteException("Errore DB Aggiornamento Esito: " + e.getMessage());
        }
    }
    
    public List<TurnoDTO> turniStaff(String staff_id, String token) throws RemoteException {
        SessionDTO utente = s.isTokenValid(token);
        if(!s.isAuthorize(utente, PolicyManager.PERM_READ, PolicyManager.OBJ_TURNI) || !staff_id.equals(utente.getUsername())){
            throw new RemoteException("Errore, non sei abilitato a fare questa operazione");
        }

        int currentWeek = (Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) % 4) + 1;

        List<TurnoDTO> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM turni WHERE staff_id = ? ORDER BY week_num ASC";

        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, staff_id);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                 Integer presente = rs.getInt("presente");
                 Boolean isPresente = (presente == 1);

                 lista.add(new TurnoDTO(
                    rs.getInt("week_num"),
                    rs.getString("staff_id"),
                    rs.getString("nome_staff"),
                    UserRole.valueOf(rs.getString("ruolo")), 
                    rs.getString("reparto"),
                    isPresente
                 ));
            }
        } catch (SQLException e) {
             throw new RemoteException("Errore DB Turni: " + e.getMessage());
        }

        List<TurnoDTO> risultato = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            int week = ((currentWeek - 1 + i) % 4) + 1;

            for (TurnoDTO t : lista) {
                if (t.getWeek_num() == week) {
                    risultato.add(t);
                    break;
                }
            }
        }

        return risultato;
    }


}
