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
            "Admin Panel Testing Application",
            "deploy.sh",
            "pause.sh",
            "dispose.sh",
            new ArrayList<>() {{
                add(new Template("deploy.sh",
                    ""
                        + "#!/bin/bash\n"
                        + "\n"
                        + "echo \"Deploy started\" > /tmp/admin-panel-log.txt\n"
                        + "sleep 2\n"
                        + "echo \"Deploy ended\" >> /tmp/admin-panel-log.txt\n"

                ));
                add(new Template("pause.sh",
                    ""
                        + "#!/bin/bash\n"
                        + "\n"
                        + "echo \"Pause started\" > /tmp/admin-panel-log.txt\n"
                        + "sleep 2\n"
                        + "echo \"Pause ended\" >> /tmp/admin-panel-log.txt\n"

                ));
                add(new Template("dispose.sh",
                    ""
                        + "#!/bin/bash\n"
                        + "\n"
                        + "echo \"Dispose started\" > /tmp/admin-panel-log.txt\n"
                        + "sleep 2\n"
                        + "echo \"Dispose ended\" >> /tmp/admin-panel-log.txt\n"

                ));
                add(new Template("template.yml",
                    ""
                        + "instancePath: {{instance_path}}\n"
                        + "some variable: {{some_variable}}\n"
                ));
            }},
            "");
    }

}
