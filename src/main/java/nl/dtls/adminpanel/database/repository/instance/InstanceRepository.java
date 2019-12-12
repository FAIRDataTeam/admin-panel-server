package nl.dtls.adminpanel.database.repository.instance;

import java.util.List;
import java.util.Optional;
import nl.dtls.adminpanel.entity.application.Application;
import nl.dtls.adminpanel.entity.instance.Instance;
import nl.dtls.adminpanel.entity.server.Server;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstanceRepository extends MongoRepository<Instance, String> {

    Optional<Instance> findByUuid(String uuid);

    List<Instance> findByServer(Server server);

    List<Instance> findByApplication(Application application);

}
