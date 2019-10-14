package nl.dtls.adminpanel.service.template;

import com.hubspot.jinjava.Jinjava;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {

    public String render(String template, Map<String, Object> context) {
        Jinjava jinjava = new Jinjava();
        return jinjava.render(template, context);
    }

}
