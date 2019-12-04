package nl.dtls.adminpanel.database.migration.development.application.data;

import java.util.ArrayList;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.application.Template;
import org.springframework.stereotype.Service;

@Service
public class ApplicationFixtures {

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
            }},
            "");
    }

}
