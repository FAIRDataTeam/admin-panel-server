package nl.dtls.adminpanel.api.filter;

import static java.lang.String.format;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CORSFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(final HttpServletRequest request,
        final HttpServletResponse response, final FilterChain fc)
        throws IOException, ServletException {

        String allowedMtds = String.join(",", RequestMethod.GET.name(), RequestMethod.POST.name(),
            RequestMethod.PUT.name(), RequestMethod.PATCH.name(), RequestMethod.DELETE.name());

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
            format("%s,%s,%s,%s", HttpHeaders.ORIGIN, HttpHeaders.AUTHORIZATION, HttpHeaders.ACCEPT,
                HttpHeaders.CONTENT_TYPE));
        response.setHeader(HttpHeaders.ALLOW, allowedMtds);
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, allowedMtds);

        fc.doFilter(request, response);
    }
}
