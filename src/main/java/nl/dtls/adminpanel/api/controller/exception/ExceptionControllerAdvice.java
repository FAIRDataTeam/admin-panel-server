package nl.dtls.adminpanel.api.controller.exception;

import javax.servlet.http.HttpServletResponse;
import nl.dtls.adminpanel.api.dto.error.ErrorDTO;
import nl.dtls.adminpanel.entity.exception.ResourceNotFoundException;
import nl.dtls.adminpanel.entity.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler({ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleBadRequest(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class, CredentialsExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleUnauthorized(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleResourceNotFound(ResourceNotFoundException e,
        HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.NOT_FOUND, e.getMessage());
    }

}
