package com.spam.whidy.domain.review;

import com.spam.whidy.domain.TimeBaseEntity;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.user.User;
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
public class Review extends TimeBaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;
    @Column(nullable = false)
    private Float score;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<ReviewKeyword> keywords = new HashSet<>();
    private Boolean isReserved;
    private String comment;
    @Enumerated(EnumType.STRING)
    private WaitTime waitTime;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<VisitPurpose> visitPurposes = new HashSet<>();
    @Enumerated(EnumType.STRING)
    private CompanionType companionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id", nullable = false)
    private User createUser;

    public void update(Review newReview) {
        score = newReview.score;
        keywords = newReview.keywords;
        isReserved = newReview.isReserved;
        waitTime = newReview.waitTime;
        visitPurposes = newReview.visitPurposes;
        companionType = newReview.companionType;
    }

    public boolean isOwner(Long userId){
        return createUser.getId().equals(userId);
    }
}