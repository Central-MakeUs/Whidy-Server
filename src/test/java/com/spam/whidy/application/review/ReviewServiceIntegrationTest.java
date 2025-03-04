package com.spam.whidy.application.review;

import com.spam.whidy.application.place.PlaceService;
import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.common.util.PointUtil;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.repository.PlaceRepository;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.domain.review.repository.ReviewRepository;
import com.spam.whidy.dto.review.ReviewRequest;
import com.spam.whidy.testConfig.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WithMockUser("1")
class ReviewServiceIntegrationTest extends IntegrationTest {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private UserFinder userFinder;
    @Autowired private PlaceService placeService;
    @Autowired private PlaceRepository placeRepository;

    private ReviewService reviewService;

    private Place place;

    @BeforeEach
    void setUp() {
        reviewService = new ReviewService(userFinder, placeService, reviewRepository);

        place = Place.builder()
                .name("Test Place")
                .address("Test Address")
                .coordinates(PointUtil.createPoint(37.5, 127.0))
                .reviewNum(0)
                .reviewScore(0.0f)
                .build();

        placeRepository.save(place);
    }

    @Test
    void saveReview_Success() {
        // given
        ReviewRequest request = new ReviewRequest(
                null,
                place.getId(),
                5.0f,
                Set.of(),
                true,
                null,
                Set.of(),
                null,
                "comment"
        );

        // when
        Long reviewId = reviewService.save(1L, request);

        // then
        Review savedReview = reviewRepository.findById(reviewId).orElseThrow();
        assertThat(savedReview).isNotNull();
        assertThat(savedReview.getScore()).isEqualTo(5.0f);
        assertThat(place.getReviewNum()).isEqualTo(1);
        assertThat(place.getReviewScore()).isEqualTo(5.0f);
    }

    @Test
    void saveReview_Conflict() {
        // given
        ReviewRequest request = new ReviewRequest(
                null,
                place.getId(),
                5.0f,
                Set.of(),
                true,
                null,
                Set.of(),
                null,
                "comment"
        );

        reviewService.save(1L, request);

        // when & then
        assertThatThrownBy(() -> reviewService.save(1L, request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.REVIEW_CONFLICT.getMessage());
    }

    @Test
    void updateReview_Success() {
        // given
        ReviewRequest request = new ReviewRequest(
                null,
                place.getId(),
                4.0f,
                Set.of(),
                true,
                null,
                Set.of(),
                null,
                "comment"
        );

        Long reviewId = reviewService.save(1L, request);

        ReviewRequest updateRequest = new ReviewRequest(
                reviewId,
                place.getId(),
                3.0f,
                Set.of(),
                true,
                null,
                Set.of(),
                null,
                "comment"
        );

        // when
        reviewService.update(1L, updateRequest);

        // then
        Review updatedReview = reviewRepository.findById(reviewId).orElseThrow();
        assertThat(updatedReview.getScore()).isEqualTo(3.0f);
        assertThat(place.getReviewScore()).isEqualTo(3.0f);
    }

    @Test
    void deleteReview_Success() {
        // given
        ReviewRequest request = new ReviewRequest(
                null,
                place.getId(),
                4.0f,
                Set.of(),
                true,
                null,
                Set.of(),
                null,
                "comment"
        );

        Long reviewId = reviewService.save(1L, request);

        // when
        reviewService.delete(1L, reviewId);

        // then
        assertThat(reviewRepository.findById(reviewId)).isEmpty();
        assertThat(place.getReviewNum()).isEqualTo(0);
        assertThat(place.getReviewScore()).isNull();
    }

    @Test
    void deleteReview_NotOwner() {
        // given
        ReviewRequest request = new ReviewRequest(
                null,
                place.getId(),
                4.0f,
                Set.of(),
                true,
                null,
                Set.of(),
                null,
                "comment"
        );

        Long reviewId = reviewService.save(1L, request);

        // when & then
        assertThatThrownBy(() -> reviewService.delete(2L, reviewId))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining(ExceptionType.FORBIDDEN.getMessage());
    }
}