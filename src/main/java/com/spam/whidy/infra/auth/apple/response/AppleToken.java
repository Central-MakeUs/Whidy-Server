package com.spam.whidy.infra.auth.apple.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleToken {

    private String accessToken;
    private String idToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;

}