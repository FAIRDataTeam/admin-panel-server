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
package nl.dtls.adminpanel.service.instance;

import java.util.HashMap;
import nl.dtls.adminpanel.api.dto.instance.InstanceChangeDTO;
import nl.dtls.adminpanel.api.dto.instance.InstanceDTO;
import nl.dtls.adminpanel.api.dto.instance.InstanceSimpleDTO;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.instance.InstanceStatus;
import nl.dtls.adminpanel.entity.server.Server;
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

    public InstanceSimpleDTO toSimpleDTO(Instance instance) {
        return new InstanceSimpleDTO(instance.getUuid(), instance.getName(), instance.getUrl());
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
