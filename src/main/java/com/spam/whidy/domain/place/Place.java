package com.spam.whidy.domain.place;

import com.spam.whidy.domain.place.additionalInfo.PlaceAdditionalInfo;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    private Integer beveragePrice;
    private Float reviewScore;
    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private PlaceAdditionalInfo additionalInfo;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "business_hour", joinColumns = @JoinColumn(name = "place_id"))
    private Set<BusinessHour> businessHours = new HashSet<>();
}
