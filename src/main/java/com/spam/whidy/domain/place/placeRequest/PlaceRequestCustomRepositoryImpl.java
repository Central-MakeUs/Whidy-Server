package com.spam.whidy.domain.place.placeRequest;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spam.whidy.domain.place.placeRequest.QPlaceRequest.placeRequest;

@Repository
@RequiredArgsConstructor
public class PlaceRequestCustomRepositoryImpl implements PlaceRequestCustomRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PlaceRequest> searchByCondition(PlaceRequestSearchCondition condition) {
        return queryFactory
                .selectFrom(placeRequest)
                .where(allConditions(condition))
                .fetch();
    }

    @Override
    public List<PlaceRequest> searchByUserAndCondition(Long userId, PlaceRequestSearchCondition condition){
        return queryFactory
                .selectFrom(placeRequest)
                .where(allConditions(condition))
                .where(placeRequest.createUser.eq(userId))
                .fetch();
    }

    private BooleanExpression[] allConditions(PlaceRequestSearchCondition condition) {
        return new BooleanExpression[]{
                nameContains(condition.name()),
                addressContains(condition.address()),
                processedEquals(condition.processed())
        };
    }

    private BooleanExpression nameContains(String name) {
        return (name != null && !name.isEmpty())
                ? placeRequest.name.contains(name)
                : null;
    }

    private BooleanExpression addressContains(String address) {
        return (address != null && !address.isEmpty())
                ? placeRequest.address.contains(address)
                : null;
    }

    private BooleanExpression processedEquals(Boolean processed) {
        return (processed != null)
                ? placeRequest.processed.eq(processed)
                : null;
    }
}
