package com.spam.whidy.api.auth;

import com.spam.whidy.application.auth.exception.SignInFailException;
import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.domain.auth.AuthToken;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.domain.user.UserRepository;
import com.spam.whidy.dto.auth.SignOutRequest;
import com.spam.whidy.dto.auth.SignUpRequest;
import com.spam.whidy.dto.auth.SignInResponse;
import com.spam.whidy.application.auth.AuthService;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@Tag(name = "로그인")
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthTokenService authTokenService;
    private final UserRepository userRepository;

    @Value("${oauth.url.success}")
    private String successUrl;
    @Value("${oauth.url.fail}")
    private String failUrl;

    @GetMapping("/delete/{email}")
    @Operation(summary = "email 로 계정 삭제. 테스트용 api")
    public void delete(@PathVariable String email){
        Optional<User> user = userRepository.findByEmail(email);
        user.ifPresent(value -> userRepository.deleteById(value.getId()));
    }

    @RequestMapping(value = "/callback/{oauthType}", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "oauth callback")
    public void login(@PathVariable OAuthType oauthType, @RequestParam @NotNull String code, HttpServletResponse response) throws Exception {
        try {
            SignInResponse tokens = authService.signIn(oauthType, code);
            String param = String.format("?accessToken=%s&refreshToken=%s", tokens.authToken().getAccessToken(), tokens.authToken().getRefreshToken());
            response.sendRedirect(successUrl + param);
        } catch (SignInFailException e) {
            String param = String.format("?signUpCode=%s", e.getSignUpCode());
            response.sendRedirect(failUrl + param);
        }
    }

    @PostMapping("/sign-up")
    @Operation(summary = "oauth 계정으로 회원가입")
    public SignInResponse signup(@RequestBody @Valid SignUpRequest request) {
        return authService.signUp(request.signUpCode(), request.email(), request.name());
    }

    @PostMapping("/sign-out")
    @Operation(summary = "로그아웃")
    public void signOut(@RequestBody @Valid SignOutRequest request) {
        authTokenService.makeTokenInvalid(request);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "토큰 갱신")
    public AuthToken refreshToken(HttpServletRequest request){
        return authTokenService.refreshToken(request);
    }


    @GetMapping("/{oauthType}")
    @Operation(summary = "oauth 페이지로 redirect",
            description = "[로그인 성공 시] <b>whidy://home</b> 으로 redirect. <b>accessToken</b> 과 <b>refresh accessToken</b> 은 parameter 에 포함." +
                    " <br>[로그인 실패 시] <b>whidy://sign-up</b> 으로 redirect 되며, parameter 에 <b>signUpCode</b> 포함됨. " +
                    " <b>signUpCode</b> 는 회원가입 시 사용된다.")
    public void redirectToOauthCodeRequestPage(@PathVariable OAuthType oauthType, HttpServletResponse response) throws IOException {
        String pageUrl = authService.getOauthCodeRequestPageUrl(oauthType);
        response.sendRedirect(pageUrl);
    }

}
