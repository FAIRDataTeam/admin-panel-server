package nl.dtls.adminpanel.service.server;

import nl.dtls.adminpanel.api.dto.server.ServerChangeDTO;
import nl.dtls.adminpanel.api.dto.server.ServerDTO;
import nl.dtls.adminpanel.api.dto.server.ServerSimpleDTO;
import nl.dtls.adminpanel.entity.Server;
import org.springframework.stereotype.Service;

@Service
public class ServerMapper {

    public ServerDTO toDTO(Server server) {
        return
            new ServerDTO(
                server.getUuid(),
                server.getName(),
                server.getUsername(),
                server.getHostname(),
                server.getPrivateKey(),
                server.getPublicKey());
    }

    public ServerSimpleDTO toSimpleDTO(Server server) {
        return
            new ServerSimpleDTO(
                server.getUuid(),
                server.getName(),
                server.getUsername(),
                server.getHostname());
    }

    public Server fromCreateDTO(ServerChangeDTO dto, String uuid) {
        return
            new Server(
                uuid,
                dto.getName(),
                dto.getUsername(),
                dto.getHostname(),
                dto.getPrivateKey(),
                dto.getPublicKey());
    }

    public Server fromChangeDTO(ServerChangeDTO dto, Server server) {
        return
            server
                .toBuilder()
                .name(dto.getName())
                .username(dto.getUsername())
                .privateKey(dto.getPrivateKey())
                .publicKey(dto.getPublicKey())
                .build();
    }

}
