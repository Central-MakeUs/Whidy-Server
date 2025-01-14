package com.spam.whidy.domain.place;

import java.util.List;

public interface PlaceDataCollector {
    List<Place> collect();
    PlaceType dataType();
}
