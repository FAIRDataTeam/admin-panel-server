package nl.dtls.adminpanel.database.repository;

import java.util.Optional;
import nl.dtls.adminpanel.entity.Server;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerRepository extends MongoRepository<Server, String> {

    Optional<Server> findByUuid(String uuid);

}
