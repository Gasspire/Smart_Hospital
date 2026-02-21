package it.unict.smarthospital.web_client.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import it.unict.smarthospital.common.Interfaces.IAuthService;
import it.unict.smarthospital.common.dto.SessionDTO;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RmiConfig rmiConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        
        if (session.getAttribute("token") != null) {
            return true;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("sh_token".equals(c.getName())) {
                    String tokenStr = c.getValue();
                    
                    IAuthService authService = rmiConfig.getAuthService();
                    if (authService != null) {
                        try {
                            SessionDTO dto = authService.getValidSessionDTO(tokenStr);
                            if (dto != null) {
                                session.setAttribute("token", dto);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        
        return true; // Passiamo la palla al Controller in ogni caso
    }
}