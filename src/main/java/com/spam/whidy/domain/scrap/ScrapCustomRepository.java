package com.spam.whidy.domain.scrap;

import com.spam.whidy.dto.scrap.ScrapDTO;
import com.spam.whidy.dto.scrap.ScrapSearchCondition;

import java.util.List;

public interface ScrapCustomRepository {

    List<ScrapDTO> searchByConditionAndUser(Long userId, ScrapSearchCondition condition);

}

