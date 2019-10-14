package nl.dtls.adminpanel.api.dto.server;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServerChangeDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String hostname;

    @NotBlank
    private String privateKey;

    @NotBlank
    private String publicKey;

}
