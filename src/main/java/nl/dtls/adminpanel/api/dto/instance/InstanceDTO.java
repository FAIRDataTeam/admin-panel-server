package nl.dtls.adminpanel.api.dto.instance;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.dtls.adminpanel.entity.instance.InstanceStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstanceDTO {

    private String uuid;

    private String name;

    private String url;

    private String path;

    private InstanceStatus status;

    private Map<String, Object> variables;

    private String applicationUuid;

    private String serverUuid;

}
