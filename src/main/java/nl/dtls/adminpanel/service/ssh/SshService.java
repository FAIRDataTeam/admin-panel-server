package nl.dtls.adminpanel.service.ssh;

import static java.lang.String.format;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.IOUtils;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.direct.Session.Command;
import net.schmizz.sshj.xfer.InMemorySourceFile;
import nl.dtls.adminpanel.entity.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

@Service
public class SshService {

    private Logger logger = LoggerFactory.getLogger(SshService.class);

    @Autowired
    private SshFactory sshFactory;

    public void ssh(Server server, String remoteCommand) throws IOException {
        SSHClient ssh = sshFactory.createSSHClient(server);
        try (Session session = ssh.startSession()) {
            Command cmd = session.exec(remoteCommand);
            String response = IOUtils.readFully(cmd.getInputStream()).toString();
            Integer status = cmd.getExitStatus();
            logger.info(format("Response: %s", response));
            logger.info(format("Status: %d", status));
        } finally {
            ssh.disconnect();
        }
    }

    public void scpStringFiles(Server server, String remoteLocalization, Map<String, String> files)
        throws IOException {
        SSHClient ssh = sshFactory.createSSHClient(server);
        try {
            ssh.useCompression();
            for (Entry<String, String> entry : files.entrySet()) {
                InMemorySourceFile file = sshFactory
                    .createInMemoryFile(entry.getKey(), entry.getValue());
                ssh.newSCPFileTransfer().upload(file, remoteLocalization);
            }
        } finally {
            ssh.disconnect();
        }
    }

    public void scpBinaryFiles(Server server, String remoteLocalization,
        Map<String, GridFsResource> files)
        throws IOException {
        SSHClient ssh = sshFactory.createSSHClient(server);
        try {
            ssh.useCompression();
            for (Entry<String, GridFsResource> entry : files.entrySet()) {
                InMemorySourceFile file = sshFactory
                    .createInMemoryFile(entry.getKey(), entry.getValue());
                ssh.newSCPFileTransfer().upload(file, remoteLocalization);
            }
        } finally {
            ssh.disconnect();
        }
    }

}
