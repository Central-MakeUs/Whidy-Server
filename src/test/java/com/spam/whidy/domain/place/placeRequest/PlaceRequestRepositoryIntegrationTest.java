package com.spam.whidy.domain.place.placeRequest;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import com.spam.whidy.testConfig.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlaceRequestRepositoryIntegrationTest extends IntegrationTest {

    @Autowired private PlaceRequestRepository placeRequestRepository;

    @BeforeEach
    void setUp() {
        PlaceRequest request1 = PlaceRequest.builder()
                .name("강남 카페")
                .address("서울 강남구")
                .latitude(37.12345)
                .longitude(127.12345)
                .processed(false)
                .build();

        PlaceRequest request2 = PlaceRequest.builder()
                .name("홍대 카페")
                .address("서울 마포구")
                .latitude(37.56789)
                .longitude(127.98765)
                .processed(true)
                .build();

        PlaceRequest request3 = PlaceRequest.builder()
                .name("서초 스터디룸")
                .address("서울 서초구")
                .latitude(37.45678)
                .longitude(127.65432)
                .processed(false)
                .build();

        placeRequestRepository.save(request1);
        placeRequestRepository.save(request2);
        placeRequestRepository.save(request3);
    }

    @Test
    @DisplayName("이름, 주소, 처리여부 조건 검색")
    void searchByConditionTest() {

        PlaceRequestSearchCondition condition = PlaceRequestSearchCondition.builder()
                .name("카페")
                .address("서울")
                .processed(true)
                .build();



        List<PlaceRequest> results = placeRequestRepository.searchByCondition(condition);


        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("홍대 카페");
        assertThat(results.get(0).isProcessed()).isTrue();
    }

    @Test
    @DisplayName("처리되지 않은 요청만 조회")
    void searchUnprocessedRequestsTest() {
        PlaceRequestSearchCondition condition = PlaceRequestSearchCondition.builder()
                .processed(false)
                .build();

        List<PlaceRequest> results = placeRequestRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(2);
        assertThat(results).extracting("processed").containsOnly(false);
    }

    @Test
    @DisplayName("이름 검색 조건만 적용")
    void searchByNameOnlyTest() {
        PlaceRequestSearchCondition condition = PlaceRequestSearchCondition.builder()
                .name("스터디룸")
                .build();

        List<PlaceRequest> results = placeRequestRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("서초 스터디룸");
    }

    @Test
    @DisplayName("조건이 없을 때 전체 데이터 조회 테스트")
    void searchWithoutConditionTest() {
        PlaceRequestSearchCondition condition = PlaceRequestSearchCondition.builder().build();

        List<PlaceRequest> results = placeRequestRepository.searchByCondition(condition);

        assertThat(results).hasSize(3);
    }
}