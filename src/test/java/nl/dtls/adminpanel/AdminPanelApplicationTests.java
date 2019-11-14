package nl.dtls.adminpanel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ActiveProfiles(Profiles.TESTING)
@SpringBootTest
public class AdminPanelApplicationTests {

    @Test
    public void contextLoads() {
    }

}
