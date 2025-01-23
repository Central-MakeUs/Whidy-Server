package com.spam.whidy.domain.place;

import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import com.spam.whidy.testConfig.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class PlaceRepositoryIntegrationTest extends IntegrationTest {

    @Autowired PlaceRepository placeRepository;

    @BeforeEach
    void setUp() {
        Set<BusinessHour> cafeBusinessHours = new HashSet<>();
        cafeBusinessHours.add(new BusinessHour(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));
        cafeBusinessHours.add(new BusinessHour(DayOfWeek.TUESDAY, LocalTime.of(9, 0), LocalTime.of(18, 0)));

        Set<BusinessHour> studyCafeBusinessHours = new HashSet<>();
        studyCafeBusinessHours.add(new BusinessHour(DayOfWeek.MONDAY, LocalTime.of(12, 0), LocalTime.of(20, 0)));

        Place cafe = Place.builder()
                .name("테스트 카페")
                .address("서울 강남구")
                .latitude(37.12345)
                .longitude(127.12345)
                .beveragePrice(5000)
                .reviewScore(4.5f)
                .placeType(PlaceType.GENERAL_CAFE)
                .businessHours(cafeBusinessHours)
                .build();

        Place studyCafe = Place.builder()
                .name("스터디 카페")
                .address("서울 서초구")
                .latitude(37.54321)
                .longitude(127.54321)
                .beveragePrice(4000)
                .reviewScore(4.0f)
                .placeType(PlaceType.STUDY_CAFE)
                .businessHours(studyCafeBusinessHours)
                .build();

        placeRepository.save(cafe);
        placeRepository.save(studyCafe);
    }

    @Test
    @DisplayName("검색 조건으로 Place 조회 테스트")
    void searchByConditionTest() {
        PlaceSearchCondition condition = new PlaceSearchCondition(
                4,               // reviewScoreFrom
                5,               // reviewScoreTo
                4000,            // beverageFrom
                6000,            // beverageTo
                Set.of(PlaceType.GENERAL_CAFE), // type
                null,            // businessDayOfWeek
                null,            // businessTimeFrom
                null,            // businessTimeTo
                37.0,            // latitudeFrom
                127.0,           // longitudeFrom
                38.0,            // latitudeTo
                128.0            // longitudeTo
        );

        List<PlaceDTO> results = placeRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("테스트 카페");
    }

    @Test
    @DisplayName("월요일 오전 10시에 영업 중인 장소 조회")
    void searchOpenPlacesOnMondayMorning() {
        PlaceSearchCondition condition = new PlaceSearchCondition(
                null, null,  // 리뷰 점수
                null, null,  // 음료 가격
                Set.of(PlaceType.GENERAL_CAFE, PlaceType.STUDY_CAFE),  // 장소 타입
                Set.of(DayOfWeek.MONDAY),  // 요일
                LocalTime.of(10, 0),  // 영업 시작 시간
                LocalTime.of(11, 0),  // 영업 종료 시간
                37.0, 127.0,  // 위도 범위 (남서쪽)
                38.0, 128.0   // 위도 범위 (북동쪽)
        );

        List<PlaceDTO> results = placeRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("테스트 카페");
    }

    @Test
    @DisplayName("월요일 오후 1시에 영업 중인 장소 조회")
    void searchOpenPlacesOnMondayAfternoon() {
        PlaceSearchCondition condition = new PlaceSearchCondition(
                null, null,  // 리뷰 점수
                null, null,  // 음료 가격
                null,  // 장소 타입
                Set.of(DayOfWeek.MONDAY),  // 요일
                LocalTime.of(13, 0),  // 영업 시작 시간
                LocalTime.of(14, 0),  // 영업 종료 시간
                37.0, 127.0,  // 위도 범위 (남서쪽)
                38.0, 128.0   // 위도 범위 (북동쪽)
        );

        List<PlaceDTO> results = placeRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(2);
    }
}
