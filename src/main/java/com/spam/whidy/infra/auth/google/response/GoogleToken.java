package com.spam.whidy.infra.auth.google.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleToken {

    private String accessToken;
    private String idToken;
    private String refreshToken;
    private String scope;
    private String tokenType;
    private Long expiresIn;


}