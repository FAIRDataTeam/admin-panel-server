package nl.dtls.adminpanel;

import static org.springframework.core.env.Profiles.of;

import nl.dtls.adminpanel.database.DevelopmentDummyDataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class AdminPanelApplication implements ApplicationRunner {

    @Autowired
    private DevelopmentDummyDataLoader developmentDummyDataLoader;

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        String property = System.getProperties().getProperty("spring.profiles.active");
        if (property == null) {
            System.setProperty("spring.profiles.active", Profiles.PRODUCTION);
        }
        SpringApplication.run(AdminPanelApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (environment.acceptsProfiles(of(Profiles.DEVELOPMENT))) {
            developmentDummyDataLoader.load();
        }
    }
}
