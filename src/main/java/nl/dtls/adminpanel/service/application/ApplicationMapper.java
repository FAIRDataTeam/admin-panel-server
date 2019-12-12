/**
 * The MIT License
 * Copyright © ${project.inceptionYear} DTL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package nl.dtls.adminpanel.service.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import nl.dtls.adminpanel.api.dto.application.ApplicationChangeDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationDTO;
import nl.dtls.adminpanel.api.dto.application.ApplicationSimpleDTO;
import nl.dtls.adminpanel.api.dto.application.TemplateDTO;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.application.Template;
import org.springframework.stereotype.Service;

@Service
public class ApplicationMapper {

    public ApplicationDTO toDTO(Application application) {
        return
            new ApplicationDTO(
                application.getUuid(),
                application.getName(),
                application.getDeployCommand(),
                application.getPauseCommand(),
                application.getDisposeCommand(),
                toTemplatesDTO(application.getTemplates()),
                application.getFormSpec());
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
                dto.getPauseCommand(),
                dto.getDisposeCommand(),
                fromTemplatesDTO(dto.getTemplates()),
                dto.getFormSpec());
    }

    public Application fromChangeDTO(ApplicationChangeDTO dto, Application application) {
        return
            application
                .toBuilder()
                .name(dto.getName())
                .deployCommand(dto.getDeployCommand())
                .pauseCommand(dto.getPauseCommand())
                .disposeCommand(dto.getDisposeCommand())
                .templates(fromTemplatesDTO(dto.getTemplates()))
                .formSpec(dto.getFormSpec())
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
