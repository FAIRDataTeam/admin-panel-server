package nl.dtls.adminpanel.database.migration.development.user;

import nl.dtls.adminpanel.database.migration.development.common.Migration;
import nl.dtls.adminpanel.database.migration.development.user.data.UserFixtures;
import nl.dtls.adminpanel.database.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMigration implements Migration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFixtures userFixtures;

    public void runMigration() {
        userRepository.deleteAll();
        userRepository.save(userFixtures.albert());
        userRepository.save(userFixtures.nikola());
    }

}
