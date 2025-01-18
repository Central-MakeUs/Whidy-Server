package com.spam.whidy.application.auth;

import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.domain.auth.SignUpInfoRepository;
import com.spam.whidy.application.auth.exception.SignInFailException;
import com.spam.whidy.domain.auth.SignUpInfo;
import com.spam.whidy.domain.auth.StateRepository;
import com.spam.whidy.dto.auth.SignInResponse;
import com.spam.whidy.application.user.UserService;
import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.auth.AuthToken;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.memberClient.OauthUserClientComposite;
import com.spam.whidy.domain.auth.oauth.oauthCodeRequest.AuthCodeRequestUrlProviderComposite;
import com.spam.whidy.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final UserFinder userFinder;
    private final AuthTokenService authTokenService;
    private final OauthUserClientComposite oauthUserClientComposite;
    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final SignUpInfoRepository signUpInfoRepository;
    private final StateRepository stateRepository;

    public String getOauthCodeRequestPageUrl(OAuthType oauthType){
        String state = UUID.randomUUID().toString();
        return authCodeRequestUrlProviderComposite.getOauthCodeRequestUrl(oauthType, state);
    }

    public SignInResponse signIn(OAuthType oauthType, String code) {
        User oauthUser = oauthUserClientComposite.findOauthUser(oauthType, code);
        Optional<User> savedUser =  userFinder.findByAuthTypeAndAuthId(oauthType, oauthUser.getOauthId());

        if(savedUser.isEmpty()) {
            String signUpCode = UUID.randomUUID().toString();
            signUpInfoRepository.save(signUpCode, new SignUpInfo(oauthType, oauthUser.getOauthId()));
            throw new SignInFailException(signUpCode);
        }
        AuthToken token = authTokenService.createAuthToken(savedUser.get().getId());
        return new SignInResponse(token, savedUser.get().getId());
    }

    public SignInResponse signUp(String signUpCode, String email, String name){
        SignUpInfo signUpInfo = getSignUpInfo(signUpCode);
        checkUserValidity(email, signUpInfo);
        User newUser = User.of(email, name, signUpInfo.oauthType(), signUpInfo.oauthId());
        userService.save(newUser);
        AuthToken token = authTokenService.createAuthToken(newUser.getId());
        return new SignInResponse(token, newUser.getId());
    }

    private SignUpInfo getSignUpInfo(String signUpCode) {
        Optional<SignUpInfo> signUpInfo = signUpInfoRepository.findByCode(signUpCode);
        if(signUpInfo.isEmpty()){
            throw new BadRequestException(ExceptionType.SIGN_UP_CODE_NOT_VALID);
        }
        return signUpInfo.get();
    }

    private void checkUserValidity(String email, SignUpInfo signUpInfo) {
        Optional<User> sameOauthUser = userFinder.findByAuthTypeAndAuthId(signUpInfo.oauthType(), signUpInfo.oauthId());
        if(sameOauthUser.isPresent()) {
            throw new BadRequestException(ExceptionType.DUPLICATED_USER);
        }
        Optional<User> saveEmailUser = userFinder.findByEmail(email);
        if(saveEmailUser.isPresent()) {
            throw new BadRequestException(ExceptionType.DUPLICATED_EMAIL_USER);
        }
    }

    private void checkStateValidity(String sessionId, String state) {
        Optional<String> savedState = stateRepository.find(sessionId);
        if(savedState.isEmpty() || !savedState.get().equals(state)){
            throw new BadRequestException(ExceptionType.SIGN_UP_CODE_NOT_VALID);
        }
    }

}

