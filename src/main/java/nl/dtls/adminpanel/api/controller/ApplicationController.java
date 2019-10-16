package nl.dtls.adminpanel.api.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import nl.dtls.adminpanel.api.dto.application.ApplicationChangeDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationDTO;
import nl.dtls.adminpanel.entity.exception.ResourceNotFoundException;
import nl.dtls.adminpanel.service.application.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<ApplicationDTO>> getApplications() {
        List<ApplicationDTO> dto = applicationService.getApplications();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<ApplicationDTO> createApplication(
        @RequestParam(required = false) String source,
        @RequestBody(required = false) @Valid ApplicationChangeDTO reqDto) {
        if (source == null) {
            ApplicationDTO dto = applicationService.createApplication(reqDto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            Optional<ApplicationDTO> oDto = applicationService.cloneApplication(source);
            if (oDto.isPresent()) {
                return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException(format("Instance '%s' doesn't exist", source));
            }
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<ApplicationDTO> getApplication(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        Optional<ApplicationDTO> oDto = applicationService.getApplicationByUuid(uuid);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Application '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<ApplicationDTO> putApplication(@PathVariable final String uuid,
        @RequestBody @Valid ApplicationChangeDTO reqDto)
        throws ResourceNotFoundException {
        Optional<ApplicationDTO> oDto = applicationService.updateApplication(uuid, reqDto);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Application '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public HttpEntity deleteApplication(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        boolean result = applicationService.deleteApplication(uuid);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(format("Application '%s' doesn't exist", uuid));
        }
    }

}
