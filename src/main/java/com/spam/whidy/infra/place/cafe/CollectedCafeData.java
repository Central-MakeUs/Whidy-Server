package com.spam.whidy.infra.place.cafe;

import com.spam.whidy.domain.place.BusinessHour;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.domain.place.additionalInfo.PlaceAdditionalInfo;
import com.spam.whidy.domain.place.additionalInfo.cafe.CafeAdditionalInfo;
import com.spam.whidy.domain.place.additionalInfo.cafe.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollectedCafeData {

    private Long id;
    private String keyword;
    private String name;
    private String etc;
    private String phone;
    private String address;
    private double latitude;
    private double longitude;
    private Integer reviewNum;
    private List<RawBusinessHourData> times;
    private List<Menu> menuList;
    private List<CafeReview> reviews;
    private List<String> infos;

    private static final String CAFE_BEVERAGE_ITEM = "아메리카노";

    public Place toEntity(PlaceType type){
        return Place.builder()
                .name(name)
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .beveragePrice(extractBeveragePrice())
                .reviewScore(null)
                .additionalInfo(extractCafeAdditionalInfo())
                .businessHours(extractBusinessHour())
                .placeType(type)
                .build();
    }

    private Set<BusinessHour> extractBusinessHour() {
        try {
            if(times == null) return null;
            if(times.stream().anyMatch(time -> time.getDayOfWeek().contains("매일"))){
                return RawBusinessHourData.allDaySame(times.get(0).getContent());
            }
            return times.stream().filter(Objects::nonNull).map(RawBusinessHourData::toBusinessHour).collect(Collectors.toSet());
        }catch (Exception e){
            log.error("영업시간 데이터 변환 에러, message : {}", e.getMessage());
            return null;
        }
    }

    private PlaceAdditionalInfo extractCafeAdditionalInfo() {
        return CafeAdditionalInfo.builder()
                .menu(menuList)
                .build();
    }

    private Integer extractBeveragePrice() {
        if(menuList == null) return null;
        Optional<Menu> americano = menuList.stream().filter(m -> m.getName().contains(CAFE_BEVERAGE_ITEM)).findFirst();
        try {
            if(americano.isPresent()){
                String priceString = americano.get().getPrice()
                        .replaceAll("[가-힣\\s]", "") // 공백, 한글 버림
                        .replaceAll(",","");
                if(priceString.contains("~")) {
                    return Integer.parseInt(priceString.split("~")[0]);
                }
                return Integer.parseInt(priceString);
            }
            return null;
        }catch (Exception e){
            log.error("카페 데이터 변환 에러, 메뉴 : {}, message : {}", americano.get().getPrice(), e.getMessage());
            return null;
        }
    }

}
