package com.spam.whidy.infra.auth.apple;


import com.spam.whidy.infra.auth.apple.response.AppleToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

public interface AppleApiClient {

    @PostExchange(url = "https://appleid.apple.com/auth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    AppleToken fetchToken(@RequestParam MultiValueMap<String, String> params);

}
