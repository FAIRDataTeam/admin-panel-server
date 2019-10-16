package nl.dtls.adminpanel.database.fixtures;

import nl.dtls.adminpanel.database.fixtures.data.ApplicationFixtures;
import nl.dtls.adminpanel.database.fixtures.data.InstanceFixtures;
import nl.dtls.adminpanel.database.fixtures.data.ServerFixtures;
import nl.dtls.adminpanel.database.fixtures.data.UserFixtures;
import nl.dtls.adminpanel.database.repository.ApplicationRepository;
import nl.dtls.adminpanel.database.repository.InstanceRepository;
import nl.dtls.adminpanel.database.repository.ServerRepository;
import nl.dtls.adminpanel.database.repository.UserRepository;
import nl.dtls.adminpanel.entity.Application;
import nl.dtls.adminpanel.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DummyDataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFixtures userFixtures;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private ServerFixtures serverFixtures;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private InstanceFixtures instanceFixtures;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationFixtures applicationFixtures;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void load() {
        userRepository.deleteAll();
        userRepository.save(userFixtures.albert());
        userRepository.save(userFixtures.nikola());

        applicationRepository.deleteAll();
        Application fdpApplication = applicationFixtures.fdpApplication();
        applicationRepository.save(fdpApplication);

        serverRepository.deleteAll();
        Server fdpServer = serverFixtures.fdpServer();
        serverRepository.save(fdpServer);

        instanceRepository.deleteAll();
        instanceRepository.save(instanceFixtures.stagingFdpInstance(fdpApplication, fdpServer));
    }

}
