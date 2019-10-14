package nl.dtls.adminpanel.api.dto.instance;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.dtls.adminpanel.api.dto.application.ApplicationSimpleDTO;
import nl.dtls.adminpanel.api.dto.server.ServerSimpleDTO;
import nl.dtls.adminpanel.entity.InstanceStatus;

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

    private ApplicationSimpleDTO application;

    private ServerSimpleDTO server;

}
