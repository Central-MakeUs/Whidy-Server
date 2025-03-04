package com.spam.whidy.dto.review;

import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.domain.review.*;
import lombok.Data;

import java.util.Set;

@Data
public class ReviewDTO {

    private Long id;
    private Long placeId;
    private PlaceType placeType;
    private String placeName;
    private String placeThumbnail;
    private Float score;
    private Set<ReviewKeyword> keywords;
    private Boolean isReserved;
    private String comment;
    private WaitTime waitTime;
    private Set<VisitPurpose> visitPurposes;
    private CompanionType companionType;
    private Long userId;
    private String userName;
    private String userProfileImage;
    private String createdDateTime;
    private String lastModifiedDateTime;

    public static ReviewDTO of(Review review){
        ReviewDTO dto = new ReviewDTO();
        dto.id = review.getId();
        dto.placeId = review.getPlace().getId();
        dto.placeType = review.getPlace().getPlaceType();
        dto.placeName = review.getPlace().getName();
        dto.placeThumbnail = review.getPlace().getThumbnail();
        dto.score = review.getScore();
        dto.keywords = review.getKeywords();
        dto.isReserved = review.getIsReserved();
        dto.waitTime = review.getWaitTime();
        dto.visitPurposes = review.getVisitPurposes();
        dto.companionType = review.getCompanionType();
        dto.userId = review.getCreateUser().getId();
        dto.userName = review.getCreateUser().getName();
        dto.userProfileImage = review.getCreateUser().getProfileImageUrl();
        dto.createdDateTime = review.getCreatedDateTime().toString();
        dto.lastModifiedDateTime = review.getLastModifiedDateTime().toString();
        dto.comment = review.getComment();
        return dto;
    }

}
