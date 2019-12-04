package nl.dtls.adminpanel.service.deployment;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import nl.dtls.adminpanel.database.repository.InstanceRepository;
import nl.dtls.adminpanel.entity.Application;
import nl.dtls.adminpanel.entity.Instance;
import nl.dtls.adminpanel.entity.Server;
import nl.dtls.adminpanel.entity.Template;
import nl.dtls.shared.entity.exception.ResourceNotFoundException;
import nl.dtls.adminpanel.service.file.FileService;
import nl.dtls.adminpanel.service.ssh.SshService;
import nl.dtls.adminpanel.service.template.TemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

@Service
public class DeploymentService {

    private Logger logger = LoggerFactory.getLogger(DeploymentService.class);

    @Autowired
    private SshService sshService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private FileService fileService;

    @Autowired
    private InstanceRepository instanceRepository;

    public void deploy(String instanceUuid) throws IOException {
        Instance instance = instanceRepository.findByUuid(instanceUuid).orElseThrow(
            () -> new ResourceNotFoundException(
                format("Instance '%s' doesn't found", instanceUuid)));
        deploy(instance);
    }

    public void deploy(Instance instance) throws IOException {
        logger.info("Deployment - started");
        createDirectory(instance);
        copyBinaryFiles(instance);
        copyStringFiles(instance);
        changeDeployScriptToExecutable(instance);
        changeDisposeScriptToExecutable(instance);
        runDeployCommand(instance);
        logger.info("Deployment - ended");
    }

    public void dispose(String instanceUuid) throws IOException {
        Instance instance = instanceRepository.findByUuid(instanceUuid).orElseThrow(
            () -> new ResourceNotFoundException(
                format("Instance '%s' doesn't found", instanceUuid)));
        dispose(instance);
    }

    public void dispose(Instance instance) throws IOException {
        logger.info("Disposal - started");
        runDisposeCommand(instance);
        logger.info("Disposal - ended");
    }

    private void createDirectory(Instance instance) throws IOException {
        logger.info("1. Creating directory - started");
        Server server = instance.getServer();
        String createDirCommand = format("mkdir %s", instance.getPath());
        sshService.ssh(server, createDirCommand);
        logger.info("1. Creating directory - ended");
    }

    private void copyBinaryFiles(Instance instance) throws IOException {
        logger.info("2. Copy binary files - started");
        Server server = instance.getServer();
        Map<String, GridFsResource> files = loadBinaryFilesFromDB(instance);
        sshService.scpBinaryFiles(server, instance.getPath(), files);
        logger.info("2. Copy binary files - ended");
    }

    private void copyStringFiles(Instance instance) throws IOException {
        logger.info("3. Copy string files - started");
        Server server = instance.getServer();
        Map<String, String> templates = loadTemplates(instance);
        sshService.scpStringFiles(server, instance.getPath(), templates);
        logger.info("3. Copy string files - ended");
    }

    private void changeDeployScriptToExecutable(Instance instance) throws IOException {
        logger.info("4. Change deploy script to executable - started");
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String chmodCommand = format("chmod +x %s/%s", instance.getPath(),
            application.getDeployCommand());
        sshService.ssh(server, chmodCommand);
        logger.info("4. Change deploy script to executable - ended");
    }

    private void changeDisposeScriptToExecutable(Instance instance) throws IOException {
        logger.info("5. Change dispose script to executable - started");
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String chmodCommand = format("chmod +x %s/%s", instance.getPath(),
            application.getDisposeCommand());
        sshService.ssh(server, chmodCommand);
        logger.info("5. Change dispose script to executable - ended");
    }

    private void runDeployCommand(Instance instance) throws IOException {
        logger.info("6. Deploy - started");
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String command = format("%s/%s", instance.getPath(), application.getDeployCommand());
        sshService.ssh(server, command);
        logger.info("6. Deploy - ended");
    }

    private void runDisposeCommand(Instance instance) throws IOException {
        logger.info("1. Dispose - started");
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String command = format("%s/%s", instance.getPath(), application.getDisposeCommand());
        sshService.ssh(server, command);
        logger.info("1. Dispose - ended");
    }

    private Map<String, GridFsResource> loadBinaryFilesFromDB(Instance instance) {
        return instance.getFiles().entrySet()
            .stream()
            .collect(toMap(Entry::getKey, e -> fileService.getFile(e.getValue())));
    }

    private Map<String, String> loadTemplates(Instance instance) {
        Application application = instance.getApplication();
        Map<String, Object> templateVariables = instance.getVariables();
        templateVariables.put("instance_url", instance.getUrl());
        templateVariables.put("instance_path", instance.getPath());
        return
            application
                .getTemplates()
                .stream()
                .collect(
                    toMap(
                        Template::getName,
                        e -> templateService.render(e.getContent(), templateVariables)));
    }

}
