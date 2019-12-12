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
package nl.dtls.adminpanel.database.migration.development;

import javax.annotation.PostConstruct;
import nl.dtls.adminpanel.Profiles;
import nl.dtls.adminpanel.database.migration.development.application.ApplicationMigration;
import nl.dtls.adminpanel.database.migration.development.instance.InstanceMigration;
import nl.dtls.adminpanel.database.migration.development.pipeline.PipelineMigration;
import nl.dtls.adminpanel.database.migration.development.server.ServerMigration;
import nl.dtls.adminpanel.database.migration.development.user.UserMigration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile(Profiles.NON_PRODUCTION)
public class MigrationRunner {

    @Autowired
    private UserMigration userMigration;

    @Autowired
    private ApplicationMigration applicationMigration;

    @Autowired
    private ServerMigration serverMigration;

    @Autowired
    private InstanceMigration instanceMigration;

    @Autowired
    private PipelineMigration pipelineMigration;

    @PostConstruct
    public void run() {
        userMigration.runMigration();
        applicationMigration.runMigration();
        serverMigration.runMigration();
        instanceMigration.runMigration();
        pipelineMigration.runMigration();
    }

}
