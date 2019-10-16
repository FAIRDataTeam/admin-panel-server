package nl.dtls.adminpanel.database.fixtures.data;

import nl.dtls.adminpanel.entity.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServerFixtures {

    @Value("${dummy.server.username:}")
    private String serverUsername;

    @Value("${dummy.server.hostname:}")
    private String serverHostname;

    @Value("${dummy.server.privateKey:}")
    private String serverPrivateKey;

    @Value("${dummy.server.publicKey:}")
    private String serverPublicKey;

    public Server fdpServer() {
        return new Server(
            "166c48ba-64d9-473a-8b59-7c1fdbb901f0",
            "FDP Server",
            serverUsername,
            serverHostname,
            serverPrivateKey,
            serverPublicKey);
    }

}
