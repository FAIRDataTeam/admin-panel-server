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
package nl.dtls.adminpanel.service.server;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nl.dtls.adminpanel.api.dto.server.ServerChangeDTO;
import nl.dtls.adminpanel.api.dto.server.ServerDTO;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.database.repository.server.ServerRepository;
import nl.dtls.adminpanel.entity.server.Server;
import nl.dtls.adminpanel.service.instance.InstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private ServerMapper serverMapper;

    public List<ServerDTO> getServers() {
        List<Server> servers = serverRepository.findAll();
        return
            servers
                .stream()
                .map(serverMapper::toDTO)
                .collect(toList());
    }

    public Optional<ServerDTO> getServerByUuid(String uuid) {
        return
            serverRepository
                .findByUuid(uuid)
                .map(serverMapper::toDTO);
    }

    public ServerDTO createServer(ServerChangeDTO reqDto) {
        String uuid = UUID.randomUUID().toString();
        Server server = serverMapper.fromCreateDTO(reqDto, uuid);
        serverRepository.save(server);
        return serverMapper.toDTO(server);
    }

    public Optional<ServerDTO> updateServer(String uuid, ServerChangeDTO reqDto) {
        Optional<Server> oServer = serverRepository.findByUuid(uuid);
        if (oServer.isEmpty()) {
            return empty();
        }
        Server server = oServer.get();
        Server updatedInstance = serverMapper.fromChangeDTO(reqDto, server);
        serverRepository.save(updatedInstance);
        return of(serverMapper.toDTO(updatedInstance));
    }

    public boolean deleteServer(String uuid) {
        Optional<Server> oServer = serverRepository.findByUuid(uuid);
        if (oServer.isEmpty()) {
            return false;
        }
        Server server = oServer.get();
        instanceRepository.findByServer(server)
            .forEach(i -> instanceService.deleteInstance(i.getUuid()));
        serverRepository.delete(server);
        return true;
    }
}
