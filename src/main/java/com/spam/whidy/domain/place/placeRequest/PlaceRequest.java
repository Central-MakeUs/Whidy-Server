package com.spam.whidy.domain.place.placeRequest;

import com.spam.whidy.domain.UserBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceRequest extends UserBaseEntity {

    @Id @GeneratedValue
    private Long id;
    private String address;
    private String name;
    private double latitude;
    private double longitude;
    @Builder.Default
    private boolean processed = false; // 처리여부
}
