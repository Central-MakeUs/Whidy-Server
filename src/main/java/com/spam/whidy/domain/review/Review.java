package com.spam.whidy.domain.review;

import com.spam.whidy.domain.UserBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends UserBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long placeId;
    @Column(nullable = false)
    private Float score;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<ReviewKeyword> keywords = new HashSet<>();
    private Boolean isReserved;
    @Enumerated(EnumType.STRING)
    private WaitTime waitTime;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<VisitPurpose> visitPurposes = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private CompanionType companionType;

    public void update(Review newReview) {
        id = newReview.id;
        placeId = newReview.placeId;
        score = newReview.score;
        keywords = newReview.keywords;
        isReserved = newReview.isReserved;
        waitTime = newReview.waitTime;
        visitPurposes = newReview.visitPurposes;
        companionType = newReview.companionType;
    }

    public boolean isOwner(Long userId){
        return getCreateUser().equals(userId);
    }
}