package com.spam.whidy.common.exception.handler;

import com.spam.whidy.common.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
public record ErrorResponse (
        int code,
        String message,
        Map<String, String> argumentMessages
){
    public static ErrorResponse badRequestError(BadRequestException e) {
        return new ErrorResponse(e.getCode(), e.getMessage(), null);
    }

    public static ErrorResponse serverError() {
        return new ErrorResponse(500, "INTERNAL_SERVER_ERROR", null);
    }

    public static ErrorResponse noResourceError(){
        return new ErrorResponse(404, "잘못된 요청입니다.", null);
    }

    public static ErrorResponse illegalArgumentError(Map<String, String> argumentMessages) {
        return new ErrorResponse(400, "잘못된 요청입니다.", argumentMessages);
    }

}
