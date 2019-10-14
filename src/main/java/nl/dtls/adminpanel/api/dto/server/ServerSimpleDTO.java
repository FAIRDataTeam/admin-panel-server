package nl.dtls.adminpanel.api.dto.server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServerSimpleDTO {

    private String uuid;

    private String name;

    private String username;

    private String hostname;

}
