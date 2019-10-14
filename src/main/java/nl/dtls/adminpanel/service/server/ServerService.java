package nl.dtls.adminpanel.service.server;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import nl.dtls.adminpanel.api.dto.server.ServerChangeDTO;
import nl.dtls.adminpanel.api.dto.server.ServerDTO;
import nl.dtls.adminpanel.database.repository.ServerRepository;
import nl.dtls.adminpanel.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerService {

    @Autowired
    private ServerRepository serverRepository;

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
        serverRepository.delete(oServer.get());
        return true;
    }
}
