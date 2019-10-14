package nl.dtls.adminpanel.entity;

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
public class Server {

    @Id
    protected ObjectId id;

    protected String uuid;

    protected String name;

    protected String username;

    protected String hostname;

    protected String privateKey;

    protected String publicKey;

    public Server(String uuid, String name, String username, String hostname, String privateKey,
        String publicKey) {
        this.uuid = uuid;
        this.name = name;
        this.username = username;
        this.hostname = hostname;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

}
