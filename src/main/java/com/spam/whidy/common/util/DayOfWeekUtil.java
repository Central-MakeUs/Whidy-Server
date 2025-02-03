package com.spam.whidy.common.util;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;

import java.time.DayOfWeek;

public class DayOfWeekUtil {

    public static DayOfWeek convertKorToEnum(String dayOfWeek){
        return switch (dayOfWeek){
            case "월" -> DayOfWeek.MONDAY;
            case "화" -> DayOfWeek.TUESDAY;
            case "수" -> DayOfWeek.WEDNESDAY;
            case "목" -> DayOfWeek.THURSDAY;
            case "금" -> DayOfWeek.FRIDAY;
            case "토" -> DayOfWeek.SATURDAY;
            case "일" -> DayOfWeek.SUNDAY;
            default -> throw new BadRequestException(ExceptionType.DAY_OF_WEEK_NOT_VALID);
        };
    }

    public static boolean isWeekend(DayOfWeek dayOfWeek){
        return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
    }
}
