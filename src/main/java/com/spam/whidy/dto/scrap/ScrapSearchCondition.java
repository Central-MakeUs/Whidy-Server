package com.spam.whidy.dto.scrap;

public record ScrapSearchCondition(

        String keyword,
        Integer offset,
        Integer limit

) { }
