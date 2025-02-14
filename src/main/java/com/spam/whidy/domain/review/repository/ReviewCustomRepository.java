package com.spam.whidy.domain.review.repository;

import com.spam.whidy.domain.review.Review;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewSearchCondition;

import java.util.List;

public interface ReviewCustomRepository {
    List<ReviewDTO> searchByCondition(ReviewSearchCondition condition);
    List<ReviewDTO> searchByUserAndCondition(Long userId, ReviewSearchCondition condition);
}
