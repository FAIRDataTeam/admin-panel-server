package nl.dtls.adminpanel.api.dto.application;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationChangeDTO {

    @NotBlank
    private String name;

    @NotBlank
    protected String deployCommand;

    @NotBlank
    protected String disposeCommand;

    @NotNull
    protected List<TemplateDTO> templates;

}
