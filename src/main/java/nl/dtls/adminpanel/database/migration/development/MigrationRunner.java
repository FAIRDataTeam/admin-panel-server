package nl.dtls.adminpanel.database.migration.development;

import javax.annotation.PostConstruct;
import nl.dtls.adminpanel.Profiles;
import nl.dtls.adminpanel.database.migration.development.application.ApplicationMigration;
import nl.dtls.adminpanel.database.migration.development.instance.InstanceMigration;
import nl.dtls.adminpanel.database.migration.development.server.ServerMigration;
import nl.dtls.adminpanel.database.migration.development.user.UserMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Profiles.NON_PRODUCTION)
public class MigrationRunner {

    @Autowired
    private UserMigration userMigration;

    @Autowired
    private ApplicationMigration applicationMigration;

    @Autowired
    private ServerMigration serverMigration;

    @Autowired
    private InstanceMigration instanceMigration;

    @PostConstruct
    public void run() {
        userMigration.runMigration();
        applicationMigration.runMigration();
        serverMigration.runMigration();
        instanceMigration.runMigration();
    }

}
