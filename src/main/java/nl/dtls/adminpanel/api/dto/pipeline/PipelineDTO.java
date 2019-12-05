package nl.dtls.adminpanel.api.dto.pipeline;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.dtls.adminpanel.api.dto.instance.InstanceSimpleDTO;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import nl.dtls.adminpanel.entity.pipeline.PipelineType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PipelineDTO {

    private String uuid;

    private PipelineType type;

    private PipelineStatus status;

    private String log;

    protected LocalDateTime created;

    protected Long duration;

    private InstanceSimpleDTO instance;

}
