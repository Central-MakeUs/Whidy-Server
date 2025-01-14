package com.spam.whidy.domain.place;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
                .select(Projections.fields(
                        PlaceDTO.class,
                        place.id,
                        place.name,
                        place.address,
                        place.latitude,
                        place.longitude,
                        place.beveragePrice,
                        place.reviewScore,
                        place.placeType
                ))
                .from(place)
                .leftJoin(place.businessHours, businessHour)
                .where(
                        reviewScoreFrom(condition.getReviewScoreFrom()),
                        reviewScoreTo(condition.getReviewScoreTo()),
                        beverageFrom(condition.getBeverageFrom()),
                        beverageTo(condition.getBeverageTo()),
                        placeTypeIn(condition.getType()),
                        businessDayOfWeekIn(condition.getBusinessDayOfWeek()),
                        businessTimeFrom(condition.getBusinessTimeFrom()),
                        businessTimeTo(condition.getBusinessTimeTo()),
                        location(condition)
                )
                .distinct()
                .fetch();
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
        double latitudeFrom = condition.getLatitudeFrom();
        double latitudeTo = condition.getLatitudeTo();
        double longitudeFrom = condition.getLongitudeFrom();
        double longitudeTo = condition.getLongitudeTo();
        return place.latitude.between(latitudeFrom, latitudeTo)
                .and(place.longitude.between(longitudeFrom, longitudeTo));
    }


}
