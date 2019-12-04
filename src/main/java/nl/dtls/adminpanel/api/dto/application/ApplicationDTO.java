package nl.dtls.adminpanel.api.dto.application;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ApplicationDTO {

    private String uuid;

    private String name;

    protected String deployCommand;

    protected String disposeCommand;

    protected List<TemplateDTO> templates;

    protected String formSpec;

}
