package com.spam.whidy.domain.place.additionalInfo.cafe;

import com.spam.whidy.domain.place.additionalInfo.PlaceAdditionalInfo;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeAdditionalInfo implements PlaceAdditionalInfo {

    private List<Menu> menu;
}
