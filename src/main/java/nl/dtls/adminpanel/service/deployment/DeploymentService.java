package nl.dtls.adminpanel.service.deployment;

import static java.lang.String.format;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import nl.dtls.adminpanel.database.repository.pipeline.PipelineRepository;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.application.Template;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.server.Server;
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
    private PipelineRepository pipelineRepository;

    public void deploy(Pipeline pipeline) throws IOException {
        log(pipeline, "Deployment - started");
        createDirectory(pipeline);
        copyBinaryFiles(pipeline);
        copyStringFiles(pipeline);
        changeDeployScriptToExecutable(pipeline);
        changePauseScriptToExecutable(pipeline);
        changeDisposeScriptToExecutable(pipeline);
        runDeployCommand(pipeline);
        log(pipeline, "Deployment - ended");
    }

    public void pause(Pipeline pipeline) throws IOException {
        log(pipeline, "Pause - started");
        runPauseCommand(pipeline);
        log(pipeline, "Pause - ended");
    }

    public void dispose(Pipeline pipeline) throws IOException {
        log(pipeline, "Disposal - started");
        runDisposeCommand(pipeline);
        log(pipeline, "Disposal - ended");
    }

    private void createDirectory(Pipeline pipeline) throws IOException {
        log(pipeline, "1. Create directory - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        String createDirCommand = format("mkdir %s", instance.getPath());
        sshService.ssh(server, createDirCommand);
        log(pipeline, "1. Create directory - ended");
    }

    private void copyBinaryFiles(Pipeline pipeline) throws IOException {
        log(pipeline, "2. Copy binary files - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Map<String, GridFsResource> files = loadBinaryFilesFromDB(instance);
        sshService.scpBinaryFiles(server, instance.getPath(), files);
        log(pipeline, "2. Copy binary files - ended");
    }

    private void copyStringFiles(Pipeline pipeline) throws IOException {
        log(pipeline, "3. Copy string files - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Map<String, String> templates = loadTemplates(instance);
        sshService.scpStringFiles(server, instance.getPath(), templates);
        log(pipeline, "3. Copy string files - ended");
    }

    private void changeDeployScriptToExecutable(Pipeline pipeline) throws IOException {
        log(pipeline, "4. Change deploy script to executable - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String chmodCommand = format("chmod +x %s/%s", instance.getPath(),
            application.getDeployCommand());
        sshService.ssh(server, chmodCommand);
        log(pipeline, "4. Change deploy script to executable - ended");
    }

    private void changePauseScriptToExecutable(Pipeline pipeline) throws IOException {
        log(pipeline, "5. Change pause script to executable - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String chmodCommand = format("chmod +x %s/%s", instance.getPath(),
            application.getPauseCommand());
        sshService.ssh(server, chmodCommand);
        log(pipeline, "5. Change pause script to executable - ended");
    }

    private void changeDisposeScriptToExecutable(Pipeline pipeline) throws IOException {
        log(pipeline, "6. Change dispose script to executable - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String chmodCommand = format("chmod +x %s/%s", instance.getPath(),
            application.getDisposeCommand());
        sshService.ssh(server, chmodCommand);
        log(pipeline, "6. Change dispose script to executable - ended");
    }

    private void runDeployCommand(Pipeline pipeline) throws IOException {
        log(pipeline, "7. Deploy - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String command = format("%s/%s", instance.getPath(), application.getDeployCommand());
        sshService.ssh(server, command);
        log(pipeline, "7. Deploy - ended");
    }

    private void runPauseCommand(Pipeline pipeline) throws IOException {
        log(pipeline, "1. Pause - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String command = format("%s/%s", instance.getPath(), application.getPauseCommand());
        sshService.ssh(server, command);
        log(pipeline, "1. Pause - ended");
    }

    private void runDisposeCommand(Pipeline pipeline) throws IOException {
        log(pipeline, "1. Dispose - started");
        Instance instance = pipeline.getInstance();
        Server server = instance.getServer();
        Application application = instance.getApplication();
        String command = format("%s/%s", instance.getPath(), application.getDisposeCommand());
        sshService.ssh(server, command);
        log(pipeline, "1. Dispose - ended");
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

    private void log(Pipeline pipeline, String message) {
        logger.info(message);
        pipeline.setLog(pipeline.getLog().concat(format("%s\n", message)));
        pipelineRepository.save(pipeline);
    }
}
