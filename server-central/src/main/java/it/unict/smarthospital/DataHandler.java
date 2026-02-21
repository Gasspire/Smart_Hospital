package it.unict.smarthospital;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

import it.unict.smarthospital.common.Interfaces.IDataService;
import it.unict.smarthospital.common.dto.*;

import it.unict.smarthospital.service.*;


public class DataHandler implements IDataService{
    private static PazienteService pazienteService;
    private static StaffService staffService;
    private static AdminService adminService;
    
    public DataHandler() {
        pazienteService = new PazienteService();
        staffService = new StaffService();
        adminService = new AdminService();
    }

    @Override
    public PazienteDTO ccPaziente(String id_paziente, String token) throws RemoteException {
        return pazienteService.ccPaziente(id_paziente, token);
    }

    @Override
    public List<PazienteDTO> PazientiReparto(String reparto, String token) throws RemoteException {
        return pazienteService.PazientiReparto(reparto, token);
    }

    @Override
    public List<EsameDTO> esamiPaziente(String id_paziente, String token) throws RemoteException {
        return pazienteService.esamiPaziente(id_paziente, token);
    }

    @Override
    public EsameDTO esamePaziente(String id_paziente, String id_esame, String token) throws RemoteException {
        return pazienteService.esamePaziente(id_paziente, id_esame, token);
    }

    @Override
    public List<TurnoDTO> turniStaff(String staff_id, String token) throws RemoteException {
        return staffService.turniStaff(staff_id, token);
    }

    @Override
    public void dimettiPaziente(String id_paziente, String token) throws RemoteException {
        staffService.dimettiPaziente(id_paziente, token);
        return;
    }

    @Override
    public void prenotaEsame(String id_paziente, String nome_esame, String reparto, Instant data, String token) throws RemoteException {
        staffService.prenotaEsame(id_paziente, nome_esame, reparto, data, token);
        return;
    }

    @Override
    public void aggiornaEsito(String id_esame, String esito, String token) throws RemoteException {
        staffService.aggiornaEsito(id_esame, esito, token);
    }

    @Override
    public List<StatisticheDTO> getStatisticheReparti(String token) throws RemoteException {
        return adminService.getStatisticheReparti(token);
    }

    @Override
    public void creaPaziente(String id, String nome, int eta, String reparto, String password, String token) throws RemoteException {
        adminService.creaPaziente(id, nome, eta, reparto, password, token);
    }

    @Override
    public List<SessionDTO> getListaUtenti(String token) throws RemoteException {
        return adminService.getListaUtenti(token);
    }

    @Override
    public void creaUtente(String username, String password, String ruolo, String nomeCompleto, String token) throws RemoteException {
        adminService.creaUtente(username, password, ruolo, nomeCompleto, token);
        return;
    }

    @Override
    public void eliminaUtente(String username, String token) throws RemoteException {
        adminService.eliminaUtente(username, token);
        return;
    }

    @Override
    public void aggiornaSoddisfazione(Integer lv, String token) throws RemoteException {
        pazienteService.aggiornaSoddisfazione(lv, token);
        return;
    }

    @Override
    public List<LiveStatsDTO> getLiveStats(String token) throws RemoteException {
        return adminService.getLiveStats(token);
    }

}
