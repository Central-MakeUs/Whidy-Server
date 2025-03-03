package com.spam.whidy.domain.review.repository;

import com.spam.whidy.domain.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {

    Optional<Review> findByCreateUserIdAndPlaceId(Long userId, Long placeId);
}
