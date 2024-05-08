package org.example.server.config.jwt;

import org.example.server.services.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwtFromCookie(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                request.setAttribute("email", email);
            } else {
                String refreshJwt = parseJwtFromHeader(request);
                if (refreshJwt != null && jwtUtils.validateRefreshJwtToken(refreshJwt)) {
                    String email = jwtUtils.getEmailFromRefreshJwtToken(refreshJwt);



                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        request.setAttribute("email", email);

                        String newAccessJwt = jwtUtils.generateJwtAccessTokenUseRefresh(email);

                        Cookie cookie = new Cookie("jwt", newAccessJwt);
                        cookie.setMaxAge(jwtUtils.getJwtAccessExpirationMs() / 1000);
                        cookie.setPath("/api/user/");
                        cookie.setSecure(true);
                        cookie.setHttpOnly(true);

                        response.addCookie(cookie);

                }
            }
        }catch (Exception e) {
            System.err.println(e);
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwtFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    private String parseJwtFromHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if(headerAuth != null) {
            if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
                return headerAuth.substring(7, headerAuth.length());
            }
        }

        return null;
    }
}
