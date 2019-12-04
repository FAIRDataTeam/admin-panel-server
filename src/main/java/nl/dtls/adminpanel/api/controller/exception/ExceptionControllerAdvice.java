package nl.dtls.adminpanel.api.controller.exception;

import javax.servlet.http.HttpServletResponse;
import nl.dtls.shared.entity.exception.ForbiddenException;
import nl.dtls.shared.entity.exception.ResourceNotFoundException;
import nl.dtls.shared.entity.exception.ValidationException;
import nl.dtls.shared.api.dto.error.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ErrorDTO handleUnauthorized(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler({ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleForbidden(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleResourceNotFound(ResourceNotFoundException e,
        HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleInternalServerError(Exception e, HttpServletResponse response) {
        LOGGER.error(e.getMessage());
        return new ErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

}
