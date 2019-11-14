package nl.dtls.adminpanel.database.migration.development.instance.data;

import java.util.HashMap;
import nl.dtls.adminpanel.database.migration.development.application.data.ApplicationFixtures;
import nl.dtls.adminpanel.database.migration.development.server.data.ServerFixtures;
import nl.dtls.adminpanel.database.repository.ApplicationRepository;
import nl.dtls.adminpanel.database.repository.ServerRepository;
import nl.dtls.adminpanel.entity.Application;
import nl.dtls.adminpanel.entity.Instance;
import nl.dtls.adminpanel.entity.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InstanceFixtures {

    @Value("${dummy.instance.url:}")
    private String instanceUrl;

    @Value("${dummy.instance.jwtSecret:}")
    private String instanceJwtSecret;

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
            "FDP Staging Instance",
            instanceUrl,
            "/tmp/fdp-test",
            new HashMap<>() {{
                put("server_image", "fairdata/fairdatapoint:develop");
                put("server_port", "81");
                put("jwt_secret", instanceJwtSecret);
                put("repository_type", 1);
                put("repository_agraph_url", "http://localhost:10035/repositories/fdp");
                put("repository_agraph_username", "user");
                put("repository_agraph_password", "password");
                put("repository_graphDb_url", "http://localhost:7200");
                put("repository_graphDb_repository", "test");
                put("repository_blazegraph_url", "http://localhost:8079/blazegraph");
                put("repository_blazegraph_repository", "fdp");
                put("repository_native_dir", "/tmp/fdp-store/");
                put("metadata_rootSpecs",
                    "https://www.purl.org/fairtools/fdp/schema/0.1/fdpMetadata");
                put("metadata_catalogSpecs",
                    "https://www.purl.org/fairtools/fdp/schema/0.1/catalogMetadata");
                put("metadata_datasetSpecs",
                    "https://www.purl.org/fairtools/fdp/schema/0.1/datasetMetadata");
                put("metadata_distributionSpecs",
                    "https://www.purl.org/fairtools/fdp/schema/0.1/distributionMetadata");
                put("metadata_publisherURI", "http://localhost");
                put("metadata_publisherName", "localhost");
                put("metadata_language", "http://id.loc.gov/vocabulary/iso639-1/en");
                put("metadata_license", "http://rdflicense.appspot.com/rdflicense/cc-by-nc-nd3.0");
                put("metadata_accessRightsDescription", "This resource has no access restriction");
                put("fairSearch_fdpSubmitUrl", "http://localhost:8080/fse/submitFdp");
                put("pidSystem_type", 1);
                put("pidSystem_purl_baseUrl", "http://purl.org/YOUR-PURL-DOMAIN/fdp");
                put("scss_customizations", "$color-primary: red");
                put("scss_extra", "body { background: pink; }");
            }},
            new HashMap<>() {{
//            put("logo.png", new ObjectId("5d9dcdea7c889a377583cfef"));
            }},
            fdpApplication,
            fdpServer);
    }

}
