package nl.dtls.adminpanel.service.pipeline;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineCreateDTO;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineDTO;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.database.repository.pipeline.PipelineRepository;
import nl.dtls.adminpanel.entity.exception.ValidationException;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private PipelineMapper pipelineMapper;

    public List<PipelineDTO> getPipelines() {
        return
            pipelineRepository
                .findAll()
                .stream()
                .map(pipelineMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<PipelineDTO> getPipelineByUuid(String uuid) {
        return
            pipelineRepository
                .findByUuid(uuid)
                .map(pipelineMapper::toDTO);
    }

    public PipelineDTO createPipeline(PipelineCreateDTO reqDto) {
        // Get Instance
        Optional<Instance> oInstance = instanceRepository.findByUuid(reqDto.getInstanceUuid());
        if (oInstance.isEmpty()) {
            throw new ValidationException("Instance doesn't exist");
        }
        Instance instance = oInstance.get();

        // Run pipeline
        String uuid = UUID.randomUUID().toString();
        Pipeline pipeline = pipelineMapper.fromCreateDTO(reqDto, uuid, instance);
        pipelineRepository.save(pipeline);
        return pipelineMapper.toDTO(pipeline);
    }

    public void deletePipelines() {
        pipelineRepository.deleteAll();
    }

    public boolean deletePipeline(String uuid) {
        Optional<Pipeline> oPipeline = pipelineRepository.findByUuid(uuid);
        if (oPipeline.isEmpty()) {
            return false;
        }
        pipelineRepository.delete(oPipeline.get());
        return true;
    }
}
