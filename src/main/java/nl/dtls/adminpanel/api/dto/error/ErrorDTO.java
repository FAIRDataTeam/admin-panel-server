package nl.dtls.adminpanel.api.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorDTO {

    @JsonProperty
    private long timestamp;

    @JsonProperty
    private int status;

    @JsonProperty
    private String error;

    @JsonProperty()
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Map<String, ?> errors;

    @JsonProperty
    private String message;

    @JsonProperty
    private String path;

    public ErrorDTO(HttpStatus status, String message, Map<String, ?> errors) {
        this.timestamp = new Date().getTime();
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.errors = errors;
    }

    public ErrorDTO(HttpStatus status, String message) {
        this(status, message, null);
    }


}