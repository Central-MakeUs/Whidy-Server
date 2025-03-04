package com.spam.whidy.domain.place.additionalInfo.cafe;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu {
    private String name;
    private String price;
    private String image;
}
