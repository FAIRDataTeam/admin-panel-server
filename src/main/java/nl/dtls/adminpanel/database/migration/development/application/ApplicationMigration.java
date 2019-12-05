package nl.dtls.adminpanel.database.migration.development.application;

import nl.dtls.adminpanel.database.migration.development.application.data.ApplicationFixtures;
import nl.dtls.adminpanel.database.migration.development.common.Migration;
import nl.dtls.adminpanel.database.repository.application.ApplicationRepository;
import nl.dtls.adminpanel.entity.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationMigration implements Migration {

    @Autowired
    private ApplicationFixtures applicationFixtures;

    @Autowired
    private ApplicationRepository applicationRepository;

    public void runMigration() {
        applicationRepository.deleteAll();
        Application fdpApplication = applicationFixtures.fdpApplication();
        applicationRepository.save(fdpApplication);
    }

}
