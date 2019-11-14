package nl.dtls.adminpanel.database.migration.development.instance;

import nl.dtls.adminpanel.database.migration.development.common.Migration;
import nl.dtls.adminpanel.database.migration.development.instance.data.InstanceFixtures;
import nl.dtls.adminpanel.database.repository.InstanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstanceMigration implements Migration {

    @Autowired
    private InstanceFixtures instanceFixtures;

    @Autowired
    private InstanceRepository instanceRepository;

    public void runMigration() {
        instanceRepository.deleteAll();
        instanceRepository.save(instanceFixtures.fdpStaging());
    }

}
