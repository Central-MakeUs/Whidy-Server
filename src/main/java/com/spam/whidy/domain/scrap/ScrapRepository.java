package com.spam.whidy.domain.scrap;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ScrapRepository extends JpaRepository<Scrap, Long>, ScrapCustomRepository {

}
