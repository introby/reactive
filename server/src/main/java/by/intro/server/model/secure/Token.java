package by.intro.server.model.secure;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
