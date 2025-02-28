package com.spam.whidy.dto.scrap;

import com.spam.whidy.dto.place.PlaceDTO;

public record ScrapDTO(
        Long scrapId,
        PlaceDTO place
) {}
