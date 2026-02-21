package it.unict.smarthospital.common.Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

import it.unict.smarthospital.common.dto.EsameDTO;
import it.unict.smarthospital.common.dto.LiveStatsDTO;
import it.unict.smarthospital.common.dto.PazienteDTO;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.common.dto.StatisticheDTO;
import it.unict.smarthospital.common.dto.TurnoDTO;

public interface IDataService extends Remote{
    //metodo per ottenere una cartella clinica
    public PazienteDTO ccPaziente(String id_paziente, String token) throws RemoteException;
    //metodo per ottenere i nomi e gli Id dei pazienti in reparto
    public List<PazienteDTO> PazientiReparto(String reparto, String token) throws RemoteException;
    //metodo per ottenere gli esami di un paziente
    public List<EsameDTO> esamiPaziente(String id_paziente, String token) throws RemoteException;
    //metodo per ottenere uno specifico esame
    public EsameDTO esamePaziente(String id_paziente, String id_esame, String token) throws RemoteException; 
    //metodo per ottenere la lista dei turni in reparto
    public List<TurnoDTO> turniStaff(String staff_id, String token) throws RemoteException;
    //metodo per dimettere i pazienti, solo per medico
    public void dimettiPaziente(String id_paziente, String token) throws RemoteException;
    //metodo per prenotare esami a nome di un certo paziente
    public void prenotaEsame(String id_paziente, String nome_esame, String reparto, Instant data,String token) throws RemoteException;
    //metodo per modificare l'esito di un esame di un paziente.
    public void aggiornaEsito(String id_esame, String esito, String token)throws RemoteException; 
    //metodo per prendere le statistiche dei reparti in questa settimana
    public List<StatisticheDTO> getStatisticheReparti(String token) throws RemoteException;
    //metodo per registrare nuovi pazienti
    public void creaPaziente(String id, String nome, int eta, String reparto, String password, String token) throws RemoteException;
    //metodo per ottenere tutti gli utenti
    public List<SessionDTO> getListaUtenti(String token) throws RemoteException;
    //metodo per creare un nuovo utente (staff)
    public void creaUtente(String username, String password, String ruolo, String nomeCompleto, String token) throws RemoteException;
    //metodo per eliminare un utente
    public void eliminaUtente(String username, String token) throws RemoteException;
    //metodo per aggiornare il parametro di soddisfazione del paziente.
    public void aggiornaSoddisfazione(Integer lv, String token) throws RemoteException;
    //metodo per prendere le statistiche attuali del reparto
    public List<LiveStatsDTO> getLiveStats(String token) throws RemoteException;
}
