package ru.itis.semwork3.handler;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;
import ru.itis.semwork3.security.details.UserDetailsImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class WebSocketHandshakeHandler implements HandshakeHandler {
    @Value("${jwt.secret}")
    private String JWT_SECRET;

    private final DefaultHandshakeHandler handshakeHandler = new DefaultHandshakeHandler();

    @Override
    public boolean doHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws HandshakeFailureException {
        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        String token = request.getHeader("Authorization");
        if (token == null) {
            var cookie = WebUtils.getCookie(request, "auth");
            if (cookie != null) {
                token = cookie.getValue();
            }
        }
        if (token != null) {
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(JWT_SECRET)
                        .parseClaimsJws(token)
                        .getBody();
            } catch (Exception e) {
                throw new AuthenticationCredentialsNotFoundException("Bad token");
            }

            UserDetails userDetails = new UserDetailsImpl(String.valueOf(claims.get("id", String.class)),
                    claims.get("role", String.class));
            map.put("user", userDetails);
            map.put("sessionId", userDetails.getUsername());
            return handshakeHandler.doHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, map);
        } else {
            serverHttpResponse.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
    }
}
