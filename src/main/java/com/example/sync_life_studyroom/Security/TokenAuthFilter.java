package com.example.sync_life_studyroom.Security;

import com.example.sync_life_studyroom.Entity.Role;
import com.example.sync_life_studyroom.Entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }

        User user;
        if("admin-token".equals(token)) {
            user = new User(null, Role.ADMIN);
        } else if(token.startsWith("user-token-")) {
            Long id = Long.parseLong(token.substring(11));
            user = new User(id, Role.USER);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        request.setAttribute("currentUser", user);
        filterChain.doFilter(request, response);
    }
}
