package com.agrosecure.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    // #region agent log
    private static final String DEBUG_LOG_PATH = "/Users/sebastianfernandezconde/IntelijProjects/AgroSecureAI/.cursor/debug.log";
    private void debugLog(String hypothesisId, String message, String data) {
        try (FileWriter fw = new FileWriter(DEBUG_LOG_PATH, true)) {
            fw.write("{\"hypothesisId\":\"" + hypothesisId + "\",\"location\":\"AuthEntryPointJwt\",\"message\":\"" + message + "\",\"data\":\"" + data.replace("\"", "'") + "\",\"timestamp\":" + System.currentTimeMillis() + "}\n");
        } catch (Exception e) {}
    }
    // #endregion

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        // #region agent log
        String path = request.getServletPath();
        String uri = request.getRequestURI();
        debugLog("H1", "UNAUTHORIZED_ACCESS", "path=" + path + ",uri=" + uri + ",method=" + request.getMethod() + ",query=" + request.getQueryString());
        // #endregion
        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", authException.getMessage());
        body.put("path", request.getServletPath());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}

