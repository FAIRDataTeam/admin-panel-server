package nl.dtls.adminpanel.service.deployment;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import nl.dtls.adminpanel.database.repository.pipeline.PipelineRepository;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeploymentWorker {

    private static final Logger log = LoggerFactory.getLogger(DeploymentWorker.class);

    @Autowired
    private PipelineRepository pipelineRepository;

    @Autowired
    private DeploymentService deploymentService;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        // 1. Get a job or quit
        List<Pipeline> pipelines = pipelineRepository.findByStatusLike(PipelineStatus.QUEUED);
        if (pipelines.size() == 0) {
            log.debug("No new job");
            return;
        }
        log.debug("No new appear");
        Pipeline pipeline = pipelines.get(0);
        changeStatusTo(pipeline, PipelineStatus.RUNNING);
        pipelineRepository.save(pipeline);

        // 2. Run the job
        try {
            switch (pipeline.getType()) {
                case DEPLOY:
                    deploymentService.deploy(pipeline);
                    break;
                case PAUSE:
                    deploymentService.pause(pipeline);
                    break;
                case DISPOSE:
                    deploymentService.dispose(pipeline);
                    break;
            }
            changeStatusTo(pipeline, PipelineStatus.DONE);
            computeDuration(pipeline);

        } catch (IOException e) {
            changeStatusTo(pipeline, PipelineStatus.ERROR);
            computeDuration(pipeline);
        }
    }

    private void changeStatusTo(Pipeline pipeline, PipelineStatus status) {
        pipeline.setStatus(status);
        pipelineRepository.save(pipeline);
        log.info("Changed pipeline '{}' to state {} (instanceUuid: {})", pipeline.getUuid(),
            pipeline.getType().name(), pipeline.getInstance().getUuid());
    }

    private void computeDuration(Pipeline pipeline) {
        long duration = ChronoUnit.SECONDS.between(pipeline.getCreated(), LocalDateTime.now());
        pipeline.setDuration(duration);
        pipelineRepository.save(pipeline);
    }

}
