package com.spam.whidy.infra.user;

import com.spam.whidy.application.user.ProfileImageStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class S3ProfileImageStorage implements ProfileImageStorage {

    private final Region REGION;
    private final S3Client s3Client;

    @Value("${application.s3.profile.bucket-name}")
    private String BUCKET_NAME;

    public S3ProfileImageStorage(@Value("${application.s3.region}") String region) {
        REGION = Region.of(region);
        this.s3Client = S3Client.builder()
                .region(REGION)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public String getImageUrl(String key, Long ttlMinute){
        try (S3Presigner presigner = getPresigner()) {
            GetObjectPresignRequest presignRequest = getPresignRequest(key, ttlMinute);
            URL presignedUrl = presigner.presignGetObject(presignRequest).url();
            return presignedUrl.toString();
        }
    }

    private GetObjectPresignRequest getPresignRequest(String key, Long ttlMinute) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();
        return GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(ttlMinute))
                .getObjectRequest(getObjectRequest)
                .build();
    }

    private S3Presigner getPresigner() {
        return S3Presigner.builder()
                .region(REGION)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Override
    public String upload(MultipartFile file, Long userId) {
        try {
            deletePreviousProfiles(userId);
            String key = getKeyPrefix(userId) + file.getOriginalFilename();
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .build();
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            return key;
        } catch (Exception e) {
            throw new RuntimeException("S3 프로필 업로드 실패 : ", e);
        }
    }

    private void deletePreviousProfiles(Long userId) {
        List<ObjectIdentifier> profilesToDelete = getPreviousProfiles(userId);
        if(!profilesToDelete.isEmpty()) {
            DeleteObjectsRequest deleteRequest = DeleteObjectsRequest.builder()
                    .bucket(BUCKET_NAME)
                    .delete(Delete.builder().objects(profilesToDelete).build())
                    .build();
            s3Client.deleteObjects(deleteRequest);
        }
    }

    private List<ObjectIdentifier> getPreviousProfiles(Long userId) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(BUCKET_NAME)
                .prefix(getKeyPrefix(userId))
                .build();
        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        return listResponse.contents()
                .stream()
                .map(s3Object -> ObjectIdentifier.builder().key(s3Object.key()).build())
                .collect(Collectors.toList());
    }

    private String getKeyPrefix(Long userId) {
        return "profile/" + userId + "/";
    }
}
