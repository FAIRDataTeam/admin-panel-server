package nl.dtls.adminpanel.entity.pipeline;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.dtls.adminpanel.entity.instance.Instance;
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
public class Pipeline {

    @Id
    protected ObjectId id;

    protected String uuid;

    protected PipelineType type;

    protected PipelineStatus status;

    protected String log;

    protected LocalDateTime created;

    protected Long duration;

    @DBRef
    protected Instance instance;

    public Pipeline(String uuid, PipelineType type, PipelineStatus status, String log,
        Instance instance) {
        this.uuid = uuid;
        this.type = type;
        this.status = status;
        this.log = log;
        this.created = LocalDateTime.now();
        this.duration = null;
        this.instance = instance;
    }

}
