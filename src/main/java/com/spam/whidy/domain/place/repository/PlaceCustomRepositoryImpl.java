package com.spam.whidy.domain.place.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static com.spam.whidy.domain.place.QPlace.place;
import static com.spam.whidy.domain.place.QBusinessHour.businessHour;

@Repository
@RequiredArgsConstructor
public class PlaceCustomRepositoryImpl implements PlaceCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceDTO> searchByCondition(PlaceSearchCondition condition) {
        return queryFactory
                .select(selectPlaceDTO())
                .from(place)
                .leftJoin(place.businessHours, businessHour)
                .where(allConditions(condition))
                .distinct()
                .fetch();
    }

    private QBean<PlaceDTO> selectPlaceDTO() {
        return Projections.fields(
                PlaceDTO.class,
                place.id,
                place.name,
                place.address,
                place.latitude,
                place.longitude,
                place.beveragePrice,
                place.reviewScore,
                place.placeType
        );
    }

    private BooleanExpression[] allConditions(PlaceSearchCondition condition) {
        return new BooleanExpression[]{
                reviewScoreFrom(condition.reviewScoreFrom()),
                reviewScoreTo(condition.reviewScoreTo()),
                beverageFrom(condition.beverageFrom()),
                beverageTo(condition.beverageTo()),
                placeTypeIn(condition.placeType()),
                businessDayOfWeekIn(condition.businessDayOfWeek()),
                businessTimeFrom(condition.businessTimeFrom()),
                businessTimeTo(condition.businessTimeTo()),
                location(condition)
        };
    }

    private BooleanExpression reviewScoreFrom(Integer reviewScoreFrom) {
        return reviewScoreFrom != null ? place.reviewScore.goe(reviewScoreFrom) : null;
    }

    private BooleanExpression reviewScoreTo(Integer reviewScoreTo) {
        return reviewScoreTo != null ? place.reviewScore.loe(reviewScoreTo) : null;
    }

    private BooleanExpression beverageFrom(Integer beverageFrom) {
        return beverageFrom != null ? place.beveragePrice.goe(beverageFrom) : null;
    }

    private BooleanExpression beverageTo(Integer beverageTo) {
        return beverageTo != null ? place.beveragePrice.loe(beverageTo) : null;
    }

    private BooleanExpression placeTypeIn(Set<PlaceType> types) {
        return types != null && !types.isEmpty() ? place.placeType.in(types) : null;
    }

    private BooleanExpression businessDayOfWeekIn(Set<DayOfWeek> days) {
        return days != null && !days.isEmpty() ? businessHour.dayOfWeek.in(days) : null;
    }

    private BooleanExpression businessTimeFrom(LocalTime fromTime) {
        return fromTime != null ? businessHour.openTime.loe(fromTime) : null;
    }

    private BooleanExpression businessTimeTo(LocalTime toTime) {
        return toTime != null ? businessHour.closeTime.goe(toTime) : null;
    }

    private BooleanExpression location(PlaceSearchCondition condition) {
        double latitudeFrom = condition.latitudeFrom();
        double latitudeTo = condition.latitudeTo();
        double longitudeFrom = condition.longitudeFrom();
        double longitudeTo = condition.longitudeTo();
        return place.latitude.between(latitudeFrom, latitudeTo)
                .and(place.longitude.between(longitudeFrom, longitudeTo));
    }


}
