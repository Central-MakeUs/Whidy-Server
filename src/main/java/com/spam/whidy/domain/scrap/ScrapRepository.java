package com.spam.whidy.domain.scrap;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


public interface ScrapRepository extends JpaRepository<Scrap, Long>, ScrapCustomRepository {

    Optional<Scrap> findByPlaceIdAndCreateUser(Long placeId, Long createUser);

}
