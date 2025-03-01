package com.spam.whidy.api.user;

import com.spam.whidy.application.place.PlaceRequestService;
import com.spam.whidy.application.review.ReviewService;
import com.spam.whidy.application.user.UserService;
import com.spam.whidy.common.config.auth.Auth;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.domain.place.placeRequest.PlaceRequest;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.dto.myPage.ProfileUpdateRequest;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewSearchCondition;
import com.spam.whidy.dto.user.ProfileResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Tag(name = "마이페이지")
@RequiredArgsConstructor
@RequestMapping("/api/my")
public class MyPageController {

    private final PlaceRequestService placeRequestService;
    private final ReviewService reviewService;
    private final UserService userService;

    @GetMapping("/place-request")
    @Operation(summary = "나의 장소 등록 요청 목록 조회")
    public List<PlaceRequest> findMyPlaceRequests(@Auth LoginUser loginUser, PlaceRequestSearchCondition condition){
        return placeRequestService.findByUserAndCondition(loginUser.getUserId(), condition);
    }

    @GetMapping("/review")
    @Operation(summary = "나의 장소 리뷰 목록 조회")
    public List<ReviewDTO> findMyReviews(@Auth LoginUser loginUser, ReviewSearchCondition condition){
        return reviewService.searchByUserAndCondition(loginUser.getUserId(), condition);
    }

    @PutMapping("/profile")
    @Operation(summary = "프로필 업데이트")
    public void updateName(@Auth LoginUser loginUser, @RequestBody @Valid ProfileUpdateRequest request){
        userService.updateProfile(loginUser.getUserId(), request.name());
    }

    @PatchMapping("/profile-image")
    @Operation(summary = "프로필 사진 업데이트")
    public void updateProfileImage(@Auth LoginUser loginUser, @RequestParam MultipartFile file){
        userService.updateProfileImage(loginUser.getUserId(), file);
    }

    @GetMapping("/profile")
    @Operation(summary = "프로필 조회")
    public ProfileResponse getProfile(@Auth LoginUser loginUser){
        return userService.getProfile(loginUser.getUserId());
    }

}
