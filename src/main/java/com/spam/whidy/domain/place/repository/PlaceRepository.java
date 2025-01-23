package com.spam.whidy.domain.place.repository;

import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceCustomRepository {
    List<Place> findByPlaceType(PlaceType placeType);

    Optional<Place> findByPlaceTypeAndId(PlaceType placeType, Long id);
}
