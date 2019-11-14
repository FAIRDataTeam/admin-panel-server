package nl.dtls.adminpanel.database.migration.development.user.data;

import nl.dtls.adminpanel.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserFixtures {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User albert() {
        return new User(
            "7e64818d-6276-46fb-8bb1-732e6e09f7e9",
            "Albert Einstein",
            "albert.einstein@example.com",
            passwordEncoder.encode("password")
        );
    }

    public User nikola() {
        return new User(
            "b5b92c69-5ed9-4054-954d-0121c29b6800",
            "Nikola Tesla",
            "nikola.tesla@example.com",
            passwordEncoder.encode("password")
        );
    }

    public User isaac() {
        return new User(
            "8d1a4c06-bb0e-4d03-a01f-14fa49bbc152",
            "Isaac Newton",
            "isaac.newton@example.com",
            passwordEncoder.encode("password")
        );
    }

}
