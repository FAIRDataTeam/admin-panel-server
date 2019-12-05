package nl.dtls.adminpanel.database.migration.development.pipeline.data;

import nl.dtls.adminpanel.database.migration.development.instance.data.InstanceFixtures;
import nl.dtls.adminpanel.database.repository.instance.InstanceRepository;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import nl.dtls.adminpanel.entity.pipeline.PipelineType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PipelineFixtures {

    @Autowired
    private InstanceFixtures instanceFixtures;

    @Autowired
    private InstanceRepository instanceRepository;

    public Pipeline pipeline() {
        Instance instance = instanceRepository.findByUuid(instanceFixtures.fdpStaging().getUuid())
            .get();
        return new Pipeline(
            "052cb344-7d02-4074-996b-681ecf1d43b1",
            PipelineType.DEPLOY,
            PipelineStatus.DONE,
            "1. Creating directory - started\n1. Creating directory - ended\n2. Copy binary files"
                + " - started\n2. Copy binary files - ended",
            instance);
    }

}
