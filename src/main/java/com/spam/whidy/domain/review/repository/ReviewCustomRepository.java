package com.spam.whidy.domain.review.repository;

import com.spam.whidy.domain.review.Review;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewSearchCondition;

import java.util.List;

public interface ReviewCustomRepository {
    List<Review> searchByCondition(ReviewSearchCondition condition);
    List<Review> searchByUserAndCondition(Long userId, ReviewSearchCondition condition);
}
