package ru.itis.semwork3.security.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import ru.itis.semwork3.redis.JwtBlacklistService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("jwtLogoutFilter")
@RequiredArgsConstructor
public class JwtLogoutFilter extends OncePerRequestFilter {
    private final RequestMatcher matcher = new AntPathRequestMatcher("/logout", "GET");
    private final JwtBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (matcher.matches(request)) {
            String token = request.getHeader("Authorization");
            if (token == null) {
                var cookie = WebUtils.getCookie(request, "auth");
                if (cookie != null) {
                    token = cookie.getValue();
                }
            }
            blacklistService.add(token);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
