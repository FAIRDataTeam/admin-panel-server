package nl.dtls.adminpanel.database.migration.development.pipeline;

import nl.dtls.adminpanel.database.migration.development.common.Migration;
import nl.dtls.adminpanel.database.migration.development.pipeline.data.PipelineFixtures;
import nl.dtls.adminpanel.database.repository.pipeline.PipelineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineMigration implements Migration {

    @Autowired
    private PipelineFixtures pipelineFixtures;

    @Autowired
    private PipelineRepository pipelineRepository;

    public void runMigration() {
        pipelineRepository.deleteAll();
        pipelineRepository.save(pipelineFixtures.pipeline());
    }

}

