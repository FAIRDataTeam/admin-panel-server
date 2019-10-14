package nl.dtls.adminpanel.service.application;

import nl.dtls.adminpanel.api.dto.application.ApplicationChangeDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationSimpleDTO;
import nl.dtls.adminpanel.entity.Application;
import org.springframework.stereotype.Service;

@Service
public class ApplicationMapper {

    public ApplicationDTO toDTO(Application application) {
        return
            new ApplicationDTO(
                application.getUuid(),
                application.getName(),
                application.getDeployCommand(),
                application.getDisposeCommand(),
                application.getTemplates());
    }

    public ApplicationSimpleDTO toSimpleDTO(Application application) {
        return
            new ApplicationSimpleDTO(
                application.getUuid(),
                application.getName());
    }

    public Application fromCreateDTO(ApplicationChangeDTO dto, String uuid) {
        return
            new Application(
                uuid,
                dto.getName(),
                dto.getDeployCommand(),
                dto.getDisposeCommand(),
                dto.getTemplates());
    }

    public Application fromChangeDTO(ApplicationChangeDTO dto, Application application) {
        return
            application
                .toBuilder()
                .name(dto.getName())
                .deployCommand(dto.getDeployCommand())
                .disposeCommand(dto.getDisposeCommand())
                .templates(dto.getTemplates())
                .build();
    }

}
