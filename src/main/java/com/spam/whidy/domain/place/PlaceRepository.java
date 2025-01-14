package com.spam.whidy.domain.place;

import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceCustomRepository {
    List<Place> findByPlaceType(PlaceType placeType);

    Optional<Place> findByPlaceTypeAndId(PlaceType placeType, Long id);
}
