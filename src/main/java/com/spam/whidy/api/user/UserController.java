package com.spam.whidy.api.user;

import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.application.user.UserService;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserFinder userFinder;

    @GetMapping("/profile-image/{userId}")
    public String getProfileImageUrl(@PathVariable Long userId){
        User user = userFinder.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.USER_NOT_FOUND));
        return userService.updateAndReturnImageUrl(user);
    }
}


