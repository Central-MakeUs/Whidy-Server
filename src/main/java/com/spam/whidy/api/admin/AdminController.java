package com.spam.whidy.api.admin;

import com.spam.whidy.application.place.PlaceDataCollectService;
import com.spam.whidy.application.place.PlaceRequestService;
import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.application.user.UserService;
import com.spam.whidy.common.config.auth.Auth;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.place.placeRequest.PlaceRequest;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import com.spam.whidy.dto.user.GrantRoleRequest;
import com.spam.whidy.dto.user.UserSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name = "관리자 페이지")
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final UserFinder userFinder;
    private final UserService userService;
    private final PlaceRequestService placeRequestService;
    private final PlaceDataCollectService placeDataCollectService;

    @GetMapping("/user/search")
    @Operation(summary = "사용자 목록 조회", description = "Super Admin 권한 필요")
    public List<User> searchUsers(@Auth LoginUser loginUser, UserSearchCondition condition){
        checkSuperAdmin(loginUser.getUserId());
        return userFinder.findByCondition(condition);
    }

    @PostMapping("/user/grant")
    @Operation(summary = "사용자 권한 부여", description = "Super Admin 권한 필요")
    public void grantPrivilegeToUser(@Auth LoginUser loginUser, @RequestBody @Valid GrantRoleRequest grantRoleRequest){
        checkSuperAdmin(loginUser.getUserId());
        userService.grantRole(grantRoleRequest.userId(), grantRoleRequest.role());
    }

    @GetMapping("/place-request/search")
    @Operation(summary = "요청된 장소 목록 조회", description = "사용자가 장소 등록을 요청한 목록을 조회. (Admin 권한 필요)")
    public List<PlaceRequest> searchPlaceRequest(@Auth LoginUser user, PlaceRequestSearchCondition condition){
        checkAdmin(user.getUserId());
        return placeRequestService.searchByCondition(condition);
    }

    @PostMapping("/place/register")
    @Operation(summary = "장소 추가 (논의필요)", description = "사용자가 요청한 장소를 등록. (Admin 권한 필요)")
    public void registerPlace(@Auth LoginUser loginUser){
        checkAdmin(loginUser.getUserId());
    }

    @PostMapping("/place/collect")
    @Operation(summary = "장소 크롤링 api (테스트용)", description = "테스트용 api")
    public void collect(@Auth LoginUser loginUser){
        checkAdmin(loginUser.getUserId());
        placeDataCollectService.collectAll();
    }

    private void checkSuperAdmin(Long userId){
        Optional<User> user = userFinder.findById(userId);
        if(user.isEmpty() || !user.get().getRole().isSuperAdmin()){
            throw new BadRequestException(ExceptionType.FORBIDDEN);
        }
    }

    private void checkAdmin(Long userId){
        Optional<User> user = userFinder.findById(userId);
        if(user.isEmpty() || !user.get().getRole().isAdmin()){
            throw new BadRequestException(ExceptionType.FORBIDDEN);
        }
    }
}
