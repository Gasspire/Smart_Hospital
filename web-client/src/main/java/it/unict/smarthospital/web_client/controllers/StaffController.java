package it.unict.smarthospital.web_client.controllers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unict.smarthospital.common.dto.EsameDTO;
import it.unict.smarthospital.common.dto.PazienteDTO;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.common.dto.TurnoDTO;
import it.unict.smarthospital.common.UserRole;
import it.unict.smarthospital.web_client.config.RmiConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private RmiConfig rmiConfig;

    @GetMapping("/home")
    public String homeStaff(@RequestParam(value = "reparto", defaultValue = "general_medicine") String reparto,
                            HttpSession session, Model model) {
        
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token == null || token.getRuolo() == UserRole.patient) { 
            return "redirect:/login";
        }

        try {
            List<PazienteDTO> listaPazienti = rmiConfig.getDataService().PazientiReparto(
                reparto, 
                token.getToken()
            );

            List<TurnoDTO> mieiTurni = rmiConfig.getDataService().turniStaff(
                token.getUsername(), 
                token.getToken()
            );

            model.addAttribute("pazienti", listaPazienti);
            model.addAttribute("turni", mieiTurni);
            model.addAttribute("repartoSelezionato", reparto);
            model.addAttribute("token", token);
            
            return "staff/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore Server: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/paziente/{id}")
    public String dettaglioPaziente(@PathVariable("id") String idPaziente, HttpSession session, Model model) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token == null || token.getRuolo() == UserRole.patient) {
            return "redirect:/login";
        }

        try {
            PazienteDTO p = rmiConfig.getDataService().ccPaziente(idPaziente, token.getToken());
            List<EsameDTO> esami = rmiConfig.getDataService().esamiPaziente(idPaziente, token.getToken());

            model.addAttribute("paziente", p);
            model.addAttribute("esami", esami);
            model.addAttribute("token", token);
            
            boolean isDoctor = (token.getRuolo() == UserRole.doctor);
            model.addAttribute("isDoctor", isDoctor);

            return "staff/paziente_view";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/staff/home";
        }
    }

    @PostMapping("/paziente/dimetti")
    public String dimettiPaziente(@RequestParam("idPaziente") String idPaziente, HttpSession session) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token != null && token.getRuolo() == UserRole.doctor) {
            try {
                rmiConfig.getDataService().dimettiPaziente(idPaziente, token.getToken());
            } catch (Exception e) { e.printStackTrace(); }
        }
        return "redirect:/staff/paziente/" + idPaziente;
    }

    @PostMapping("/esame/crea")
    public String creaEsame(@RequestParam("idPaziente") String idPaziente,
                            @RequestParam("nomeEsame") String nomeEsame,
                            @RequestParam("reparto") String reparto,
                            @RequestParam("dataEsame") String dataString, 
                            HttpSession session) {
        
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token != null && token.getRuolo() == UserRole.doctor) {
            try {
                java.time.Instant dataInstant = LocalDate.parse(dataString)
                                                .atStartOfDay(ZoneId.systemDefault())
                                                .toInstant();

                rmiConfig.getDataService().prenotaEsame(
                    idPaziente, 
                    nomeEsame, 
                    reparto, 
                    dataInstant, 
                    token.getToken()
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/staff/paziente/" + idPaziente;
    }

    @PostMapping("/esame/update")
    public String aggiornaEsame(@RequestParam("idEsame") String idEsame,
                                @RequestParam("esito") String nuovoEsito,
                                @RequestParam("idPaziente") String idPaziente,
                                HttpSession session) {
        
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token != null && token.getRuolo() == UserRole.doctor) {
            try {
                rmiConfig.getDataService().aggiornaEsito(idEsame, nuovoEsito, token.getToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/staff/paziente/" + idPaziente;
    }
}