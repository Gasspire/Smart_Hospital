package it.unict.smarthospital.web_client.controllers;

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
import it.unict.smarthospital.common.UserRole;
import it.unict.smarthospital.web_client.config.RmiConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/paziente")
public class PazienteController {

    @Autowired
    private RmiConfig rmiConfig;

    @GetMapping("/home")
    public String homePaziente(HttpSession session, Model model) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token == null || token.getRuolo() != UserRole.patient) {
            return "redirect:/login";
        }

        try {
            PazienteDTO datiPaziente = rmiConfig.getDataService().ccPaziente(
                token.getUsername(),
                token.getToken()
            );

            List<EsameDTO> listaEsami = rmiConfig.getDataService().esamiPaziente(
                token.getUsername(),
                token.getToken()
            );

            model.addAttribute("paziente", datiPaziente);
            model.addAttribute("esami", listaEsami); 
            model.addAttribute("token", token);
            
            return "paziente/dashboard"; 

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore recupero dati: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/esame/{id}")
    public String dettaglioEsame(@PathVariable("id") String idEsame, HttpSession session, Model model) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token == null || token.getRuolo() != UserRole.patient) {
            return "redirect:/login";
        }

        try {
            EsameDTO esame = rmiConfig.getDataService().esamePaziente(
                token.getUsername(), 
                idEsame,             
                token.getToken()
            );
            
            model.addAttribute("esame", esame);
            model.addAttribute("token", token);
            
            return "paziente/esame_dettagliato"; 

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/paziente/home";
        }
    }

    @PostMapping("/update/soddisfazione")
    public String aggiornaSoddisfazione(@RequestParam("soddisfazione") int livello, HttpSession session) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token != null && token.getRuolo() == UserRole.patient) {
            try {
                rmiConfig.getDataService().aggiornaSoddisfazione(livello, token.getToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/paziente/home";
    }
}