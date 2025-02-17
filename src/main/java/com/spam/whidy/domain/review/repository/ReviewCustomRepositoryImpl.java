package com.spam.whidy.domain.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spam.whidy.domain.place.QPlace;
import com.spam.whidy.domain.review.QReview;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.domain.user.QUser.*;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spam.whidy.domain.place.QPlace.place;
import static com.spam.whidy.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory queryFactory;

    QReview review = QReview.review;

    @Override
    public List<ReviewDTO> searchByCondition(ReviewSearchCondition condition) {
        return queryFactory
                .select(Projections.fields(
                        ReviewDTO.class,
                        review.id,
                        review.placeId,
                        review.score,
                        review.isReserved,
                        review.waitTime,
                        review.companionType,
                        review.keywords,
                        review.visitPurposes,
                        user.id.as("userId"),
                        user.name.as("userName"),
                        user.profileImage.profileImageUrl.as("userProfileImage"),
                        place.name.as("placeName"),
                        place.thumbnail.as("placeThumbnail")
                ))
                .from(review)
                .leftJoin(user).on(user.id.eq(review.createUser))
                .leftJoin(place).on(place.id.eq(review.placeId))
                .leftJoin(review.keywords).fetchJoin()
                .leftJoin(review.visitPurposes).fetchJoin()
                .where(review.placeId.eq(condition.placeId()))
                .offset(condition.offset())
                .limit(condition.limit())
                .fetch();
    }

    @Override
    public List<ReviewDTO> searchByUserAndCondition(Long userId, ReviewSearchCondition condition) {
        return queryFactory
                .select(Projections.fields(
                        ReviewDTO.class,
                        review.id,
                        review.placeId,
                        review.score,
                        review.isReserved,
                        review.waitTime,
                        review.companionType,
                        review.keywords,
                        review.visitPurposes,
                        user.id.as("userId"),
                        user.name.as("userName"),
                        user.profileImage.profileImageUrl.as("userProfileImage"),
                        place.name.as("placeName"),
                        place.thumbnail.as("placeThumbnail")
                ))
                .leftJoin(user).on(user.id.eq(review.createUser))
                .leftJoin(place).on(place.id.eq(review.placeId))
                .leftJoin(review.keywords).fetchJoin()
                .leftJoin(review.visitPurposes).fetchJoin()
                .where(review.placeId.eq(condition.placeId()))
                .where(review.createUser.eq(userId))
                .offset(condition.offset())
                .limit(condition.limit())
                .fetch();
    }
}
