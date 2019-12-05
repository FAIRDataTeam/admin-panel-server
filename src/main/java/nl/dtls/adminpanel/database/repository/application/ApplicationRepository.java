package nl.dtls.adminpanel.database.repository.application;

import java.util.Optional;
import nl.dtls.adminpanel.entity.application.Application;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApplicationRepository extends MongoRepository<Application, String> {

    Optional<Application> findByUuid(String uuid);

}
