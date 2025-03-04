package com.spam.whidy.dto.place;

import com.spam.whidy.api.common.ExcelColumn;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.common.util.DayOfWeekUtil;
import com.spam.whidy.common.util.HourUtil;
import com.spam.whidy.common.util.PointUtil;
import com.spam.whidy.domain.place.BusinessHour;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.domain.place.additionalInfo.FreeSpaceAdditionalInfo;
import com.spam.whidy.domain.place.additionalInfo.PlaceAdditionalInfo;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
// 장소 등록 시 사용되는 dto
public class PlaceRegisterDTO {

    @ExcelColumn(name = "이름")
    private String name;
    @ExcelColumn(name = "장소 종류 (공부공간 or 면접복장 or 면접사진)")
    private String placeType;
    @ExcelColumn(name = "위도")
    private Double latitude;
    @ExcelColumn(name = "경도")
    private Double longitude;
    @ExcelColumn(name = "주소")
    private String address;
    @ExcelColumn(name = "평일 영업시간 (09:00~18:00)")
    private String hoursWeekdays;
    @ExcelColumn(name = "주말 영업시간 (09:00~18:00)")
    private String hoursWeekend;
    @ExcelColumn(name = "영업일 (월,화,수)")
    private String openDays;
    @ExcelColumn(name = "휴무일 (토,일)")
    private String closedDays;
    @ExcelColumn(name = "음료가격 (3000)")
    private Double beveragePrice;

    // 무료 공부공간, 면접복장, 면접 사진 특화
    @ExcelColumn(name = "자리 유형 (자유기입)")
    private String seatingTypes;
    @ExcelColumn(name = "시설 및 프로그램 (자유기입)")
    private String amenities;
    @ExcelColumn(name = "기본제공 (자유기입)")
    private String amenitiesBasic;
    @ExcelColumn(name = "설명")
    private String description;
    @ExcelColumn(name = "예약 필수 여부 (Y 혹은 N)")
    private String reservationRequired;
    @ExcelColumn(name = "연락처")
    private String contact;
    @ExcelColumn(name = "홈페이지")
    private String homepage;

    public Place toEntity(){
        return Place.builder()
                .name(name)
                .address(address)
                .coordinates(PointUtil.createPoint(latitude, longitude))
                .beveragePrice(beveragePrice.intValue())
                .placeType(getPlaceType())
                .additionalInfo(makeAdditionalInfo())
                .businessHours(extractBusinessHours())
                .build();
    }

    private Set<BusinessHour> extractBusinessHours() {
        Set<BusinessHour> result = new HashSet<>();
        for(String dayOfWeekString : openDays.split(",")){
            DayOfWeek dayOfWeek = extractDayOfWeek(dayOfWeekString);
            LocalTime[] startAndEndTime = extractHour(dayOfWeek);
            result.add(new BusinessHour(dayOfWeek, startAndEndTime[0], startAndEndTime[1]));
        }
        return result;
    }

    private DayOfWeek extractDayOfWeek(String dayOfWeek) {
        dayOfWeek = dayOfWeek.replaceAll(" ","");
        return  DayOfWeekUtil.convertKorToEnum(dayOfWeek);
    }

    private LocalTime[] extractHour(DayOfWeek dayOfWeek) {
        String businessHour = DayOfWeekUtil.isWeekend(dayOfWeek) ? hoursWeekend : hoursWeekdays;
        if(StringUtils.isEmpty(businessHour)) {
            return new LocalTime[]{null, null};
        }
        return HourUtil.extractStartAndEndTime(businessHour, ":");
    }

    private PlaceAdditionalInfo makeAdditionalInfo() {
        return FreeSpaceAdditionalInfo.builder()
                .seatingTypes(seatingTypes)
                .amenities(amenities)
                .amenitiesBasic(amenitiesBasic)
                .description(description)
                .reservationRequired(reservationRequired.equals("Y"))
                .contact(contact)
                .homepage(homepage)
                .build();
    }

    private PlaceType getPlaceType() {
        return switch (placeType){
            case "공부공간" -> PlaceType.FREE_STUDY_SPACE;
            case "면접복장" -> PlaceType.FREE_CLOTHES_RENTAL;
            case "면접사진" -> PlaceType.FREE_PICTURE;
            default -> throw new BadRequestException(ExceptionType.PLACE_TYPE_NOT_VALID);
        };
    }

}