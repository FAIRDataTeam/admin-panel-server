package nl.dtls.adminpanel.entity;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Application {

    @Id
    protected ObjectId id;

    protected String uuid;

    protected String name;

    protected String deployCommand;

    protected String disposeCommand;

    protected List<Template> templates;

    protected String formSpec;

    public Application(String uuid, String name, String deployCommand, String disposeCommand,
        List<Template> templates, String formSpec) {
        this.uuid = uuid;
        this.name = name;
        this.deployCommand = deployCommand;
        this.disposeCommand = disposeCommand;
        this.templates = templates;
        this.formSpec = formSpec;
    }

}
