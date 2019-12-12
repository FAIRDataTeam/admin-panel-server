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
