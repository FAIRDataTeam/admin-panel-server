package nl.dtls.adminpanel.api.dto.instance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstanceSimpleDTO {

    private String uuid;

    private String name;

    private String url;

}
