package nl.dtls.adminpanel.service.instance;

import java.util.HashMap;
import nl.dtls.adminpanel.api.dto.instance.InstanceChangeDTO;
import nl.dtls.adminpanel.api.dto.instance.InstanceDTO;
import nl.dtls.adminpanel.entity.Application;
import nl.dtls.adminpanel.entity.Instance;
import nl.dtls.adminpanel.entity.InstanceStatus;
import nl.dtls.adminpanel.entity.Server;
import nl.dtls.adminpanel.service.application.ApplicationMapper;
import nl.dtls.adminpanel.service.server.ServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstanceMapper {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private ServerMapper serverMapper;

    public InstanceDTO toDTO(Instance instance, InstanceStatus status) {
        return new InstanceDTO(
            instance.getUuid(),
            instance.getName(),
            instance.getUrl(),
            instance.getPath(),
            status,
            instance.getVariables(),
            instance.getApplication().getUuid(),
            instance.getServer().getUuid()
        );
    }

    public Instance fromCreateDTO(InstanceChangeDTO reqDto, String uuid, Server server,
        Application application) {
        return new Instance(
            uuid,
            reqDto.getName(),
            reqDto.getUrl(),
            reqDto.getPath(),
            reqDto.getVariables(),
            new HashMap<>(),
            application,
            server);
    }

    public Instance fromChangeDTO(InstanceChangeDTO dto, Instance instance, Server server,
        Application application) {
        return instance
            .toBuilder()
            .name(dto.getName())
            .url(dto.getUrl())
            .path(dto.getPath())
            .variables(dto.getVariables())
            .server(server)
            .application(application)
            .build();
    }

}
