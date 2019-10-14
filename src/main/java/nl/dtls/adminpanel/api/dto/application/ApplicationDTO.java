package nl.dtls.adminpanel.api.dto.application;

import java.util.Map;
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

    protected Map<String, String> templates;

}
