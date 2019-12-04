package nl.dtls.adminpanel.api.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import nl.dtls.adminpanel.api.dto.instance.InstanceChangeDTO;
import nl.dtls.adminpanel.api.dto.instance.InstanceDTO;
import nl.dtls.adminpanel.service.instance.InstanceService;
import nl.dtls.shared.entity.exception.ResourceNotFoundException;
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
@RequestMapping("/instances")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<InstanceDTO>> getInstances() {
        List<InstanceDTO> dto = instanceService.getInstances();
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<InstanceDTO> createInstance(@RequestParam(required = false) String source,
        @RequestBody(required = false) @Valid InstanceChangeDTO reqDto) {
        if (source == null) {
            InstanceDTO dto = instanceService.createInstance(reqDto);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } else {
            Optional<InstanceDTO> oDto = instanceService.cloneInstance(source);
            if (oDto.isPresent()) {
                return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException(format("Instance '%s' doesn't exist", source));
            }
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<InstanceDTO> getInstance(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        Optional<InstanceDTO> oDto = instanceService.getInstanceByUuid(uuid);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Instance '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.PUT)
    public ResponseEntity<InstanceDTO> putInstance(@PathVariable final String uuid,
        @RequestBody @Valid InstanceChangeDTO reqDto) throws ResourceNotFoundException {
        Optional<InstanceDTO> oDto = instanceService.updateInstance(uuid, reqDto);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Instance '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public HttpEntity deleteInstance(@PathVariable final String uuid)
        throws ResourceNotFoundException {
        boolean result = instanceService.deleteInstance(uuid);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(format("Instance '%s' doesn't exist", uuid));
        }
    }

}
