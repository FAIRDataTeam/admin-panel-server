package nl.dtls.adminpanel.database.repository;

import java.util.Optional;
import nl.dtls.adminpanel.entity.instance.Instance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstanceRepository extends MongoRepository<Instance, String> {

    Optional<Instance> findByUuid(String uuid);

}
