package com.spam.whidy.domain.place;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PlaceTest {

    private Place place;

    @BeforeEach
    void setUp() {
        place = Place.builder()
                .name("Test place")
                .address("Test address")
                .build();
    }

    @Test
    void addReview_ShouldUpdateReviewScoreAndCount() {
        place.addReview(4.0f);
        place.addReview(5.0f);

        assertEquals(2, place.getReviewNum());
        assertEquals(4.5f, place.getReviewScore(), 0.01);
    }

    @Test
    void removeReview_ShouldUpdateReviewScoreAndCount() {
        place.addReview(4.0f);
        place.addReview(5.0f);

        place.removeReview(4.0f);

        assertEquals(1, place.getReviewNum());
        assertEquals(5.0f, place.getReviewScore(), 0.01);
    }

    @Test
    void updateReview_ShouldUpdateReviewScore() {
        place.addReview(4.0f);
        place.addReview(5.0f);

        place.updateReview(4.0f, 3.0f);

        assertEquals(2, place.getReviewNum());
        assertEquals(4.0f, place.getReviewScore(), 0.01);
    }

    @Test
    void removeReview_LastReview_ShouldHandleZeroReviewScore() {
        place.addReview(5.0f);

        place.removeReview(5.0f);

        assertEquals(0, place.getReviewNum());
        assertNull(place.getReviewScore());
    }
}