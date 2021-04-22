package com.github.defaultcore.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.github.defaultcore.jwt.helper.JwtHelper;
import com.github.defaultcore.security.service.DefaultUserDetailsService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * jwt过滤器
 * </p>
 *
 * @author wangpeiyuan
 * @date 2021/4/22 8:43
 */
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final DefaultUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            String authorization = request.getHeader("Authorization");
            String startStr = "Bearer ";
            if (!StringUtils.isEmpty(authorization) && authorization.startsWith(startStr)) {
                String token = authorization.substring(startStr.length());
                String username = jwtHelper.verity(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            chain.doFilter(request, response);
        } catch (JWTVerificationException e) {
            SecurityContextHolder.clearContext();
            response.setStatus(401);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter printWriter = response.getWriter();
            String errMsg = e.getMessage();
            if (errMsg.contains("doesn't have a valid JSON format.")) {
                errMsg = "非法token，请重新获取.";
            }
            printWriter.write(errMsg);
            printWriter.flush();
        }
    }
}
