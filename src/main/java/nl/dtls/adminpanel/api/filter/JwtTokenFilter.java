package nl.dtls.adminpanel.api.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nl.dtls.adminpanel.api.dto.error.ErrorDTO;
import nl.dtls.adminpanel.entity.exception.UnauthorizedException;
import nl.dtls.adminpanel.service.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilterInternal(final HttpServletRequest request,
        final HttpServletResponse response, final FilterChain fc)
        throws IOException, ServletException {
        try {
            String token = jwtService.resolveToken(request);
            if (token != null && jwtService.validateToken(token)) {
                Authentication auth = jwtService.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            fc.doFilter(request, response);
        } catch (UnauthorizedException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            ErrorDTO error = new ErrorDTO(HttpStatus.UNAUTHORIZED, e.getMessage());
            objectMapper.writeValue(response.getWriter(), error);
        }
    }

}