package com.spam.whidy.api.test;

import com.spam.whidy.application.test.TestService;
import com.spam.whidy.common.config.auth.Auth;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.domain.auth.SignUpInfo;
import com.spam.whidy.domain.auth.SignUpInfoRepository;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.test.Test;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

    private final TestService testService;
    private final LocalDateTime version = LocalDateTime.now();
    private final SignUpInfoRepository signUpInfoRepository;

    @GetMapping("/{string}")
    public Long test(@PathVariable String string){
        return testService.save(string);
    }

    @GetMapping("/find")
    public List<Test> findList(){
        return testService.findList();
    }

    @GetMapping("/whoami")
    public Long findList(@Auth LoginUser user){
        return user.getUserId();
    }

    @GetMapping("/version")
    public String version(){
        return version.toString();
    }

    @GetMapping("/save")
    public void save(){
        signUpInfoRepository.save("111",new SignUpInfo(OAuthType.KAKAO, "oauthId"));
    }

    @GetMapping("/find2")
    public void find(){
        SignUpInfo info = signUpInfoRepository.findByCode("111").get();
        System.out.println();
    }

}
