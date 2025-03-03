package com.spam.whidy.application.review;

import com.spam.whidy.application.place.PlaceService;
import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.domain.review.repository.ReviewRepository;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewRequest;
import com.spam.whidy.dto.review.ReviewSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final UserFinder userFinder;
    private final PlaceService placeService;
    private final ReviewRepository reviewRepository;

    public List<ReviewDTO> searchByCondition(ReviewSearchCondition condition){
        List<Review> reviews = reviewRepository.searchByCondition(condition);
        return reviews.stream().map(ReviewDTO::of).collect(Collectors.toList());
    }

    public List<ReviewDTO> searchByUserAndCondition(Long userId, ReviewSearchCondition condition){
        List<Review> reviews = reviewRepository.searchByUserAndCondition(userId, condition);
        return reviews.stream().map(ReviewDTO::of).collect(Collectors.toList());
    }

    public Review findById(Long reviewId){
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.REVIEW_NOT_FOUND));
    }

    @Transactional
    public Long save(Long requestUserId, ReviewRequest request){
        Optional<Review> existingReview = reviewRepository.findByCreateUserIdAndPlaceId(requestUserId, request.placeId());
        if(existingReview.isPresent()){
            throw new BadRequestException(ExceptionType.REVIEW_CONFLICT);
        }

        Place place = placeService.findById(request.placeId());
        place.addReview(request.score());
        User user = userFinder.findById(requestUserId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.USER_NOT_FOUND));

        Review review = request.toEntity(place, user);
        reviewRepository.save(review);
        return review.getId();
    }

    @Transactional
    public void update(Long requestUserId, ReviewRequest request){
        Review previousReview = findReview(request.reviewId());
        if(!previousReview.isOwner(requestUserId)){
            throw new BadRequestException(ExceptionType.FORBIDDEN);
        }

        Place place = placeService.findById(previousReview.getPlace().getId());
        place.updateReview(previousReview.getScore(), request.score());

        Review newReview = request.toUpdateEntity();
        previousReview.update(newReview);
    }

    @Transactional
    public void delete(Long requestUserId, Long reviewId){
        Review review = findReview(reviewId);
        if(!review.isOwner(requestUserId)){
            throw new BadRequestException(ExceptionType.FORBIDDEN);
        }

        Place place = placeService.findById(review.getPlace().getId());
        place.removeReview(review.getScore());

        reviewRepository.delete(review);
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.REVIEW_NOT_FOUND));
    }

}
