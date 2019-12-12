package nl.dtls.adminpanel.database.repository.pipeline;

import java.util.List;
import java.util.Optional;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.pipeline.Pipeline;
import nl.dtls.adminpanel.entity.pipeline.PipelineStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PipelineRepository extends MongoRepository<Pipeline, String> {

    Optional<Pipeline> findByUuid(String uuid);

    List<Pipeline> findByStatusLike(PipelineStatus status);

    List<Pipeline> findByInstance(Instance instance);

}
