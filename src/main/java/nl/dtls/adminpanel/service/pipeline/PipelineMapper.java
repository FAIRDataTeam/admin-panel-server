package nl.dtls.adminpanel.service.pipeline;

import nl.dtls.adminpanel.api.dto.pipeline.PipelineCreateDTO;
import nl.dtls.adminpanel.api.dto.pipeline.PipelineDTO;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import nl.dtls.adminpanel.service.instance.InstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineMapper {

    @Autowired
    private InstanceMapper instanceMapper;

    public PipelineDTO toDTO(Pipeline pipeline) {
        return new PipelineDTO(pipeline.getUuid(), pipeline.getType(), pipeline.getStatus(),
            pipeline.getLog(), pipeline.getCreated(), pipeline.getDuration(),
            instanceMapper.toSimpleDTO(pipeline.getInstance()));
    }

    public Pipeline fromCreateDTO(PipelineCreateDTO reqDto, String uuid, Instance instance) {
        return new Pipeline(uuid, reqDto.getType(), PipelineStatus.QUEUED, "", instance);
    }

}
