package it.unict.smarthospital.web_client.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unict.smarthospital.common.dto.LiveStatsDTO;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.common.dto.StatisticheDTO;
import it.unict.smarthospital.common.UserRole;
import it.unict.smarthospital.web_client.config.RmiConfig;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private RmiConfig rmiConfig;

    @GetMapping("/home")
    public String dashboard(HttpSession session, Model model) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        
        if (token == null || token.getRuolo() != UserRole.admin) {
            return "redirect:/login";
        }

        try {
            List<LiveStatsDTO> liveStats = rmiConfig.getDataService().getLiveStats(token.getToken());

            List<StatisticheDTO> stats = rmiConfig.getDataService().getStatisticheReparti(token.getToken());
            
            List<SessionDTO> utenti = rmiConfig.getDataService().getListaUtenti(token.getToken());

            model.addAttribute("liveStats", liveStats); 
            model.addAttribute("stats", stats);
            model.addAttribute("utenti", utenti);
            model.addAttribute("token", token);
            
            return "admin/dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Errore Dashboard: " + e.getMessage());
            return "login";
        }
    }
    @PostMapping("/paziente/create")
    public String creaPaziente(@RequestParam String id, @RequestParam String nome,
                               @RequestParam int eta, @RequestParam String reparto,
                               @RequestParam String password, HttpSession session) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        if (token != null && token.getRuolo() == UserRole.admin) {
            try {
                rmiConfig.getDataService().creaPaziente(id, nome, eta, reparto, password, token.getToken());
            } catch (Exception e) { e.printStackTrace(); }
        }
        return "redirect:/admin/home";
    }

    @PostMapping("/user/create")
    public String creaUtente(@RequestParam String username, @RequestParam String password,
                             @RequestParam String nome, @RequestParam String ruolo, HttpSession session) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        if (token != null && token.getRuolo() == UserRole.admin) {
            try {
                rmiConfig.getDataService().creaUtente(username, password, ruolo, nome, token.getToken());
            } catch (Exception e) { e.printStackTrace(); }
        }
        return "redirect:/admin/home";
    }

    @PostMapping("/user/delete")
    public String eliminaUtente(@RequestParam String username, HttpSession session) {
        SessionDTO token = (SessionDTO) session.getAttribute("token");
        if (token != null && token.getRuolo() == UserRole.admin) {
            try {
                if(!username.equals(token.getUsername())) {
                    rmiConfig.getDataService().eliminaUtente(username, token.getToken());
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
        return "redirect:/admin/home";
    }
}