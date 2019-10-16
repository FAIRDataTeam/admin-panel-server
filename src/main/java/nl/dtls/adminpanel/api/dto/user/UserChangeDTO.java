package nl.dtls.adminpanel.api.dto.user;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserChangeDTO {

    @NotBlank
    protected String name;

    @NotBlank
    protected String email;

}