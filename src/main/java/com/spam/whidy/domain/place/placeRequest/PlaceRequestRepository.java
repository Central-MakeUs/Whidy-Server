package com.spam.whidy.domain.place.placeRequest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRequestRepository extends JpaRepository<PlaceRequest, Long>, PlaceRequestCustomRepository {

}
