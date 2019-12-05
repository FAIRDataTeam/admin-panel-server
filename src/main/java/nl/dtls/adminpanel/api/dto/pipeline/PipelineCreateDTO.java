package nl.dtls.adminpanel.api.dto.pipeline;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.dtls.adminpanel.entity.pipeline.PipelineType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PipelineCreateDTO {

    @NotNull
    private PipelineType type;

    @NotBlank
    private String instanceUuid;

}
