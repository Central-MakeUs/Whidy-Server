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
    private Double latitude;
    @Column(nullable = false)
    private Double longitude;
    private Integer beveragePrice;
    @Builder.Default
    private int reviewNum = 0;
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


    public void addReview(float score) {
        float previousTotalScore = reviewNum * reviewScore;
        float newTotalScore = previousTotalScore + score;
        reviewScore = newTotalScore / ++reviewNum;
    }

    public void removeReview(float score){
        float previousTotalScore = reviewNum * reviewScore;
        float newTotalScore = previousTotalScore - score;
        reviewScore = newTotalScore / --reviewNum;
    }

    public void updateReview(float previousScore, float newScore){
        removeReview(previousScore);
        addReview(newScore);
    }
}
