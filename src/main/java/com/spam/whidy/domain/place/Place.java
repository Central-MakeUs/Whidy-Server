package com.spam.whidy.domain.place;

import com.spam.whidy.domain.place.additionalInfo.PlaceAdditionalInfo;
import com.vladmihalcea.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
            @UniqueConstraint(
                    name = "unique_place_constraint",
                    columnNames = {"name", "address", "placeType"}
            )}
)
public class Place {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, columnDefinition = "geometry")
    private Point coordinates;
    private Integer beveragePrice;
    @Column(length = 2048)
    private String thumbnail;
    @Builder.Default
    @Column(length = 2048)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "place_images", joinColumns = @JoinColumn(name = "place_id"))
    private List<String> images = new ArrayList<>();
    @Builder.Default
    private int reviewNum = 0;
    private Float reviewScore;
    @Enumerated(EnumType.STRING)
    private PlaceType placeType;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "business_hour", joinColumns = @JoinColumn(name = "place_id"))
    private Set<BusinessHour> businessHours = new HashSet<>();

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private PlaceAdditionalInfo additionalInfo;


    public Double getLatitude(){
        return coordinates.getY();
    }

    public Double getLongitude(){
        return coordinates.getX();
    }

    public void addReview(float score) {
        if(reviewScore == null){
            reviewScore = score;
            reviewNum = 1;
        }else{
            float previousTotalScore = reviewNum * reviewScore;
            float newTotalScore = previousTotalScore + score;
            reviewScore = newTotalScore / ++reviewNum;
        }
    }

    public void removeReview(float score){
        float previousTotalScore = reviewNum * reviewScore;
        float newTotalScore = previousTotalScore - score;
        if(--reviewNum == 0){
            reviewScore = null;
        }else{
            reviewScore = newTotalScore / reviewNum;
        }
    }

    public void updateReview(float previousScore, float newScore){
        removeReview(previousScore);
        addReview(newScore);
    }
}
