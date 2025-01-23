package com.spam.whidy.domain.user;

import com.spam.whidy.dto.user.UserSearchCondition;

import java.util.List;

public interface UserCustomRepository {

    List<User> searchByCondition(UserSearchCondition condition);
}
