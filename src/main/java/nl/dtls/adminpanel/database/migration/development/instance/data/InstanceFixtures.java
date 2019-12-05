package nl.dtls.adminpanel.database.migration.development.instance.data;

import java.util.HashMap;
import nl.dtls.adminpanel.database.migration.development.application.data.ApplicationFixtures;
import nl.dtls.adminpanel.database.migration.development.server.data.ServerFixtures;
import nl.dtls.adminpanel.database.repository.application.ApplicationRepository;
import nl.dtls.adminpanel.database.repository.server.ServerRepository;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InstanceFixtures {

    @Value("${dummy.instance.url:}")
    private String instanceUrl;

    @Autowired
    private ApplicationFixtures applicationFixtures;

    @Autowired
    private ServerFixtures serverFixtures;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ServerRepository serverRepository;

    public Instance fdpStaging() {
        Application fdpApplication = applicationRepository
            .findByUuid(applicationFixtures.fdpApplication().getUuid()).get();
        Server fdpServer = serverRepository.findByUuid(serverFixtures.fdpServer().getUuid()).get();
        return new Instance(
            "6f29daa8-1c43-49c3-9d1b-dc422e333d1e",
            "Admin Panel Testing Instance",
            instanceUrl,
            "/tmp/ap",
            new HashMap<>() {{
                put("some_variable", "Some variable value");
            }},
            new HashMap<>() {{
//            put("logo.png", new ObjectId("5d9dcdea7c889a377583cfef"));
            }},
            fdpApplication,
            fdpServer);
    }

}
