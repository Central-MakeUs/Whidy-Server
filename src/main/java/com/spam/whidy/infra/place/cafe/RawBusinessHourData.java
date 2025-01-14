package com.spam.whidy.infra.place.cafe;

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
        Pattern pattern = Pattern.compile("\\b\\d{2}:\\d{2}\\b");
        Matcher matcher = pattern.matcher(content);

        LocalTime startTime = null;
        LocalTime endTime = null;

        int count = 0;
        while (matcher.find()) {
            if (count == 0) {
                startTime = LocalTime.parse(matcher.group());
            } else if (count == 1) {
                String endTimeString = matcher.group();
                endTimeString = endTimeString.equals("24:00") ? "00:00" : endTimeString;
                endTime = LocalTime.parse(endTimeString);
                break;
            }
            count++;
        }
        return new LocalTime[]{startTime, endTime};
    }

    private DayOfWeek extractDayOfWeek() {
        return switch (dayOfWeek){
            case "월" -> DayOfWeek.MONDAY;
            case "화" -> DayOfWeek.TUESDAY;
            case "수" -> DayOfWeek.WEDNESDAY;
            case "목" -> DayOfWeek.THURSDAY;
            case "금" -> DayOfWeek.FRIDAY;
            case "토" -> DayOfWeek.SATURDAY;
            default -> DayOfWeek.SUNDAY;
        };
    }
}
