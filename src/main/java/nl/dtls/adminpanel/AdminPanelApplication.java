package nl.dtls.adminpanel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminPanelApplication {

    public static void main(String[] args) {
        String property = System.getProperties().getProperty("spring.profiles.active");
        if (property == null) {
            System.setProperty("spring.profiles.active", Profiles.PRODUCTION);
        }
        SpringApplication.run(AdminPanelApplication.class, args);
    }

}
