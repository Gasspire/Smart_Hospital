package it.unict.smarthospital.web_client.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.unict.smarthospital.common.Interfaces.IAuthService;
import it.unict.smarthospital.common.dto.SessionDTO;
import it.unict.smarthospital.web_client.config.RmiConfig;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private RmiConfig rmiConfig;

    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if(session.getAttribute("token") != null){
            try {
                SessionDTO token = (SessionDTO) session.getAttribute("token");
                String tk = (String) token.getToken();
                
                IAuthService authService = rmiConfig.getAuthService();
                
                if(authService != null && authService.getValidSessionDTO(tk) != null){
                    return "redirect:/dashboard";
                }
            } catch (Exception e) {
                return "login";
            }
        }
        return "login";
    }
    
    @PostMapping("/login")
    public String tryLogin(@RequestParam String username, @RequestParam String password, 
                           HttpSession session, Model model, HttpServletResponse response) {
        try {
            IAuthService authService = rmiConfig.getAuthService();

            if(authService == null){ 
                model.addAttribute("error", "Errore: Il servizio di Autenticazione non è raggiungibile, riprova più tardi!");
                return "login";
            }

            SessionDTO token = authService.trylogin(username, password);

            session.setAttribute("token", token);
            
            Cookie authCookie = new Cookie("sh_token", (String) token.getToken());
            authCookie.setMaxAge(3 * 60 * 60); 
            authCookie.setPath("/"); 
            authCookie.setHttpOnly(true);
            response.addCookie(authCookie);

            return "redirect:/dashboard";

        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            String errmessagge = cause.getMessage();
            model.addAttribute("error", "Login fallito: " + errmessagge);
            return "login";
        }        
    }
    
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        SessionDTO s = (SessionDTO) session.getAttribute("token");
        if (s == null){
            return "redirect:/login";
        }
        try {
            String tk = (String) s.getToken();
        
            IAuthService authService = rmiConfig.getAuthService();

            if(authService == null || authService.getValidSessionDTO(tk)==null){
                session.invalidate();
                return "redirect:/login";
            }

        } catch (Exception e) {
            e.printStackTrace();
            Throwable cause = e;
            while (cause.getCause() != null) {
                cause = cause.getCause();
            }
            model.addAttribute("error", "Errore sessione: " + cause.getMessage());
            return "login"; 
        }

        switch (s.getRuolo()) {
            case patient:
                return "redirect:/paziente/home";
            case nurse:
                return "redirect:/staff/home";
            case doctor:
                return "redirect:/staff/home";
            case admin:
                return "redirect:/admin/home";
            default:
                model.addAttribute("username", s.getUsername());
                model.addAttribute("ruolo", s.getRuolo());
                return "dashboard";
        }
    }

    @GetMapping("/logout")
    public String logoutinvalidate(HttpSession session, Model model, HttpServletResponse response) {
        try {
            SessionDTO t = (SessionDTO) session.getAttribute("token");
            if (t != null) {
                String token = (String) t.getToken();
                
                IAuthService authService = rmiConfig.getAuthService();
                if (authService != null) {
                    authService.invalidateSession(token);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Svuota la RAM
            session.invalidate(); 
            
            Cookie deleteCookie = new Cookie("sh_token", null);
            deleteCookie.setMaxAge(0); 
            deleteCookie.setPath("/");
            response.addCookie(deleteCookie);
        }
        
        return "redirect:/login"; 
    }
}