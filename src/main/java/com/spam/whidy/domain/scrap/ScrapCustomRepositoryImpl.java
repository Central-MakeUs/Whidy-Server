package com.spam.whidy.domain.scrap;

import static com.querydsl.core.types.dsl.Expressions.numberTemplate;
import static com.spam.whidy.domain.place.QPlace.place;
import static com.spam.whidy.domain.scrap.QScrap.scrap;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.scrap.ScrapDTO;
import com.spam.whidy.dto.scrap.ScrapSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ScrapCustomRepositoryImpl implements ScrapCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ScrapDTO> searchByConditionAndUser(Long userId, ScrapSearchCondition condition) {
        JPAQuery<ScrapDTO> query = queryFactory
                .select(scrapDtoProjection())
                .from(scrap)
                .join(place).on(scrap.placeId.eq(place.id))
                .where(allConditions(userId, condition));

        if (condition.limit() != null && condition.offset() != null) {
            query.limit(condition.limit());
            query.offset(condition.offset());
        }

        return query.fetch();
    }

    private ConstructorExpression<ScrapDTO> scrapDtoProjection() {
        return Projections.constructor(
                ScrapDTO.class,
                scrap.id,
                placeDtoProjection()
        );
    }

    private QBean<PlaceDTO> placeDtoProjection() {
        return Projections.fields(
                PlaceDTO.class,
                place.id,
                place.name,
                place.address,
                numberTemplate(Double.class, "ST_X({0})", place.coordinates).as("latitude"),
                numberTemplate(Double.class, "ST_Y({0})", place.coordinates).as("longitude"),
                place.beveragePrice,
                place.reviewNum,
                place.reviewScore,
                place.thumbnail,
                place.placeType
        );
    }

    private BooleanExpression allConditions(Long userId, ScrapSearchCondition condition) {
        return isUserOwner(userId)
                .and(keywordContains(condition.keyword()));
    }

    private BooleanExpression isUserOwner(Long userId) {
        return scrap.createUser.eq(userId);
    }


    private BooleanExpression keywordContains(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return place.name.containsIgnoreCase(keyword)
                .or(place.address.containsIgnoreCase(keyword));
    }
}
