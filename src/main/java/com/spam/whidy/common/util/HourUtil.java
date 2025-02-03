package com.spam.whidy.common.util;

import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HourUtil {

    public static LocalTime[] extractStartAndEndTime(String content, String separator){
        Pattern pattern = Pattern.compile(String.format("\\b\\d{2}%s\\d{2}\\b",separator));
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
}
