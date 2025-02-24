package com.spam.whidy.domain.scrap;
import com.spam.whidy.common.config.jpa.AuditorAwareImpl;
import com.spam.whidy.common.util.PointUtil;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.repository.PlaceRepository;
import com.spam.whidy.dto.scrap.ScrapDTO;
import com.spam.whidy.dto.scrap.ScrapSearchCondition;
import com.spam.whidy.testConfig.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Disabled
@WithMockUser("1")
class ScrapRepositoryIntegrationTest extends IntegrationTest {

        @Autowired private ScrapRepository scrapRepository;
        @Autowired private PlaceRepository placeRepository;

        @MockBean private AuditorAwareImpl auditorAware;

        private Place place1;
        private Place place2;
        private Scrap scrap1;
        private Scrap scrap2;

        @BeforeEach
        void setup() {
            when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(1L));
            place1 = Place.builder()
                    .name("Test 카페")
                    .address("test address")
                    .coordinates(PointUtil.createPoint(0.0, 0.0))
                    .build();
            placeRepository.save(place1);

            place2 = Place.builder()
                    .name("어쩌구 카페")
                    .address("어쩌구 address")
                    .coordinates(PointUtil.createPoint(0.0, 0.0))
                    .build();
            placeRepository.save(place2);

            scrap1 = Scrap.builder()
                    .placeId(place1.getId())
                    .build();

            scrap2 = Scrap.builder()
                    .placeId(place2.getId())
                    .build();

            scrapRepository.save(scrap1);
            scrapRepository.save(scrap2);
        }

        @Test
        void searchByConditionAndUser() {
            Long userId = 1L;
            ScrapSearchCondition condition = new ScrapSearchCondition(null, 0, 10);

            List<ScrapDTO> results = scrapRepository.searchByConditionAndUser(userId, condition);

            assertThat(results).hasSize(2);
        }


        @Test
        void searchByConditionAndUser_shouldReturnMatchingScrap1() {
            Long userId = 1L;
            ScrapSearchCondition condition = new ScrapSearchCondition("test", 0, 10);

            List<ScrapDTO> results = scrapRepository.searchByConditionAndUser(userId, condition);

            assertThat(results).hasSize(1);
            assertThat(results.get(0).scrapId()).isEqualTo(scrap1.getId());
            assertThat(results.get(0).place().getId()).isEqualTo(place1.getId());
        }

        @Test
        void searchByConditionAndUser_shouldReturnMatchingScrap2() {
            Long userId = 1L;
            ScrapSearchCondition condition = new ScrapSearchCondition("어쩌구", 0, 10);

            List<ScrapDTO> results = scrapRepository.searchByConditionAndUser(userId, condition);

            assertThat(results).hasSize(1);
            assertThat(results.get(0).scrapId()).isEqualTo(scrap2.getId());
            assertThat(results.get(0).place().getId()).isEqualTo(place2.getId());
        }

        @Test
        void searchByConditionAndUser_offsetAndLimit() {
            Long userId = 1L;
            ScrapSearchCondition condition = new ScrapSearchCondition(null, 1, 1);

            List<ScrapDTO> results = scrapRepository.searchByConditionAndUser(userId, condition);

            assertThat(results).hasSize(1);
            assertThat(results.get(0).scrapId()).isEqualTo(scrap2.getId());
            assertThat(results.get(0).place().getId()).isEqualTo(place2.getId());
        }

        @Test
        void searchByConditionAndUser_noResultsForNonMatchingUser() {
            Long userId = 99L; // 존재하지 않는 유저 ID
            ScrapSearchCondition condition = new ScrapSearchCondition("Test", 0, 10);

            List<ScrapDTO> results = scrapRepository.searchByConditionAndUser(userId, condition);

            assertThat(results).isEmpty();
        }
    }