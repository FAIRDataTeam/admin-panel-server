package nl.dtls.adminpanel.api.dto.instance;

import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InstanceChangeDTO {

    @NotBlank
    private String name;

    @Pattern(regexp = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+"
        + "([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$", message = "must be a "
        + "non-empty URL")
    private String url;

    @NotBlank
    private String path;

    @NotNull
    private Map<String, Object> variables;

    private String serverUuid;

    private String applicationUuid;

}
