package nl.dtls.adminpanel.database.repository;

import java.util.Optional;
import nl.dtls.adminpanel.entity.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByUuid(String uuid);

    Optional<User> findByEmail(String email);

}
