package nl.dtls.adminpanel.database.repository;

import java.util.Optional;
import nl.dtls.adminpanel.entity.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    Optional<Application> findByUuid(String uuid);

}
