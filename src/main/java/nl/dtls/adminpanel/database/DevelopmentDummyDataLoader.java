package nl.dtls.adminpanel.database;

import java.util.ArrayList;
import java.util.HashMap;
import nl.dtls.adminpanel.database.repository.ApplicationRepository;
import nl.dtls.adminpanel.database.repository.InstanceRepository;
import nl.dtls.adminpanel.database.repository.ServerRepository;
import nl.dtls.adminpanel.database.repository.UserRepository;
import nl.dtls.adminpanel.entity.Application;
import nl.dtls.adminpanel.entity.Instance;
import nl.dtls.adminpanel.entity.Server;
import nl.dtls.adminpanel.entity.Template;
import nl.dtls.adminpanel.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DevelopmentDummyDataLoader {

    @Value("${dummy.server.username:}")
    private String serverUsername;

    @Value("${dummy.server.hostname:}")
    private String serverHostname;

    @Value("${dummy.server.privateKey:}")
    private String serverPrivateKey;

    @Value("${dummy.server.publicKey:}")
    private String serverPublicKey;

    @Value("${dummy.instance.url:}")
    private String instanceUrl;

    @Value("${dummy.instance.jwtSecret:}")
    private String instanceJwtSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServerRepository serverRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void load() {
        userRepository.deleteAll();
        userRepository.save(admin());

        applicationRepository.deleteAll();
        Application fdpApplication = fdpApplication();
        applicationRepository.save(fdpApplication);

        serverRepository.deleteAll();
        Server fdpServer = fdpServer();
        serverRepository.save(fdpServer);

        instanceRepository.deleteAll();
        instanceRepository.save(stagingFdpInstance(fdpApplication, fdpServer));
    }

    public User admin() {
        return new User(
            "7e64818d-6276-46fb-8bb1-732e6e09f7e9",
            "Admin",
            "admin@example.com",
            passwordEncoder.encode("password")
        );
    }

    public Application fdpApplication() {
        return new Application(
            "31c170e2-f624-455b-93c7-e1b96156361c",
            "Fair Data Point",
            "deploy.sh",
            "dispose.sh",
            new ArrayList<>() {{
                add(new Template("deploy.sh",
                    ""
                        + "#!/bin/bash\n"
                        + "\n"
                        + "docker-compose -f {{instance_path}}/docker-compose.yml pull\n"
                        + "docker-compose -f {{instance_path}}/docker-compose.yml down\n"
                        + "docker-compose -f {{instance_path}}/docker-compose.yml up -d\n"
                ));
                add(new Template("dispose.sh",
                    ""
                        + "#!/bin/bash\n"
                        + "\n"
                        + "docker-compose -f {{instance_path}}/docker-compose.yml down\n"
                        + "rm -rf {{instance_path}}\n"

                ));
                add(new Template("docker-compose.yml",
                    ""
                        + "version: '3'\n"
                        + "services:\n"
                        + "  fdp:\n"
                        + "    image: {{server_image}}\n"
                        + "    ports:\n"
                        + "      - {{server_port}}:8080\n"
                        + "    volumes:\n"
                        + "      - {{instance_path}}/application-production"
                        + ".yml:/fdp/application-production.yml\n"
                        + "      - {{instance_path}}/_customization.scss:/fdp/scss/_customization"
                        + ".scss\n"
                        + "      - {{instance_path}}/_extra.scss:/fdp/scss/_extra.scss\n"
                ));
                add(new Template("application-production.yml",
                    ""
                        + "instance:\n"
                        + "  url: {{instance_url}}\n"
                        + "\n"
                        + "security:\n"
                        + "  jwt:\n"
                        + "    token:\n"
                        + "      secret-key: {{jwt_secret}}\n"
                        + "\n"
                        + "handlebars:\n"
                        + "  prefix: classpath:templates\n"
                        + "\n"
                        + "# valid repository type options {1 = inMemoryStore, 2 = NativeStore, 3"
                        + " = "
                        + "AllegroGraph, 4 = graphDB, 5 = blazegraph}\n"
                        + "repository:\n"
                        + "  type: {{repository_type}}\n"
                        + "  agraph:\n"
                        + "    url: {{repository_agraph_url}}\n"
                        + "    username: {{repository_agraph_user}}\n"
                        + "    password: {{repository_agraph_password}}\n"
                        + "  graphDb:\n"
                        + "    url: {{repository_graphDb_url}}\n"
                        + "    repository: {{repository_graphDb_repository}}\n"
                        + "  blazegraph:\n"
                        + "    url: {{repository_blazegraph_url}}\n"
                        + "    repository: {{repository_blazegraph_repository}}\n"
                        + "  native:\n"
                        + "    dir: {{repository_native_dir}}\n"
                        + "\n"
                        + "metadataProperties:\n"
                        + "  rootSpecs: {{metadata_rootSpecs}}\n"
                        + "  catalogSpecs: {{metadata_catalogSpecs}}\n"
                        + "  datasetSpecs: {{metadata_datasetSpecs}}\n"
                        + "  distributionSpecs: {{metadata_distributionSpecs}}\n"
                        + "  publisherURI: {{metadata_publisherURI}}\n"
                        + "  publisherName: {{metadata_publisherName}}\n"
                        + "  language: {{metadata_language}}\n"
                        + "  license: {{metadata_license}}\n"
                        + "  accessRightsDescription: {{metadata_accessRightsDescription}}\n"
                        + "\n"
                        + "fairSearch:\n"
                        + "  fdpSubmitUrl: {{fairSearch_fdpSubmitUrl}}\n"
                        + "\n"
                        + "metadataMetrics:\n"
                        + "  https://purl.org/fair-metrics/FM_F1A: https://www.ietf.org/rfc/rfc3986"
                        + ".txt\n"
                        + "  https://purl.org/fair-metrics/FM_A1.1: https://www.wikidata"
                        + ".org/wiki/Q8777\n"
                        + "\n"
                        + "# Valid pidSystem type options {1 = default PIDSystem, 2 = purl.org "
                        + "PIDSystem}\n"
                        + "pidSystem:\n"
                        + "  type: {{pidSystem_type}}\n"
                        + "  purl:\n"
                        + "    baseUrl: {{pidSystem_purl_baseUrl}}\n"
                        + "\n"
                        + "logging:\n"
                        + "  config: classpath:log4j2-production.xml\n"
                        + "\n"
                        + "scssDir: /fdp/scss\n"
                ));
                add(new Template("_customization.scss", "{{scss_customizations}}\n"));
                add(new Template("_extra.scss", "{{scss_extra}}\n"));
            }});
    }

    public Server fdpServer() {
        return new Server(
            "166c48ba-64d9-473a-8b59-7c1fdbb901f0",
            "FDP Server",
            serverUsername,
            serverHostname,
            serverPrivateKey,
            serverPublicKey);
    }

    public Instance stagingFdpInstance(Application fdpApplication,
        Server fdpServer) {
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
