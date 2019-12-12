/**
 * The MIT License
 * Copyright © ${project.inceptionYear} DTL
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
