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
package nl.dtls.adminpanel.database.migration.development.server.data;

import nl.dtls.adminpanel.entity.server.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ServerFixtures {

    @Value("${dummy.server.username:}")
    private String serverUsername;

    @Value("${dummy.server.hostname:}")
    private String serverHostname;

    @Value("${dummy.server.privateKey:}")
    private String serverPrivateKey;

    @Value("${dummy.server.publicKey:}")
    private String serverPublicKey;

    public Server fdpServer() {
        return new Server(
            "166c48ba-64d9-473a-8b59-7c1fdbb901f0",
            "FDP Server",
            serverUsername,
            serverHostname,
            serverPrivateKey,
            serverPublicKey);
    }

}
