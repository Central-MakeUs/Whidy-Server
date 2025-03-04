package com.spam.whidy.domain.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spam.whidy.domain.place.QPlace;
import com.spam.whidy.domain.review.QReview;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.domain.user.QUser;
import com.spam.whidy.domain.user.QUser.*;
import com.spam.whidy.dto.review.ReviewDTO;
import com.spam.whidy.dto.review.ReviewSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.core.group.GroupBy.set;
import static com.spam.whidy.domain.place.QPlace.place;
import static com.spam.whidy.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {

    private final JPAQueryFactory queryFactory;

    QReview review = QReview.review;

    @Override
    public List<Review> searchByCondition(ReviewSearchCondition condition) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .join(review.createUser, user).fetchJoin()
                .join(review.place, place).fetchJoin()
                .where(review.place.id.eq(condition.placeId()));

        if (condition.limit() != null && condition.offset() != null) {
            query.limit(condition.limit());
            query.offset(condition.offset());
        }

        return query.fetch();
    }

    @Override
    public List<Review> searchByUserAndCondition(Long userId, ReviewSearchCondition condition) {
        JPAQuery<Review> query = queryFactory
                .selectFrom(review)
                .join(review.createUser, user).fetchJoin()
                .join(review.place, place).fetchJoin()
                .where(review.createUser.id.eq(userId));

        if (condition.limit() != null && condition.offset() != null) {
            query.limit(condition.limit());
            query.offset(condition.offset());
        }

        return query.fetch();
    }
}
