package nl.dtls.adminpanel.api.controller;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineCreateDTO;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineDTO;
import nl.dtls.adminpanel.entity.exception.ResourceNotFoundException;
import nl.dtls.adminpanel.service.pipeline.PipelineService;
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
@RequestMapping("/pipelines")
public class PipelineController {

    @Autowired
    private PipelineService pipelineService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<PipelineDTO>> getPipelines() {
        List<PipelineDTO> dtos = pipelineService.getPipelines();
        return ResponseEntity.ok(dtos);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<PipelineDTO> postPipelines(@RequestBody @Valid PipelineCreateDTO reqDto) {
        PipelineDTO dto = pipelineService.createPipeline(reqDto);
        return ResponseEntity.ok(dto);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public HttpEntity deletePipelines() {
        pipelineService.deletePipelines();
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<PipelineDTO> getPipeline(@PathVariable final String uuid) {
        Optional<PipelineDTO> oDto = pipelineService.getPipelineByUuid(uuid);
        if (oDto.isPresent()) {
            return new ResponseEntity<>(oDto.get(), HttpStatus.OK);
        } else {
            throw new ResourceNotFoundException(format("Pipeline '%s' doesn't exist", uuid));
        }
    }

    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public HttpEntity deletePipeline(@PathVariable final String uuid) {
        boolean result = pipelineService.deletePipeline(uuid);
        if (result) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException(format("Instance '%s' doesn't exist", uuid));
        }
    }

}
