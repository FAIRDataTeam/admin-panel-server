package nl.dtls.adminpanel.service.ssh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import nl.dtls.adminpanel.entity.Server;
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
