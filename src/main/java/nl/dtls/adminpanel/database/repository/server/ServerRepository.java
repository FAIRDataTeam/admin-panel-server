package nl.dtls.adminpanel.database.repository.server;

import java.util.Optional;
import nl.dtls.adminpanel.entity.server.Server;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerRepository extends MongoRepository<Server, String> {

    Optional<Server> findByUuid(String uuid);

}
