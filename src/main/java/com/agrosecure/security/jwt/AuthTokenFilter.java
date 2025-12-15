package com.agrosecure.security.jwt;

import com.agrosecure.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.FileWriter;

public class AuthTokenFilter extends OncePerRequestFilter {
    private JwtUtils jwtUtils;
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    // #region agent log
    private static final String DEBUG_LOG_PATH = "/Users/sebastianfernandezconde/IntelijProjects/AgroSecureAI/.cursor/debug.log";
    private void debugLog(String hypothesisId, String message, String data) {
        try (FileWriter fw = new FileWriter(DEBUG_LOG_PATH, true)) {
            String logEntry = "{\"hypothesisId\":\"" + hypothesisId + "\",\"location\":\"AuthTokenFilter\",\"message\":\"" + message + "\",\"data\":\"" + data.replace("\"", "'") + "\",\"timestamp\":" + System.currentTimeMillis() + "}\n";
            fw.write(logEntry);
            fw.flush();
            logger.info("DEBUG: {}", logEntry.trim());
        } catch (Exception e) {
            logger.error("Failed to write debug log: {}", e.getMessage());
        }
    }
    // #endregion

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // #region agent log
        String uri = request.getRequestURI();
        debugLog("H2", "FILTER_REQUEST", "uri=" + uri + ",method=" + request.getMethod() + ",servletPath=" + request.getServletPath());
        // #endregion
        try {
            String jwt = parseJwt(request);
            // #region agent log
            boolean isValid = jwt != null && jwtUtils != null && jwtUtils.validateJwtToken(jwt);
            debugLog("H2", "JWT_CHECK", "uri=" + uri + ",hasToken=" + (jwt != null) + ",isValid=" + isValid);
            // #endregion
            if (jwt != null && jwtUtils != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                // #region agent log
                debugLog("H2", "AUTH_SET", "uri=" + uri + ",username=" + username);
                // #endregion
            } else {
                // #region agent log
                debugLog("H2", "NO_AUTH", "uri=" + uri + ",noTokenOrInvalid");
                // #endregion
            }
        } catch (Exception e) {
            // #region agent log
            debugLog("H2", "AUTH_ERROR", "uri=" + request.getRequestURI() + ",error=" + e.getMessage());
            // #endregion
            logger.error("Cannot set user authentication: {}", e);
        }

        filterChain.doFilter(request, response);
        // #region agent log
        debugLog("H2", "FILTER_COMPLETE", "uri=" + request.getRequestURI() + ",status=" + response.getStatus());
        // #endregion
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}

