package com.spam.whidy.infra.place.cafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CafeReview {
    private String content;
    private int num;
}
