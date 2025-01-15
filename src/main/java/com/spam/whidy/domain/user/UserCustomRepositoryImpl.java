package com.spam.whidy.domain.user;

import com.spam.whidy.dto.user.UserSearchCondition;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.spam.whidy.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> searchByCondition(UserSearchCondition condition) {
        return queryFactory
                .selectFrom(user)
                .where(
                        nameContains(condition.name()),
                        emailContains(condition.email()),
                        roleEq(condition.role()),
                        joinDateAfter(condition.joinDateFrom()),
                        joinDateBefore(condition.joinDateTo())
                )
                .fetch();
    }

    private BooleanExpression nameContains(String name) {
        return (name != null && !name.isEmpty()) ? user.name.contains(name) : null;
    }

    private BooleanExpression emailContains(String email) {
        return (email != null && !email.isEmpty()) ? user.email.contains(email) : null;
    }

    private BooleanExpression roleEq(Role role) {
        return role != null ? user.role.eq(role) : null;
    }

    private BooleanExpression joinDateAfter(LocalDateTime joinDateFrom) {
        return joinDateFrom != null ? user.joinDateTime.goe(joinDateFrom) : null;
    }

    private BooleanExpression joinDateBefore(LocalDateTime joinDateTo) {
        return joinDateTo != null ? user.joinDateTime.loe(joinDateTo) : null;
    }
}