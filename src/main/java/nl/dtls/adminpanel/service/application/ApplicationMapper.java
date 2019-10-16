package nl.dtls.adminpanel.service.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import nl.dtls.adminpanel.api.dto.application.ApplicationChangeDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationSimpleDTO;
import nl.dtls.adminpanel.api.dto.application.TemplateDTO;
import nl.dtls.adminpanel.entity.Application;
import nl.dtls.adminpanel.entity.Template;
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
                toTemplatesDTO(application.getTemplates()));
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
                fromTemplatesDTO(dto.getTemplates()));
    }

    public Application fromChangeDTO(ApplicationChangeDTO dto, Application application) {
        return
            application
                .toBuilder()
                .name(dto.getName())
                .deployCommand(dto.getDeployCommand())
                .disposeCommand(dto.getDisposeCommand())
                .templates(fromTemplatesDTO(dto.getTemplates()))
                .build();
    }

    public List<TemplateDTO> toTemplatesDTO(List<Template> dto) {
        return dto.stream().map(this::toTemplateDTO).collect(toList());
    }

    public TemplateDTO toTemplateDTO(Template template) {
        return
            new TemplateDTO(
                template.getName(),
                template.getContent());
    }

    public List<Template> fromTemplatesDTO(List<TemplateDTO> dto) {
        return dto.stream().map(this::fromTemplateDTO).collect(toList());
    }

    public Template fromTemplateDTO(TemplateDTO dto) {
        return
            new Template(
                dto.getName(),
                dto.getContent());
    }

}
