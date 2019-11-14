package nl.dtls.adminpanel.database.migration.development.server;

import nl.dtls.adminpanel.database.migration.development.common.Migration;
import nl.dtls.adminpanel.database.migration.development.server.data.ServerFixtures;
import nl.dtls.adminpanel.database.repository.ServerRepository;
import nl.dtls.adminpanel.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerMigration implements Migration {

    @Autowired
    private ServerFixtures serverFixtures;

    @Autowired
    private ServerRepository serverRepository;

    public void runMigration() {
        serverRepository.deleteAll();
        Server fdpServer = serverFixtures.fdpServer();
        serverRepository.save(fdpServer);
    }

}
