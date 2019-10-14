package nl.dtls.adminpanel.entity;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Instance {

    @Id
    protected ObjectId id;

    protected String uuid;

    protected String name;

    protected String url;

    protected String path;

    protected Map<String, Object> variables;

    protected Map<String, ObjectId> files;

    @DBRef
    protected Application application;

    @DBRef
    protected Server server;

    public Instance(String uuid, String name, String url, String path,
        Map<String, Object> variables, Map<String, ObjectId> files, Application application,
        Server server) {
        this.uuid = uuid;
        this.name = name;
        this.url = url;
        this.path = path;
        this.variables = variables;
        this.files = files;
        this.application = application;
        this.server = server;
    }

}
