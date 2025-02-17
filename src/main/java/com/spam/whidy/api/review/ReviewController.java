package com.spam.whidy.api.review;

import com.spam.whidy.application.review.ReviewService;
import com.spam.whidy.common.config.auth.Auth;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewRequest;
import com.spam.whidy.dto.review.ReviewSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "장소 리뷰")
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping
    @Operation(summary = "장소 리뷰 리스트 조회")
    public List<ReviewDTO> findByPlaceId(@Valid ReviewSearchCondition condition){
        return reviewService.searchByCondition(condition);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "장소 리뷰 조회")
    public Review findById(@PathVariable Long reviewId){
        return reviewService.findById(reviewId);
    }

    @PostMapping
    @Operation(summary = "장소 리뷰 저장", description = "한 장소에 대한 두 번 이상의 리뷰 작성 시 409 에러 발생")
    public Long save(@Auth LoginUser loginUser, @RequestBody @Valid ReviewRequest request){
        return reviewService.save(loginUser.getUserId(), request);
    }

    @PutMapping
    @Operation(summary = "소 리뷰 업데이트")
    public void update(@Auth LoginUser loginUser, @RequestBody @Valid ReviewRequest request){
        reviewService.update(loginUser.getUserId(), request);
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "장소 리뷰 삭제")
    public void delete(@Auth LoginUser loginUser, @PathVariable Long reviewId){
        reviewService.delete(loginUser.getUserId(), reviewId);
    }
}
