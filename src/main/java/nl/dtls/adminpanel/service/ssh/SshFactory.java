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
package nl.dtls.adminpanel.service.ssh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import nl.dtls.adminpanel.entity.server.Server;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

@Service
public class SshFactory {

    public SSHClient createSSHClient(Server server) throws IOException {
        SSHClient ssh = new SSHClient();
        ssh.addHostKeyVerifier(new PromiscuousVerifier());
        KeyProvider keys = ssh.loadKeys(server.getPrivateKey(), server.getPublicKey(), null);
        ssh.connect(server.getHostname());
        ssh.authPublickey(server.getUsername(), keys);
        return ssh;
    }

    public InMemorySourceFile createInMemoryFile(String fileName, String content) {
        byte[] tlsScriptBytes = content.getBytes(StandardCharsets.UTF_8);
        return new InMemorySourceFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public long getLength() {
                return tlsScriptBytes.length;
            }

            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(tlsScriptBytes);
            }
        };
    }

    public InMemorySourceFile createInMemoryFile(String fileName, GridFsResource file) {
        return new InMemorySourceFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public long getLength() {
                try {
                    return file.contentLength();
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }

            @Override
            public InputStream getInputStream() {
                try {
                    return file.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

}
