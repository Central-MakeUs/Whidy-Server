package com.spam.whidy.infra.place.cafe;

import com.spam.whidy.common.util.DayOfWeekUtil;
import com.spam.whidy.common.util.HourUtil;
import com.spam.whidy.domain.place.BusinessHour;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawBusinessHourData {

    private String dayOfWeek;
    private String content;

    public BusinessHour toBusinessHour(){
        DayOfWeek dayOfWeek = extractDayOfWeek();
        content = content.replaceAll("\\n.*","");
        LocalTime[] times = extractTime(content);
        LocalTime start = times[0];
        LocalTime end = times[1];
        return new BusinessHour(dayOfWeek, start, end);
    }

    public static Set<BusinessHour> allDaySame(String content){
        LocalTime[] times = extractTime(content);
        LocalTime start = times[0];
        LocalTime end = times[1];
        return Arrays.stream(DayOfWeek.values()).map(d -> new BusinessHour(d, start, end)).collect(Collectors.toSet());
    }

    private static LocalTime[] extractTime(String content){
        return HourUtil.extractStartAndEndTime(content, ":");
    }

    private DayOfWeek extractDayOfWeek() {
        dayOfWeek = dayOfWeek.replaceAll("요일", "");
        return DayOfWeekUtil.convertKorToEnum(dayOfWeek);
    }
}
