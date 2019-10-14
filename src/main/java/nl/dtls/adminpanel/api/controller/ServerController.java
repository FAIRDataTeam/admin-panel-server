package nl.dtls.adminpanel.api.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import nl.dtls.adminpanel.api.dto.server.ServerChangeDTO;
import nl.dtls.adminpanel.api.dto.server.ServerDTO;
import nl.dtls.adminpanel.entity.exception.ResourceNotFoundException;
import nl.dtls.adminpanel.service.server.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servers")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity getServers() {
        List<ServerDTO> dto = serverService.getServers();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity createServer(@RequestBody @Valid ServerChangeDTO reqDto) {
        ServerDTO dto = serverService.createServer(reqDto);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public HttpEntity getInstance(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        Optional<ServerDTO> oDto = serverService.getServerByUuid(uuid);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Server '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public HttpEntity putInstance(@PathVariable final String uuid,
        @RequestBody @Valid ServerChangeDTO reqDto)
        throws ResourceNotFoundException {
        Optional<ServerDTO> oDto = serverService.updateServer(uuid, reqDto);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Server '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public HttpEntity deleteInstance(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        boolean result = serverService.deleteServer(uuid);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(format("Server '%s' doesn't exist", uuid));
        }
    }

}
