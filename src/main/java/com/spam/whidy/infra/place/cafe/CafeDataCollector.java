package com.spam.whidy.infra.place.cafe;

import com.google.gson.Gson;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceDataCollector;
import com.spam.whidy.domain.place.PlaceType;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.json.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CafeDataCollector implements PlaceDataCollector {

    private List<String> studyCafes;
    private List<String> franchiseCafes;
    @Value("${application.data.file-path}")
    private String dataFilePath;

    private static final int MIN_REVIEW_NUM_TO_COLLECT = 40;

    @Override
    public List<Place> collect() {
        setFilteringCafeKeywords();
        List<CollectedCafeData> details = getCollectedCafeDetails();
        return details.stream()
                .map(data -> data.toEntity(getType(data.getName())))
//                .filter(this::isProperToCollect)
                .collect(Collectors.toList());
    }

    private boolean isProperToCollect(CollectedCafeData data){
        return data.getReviewNum() >= MIN_REVIEW_NUM_TO_COLLECT && data.isGoodToFocus();
    }

    private PlaceType getType(String cafeName) {
        if(studyCafes.stream().anyMatch(cafeName::contains)){
            return PlaceType.STUDY_CAFE;
        }
        else if(franchiseCafes.stream().anyMatch(cafeName::contains)){
            return PlaceType.FRANCHISE_CAFE;
        }else{
            return PlaceType.GENERAL_CAFE;
        }
    }

    private List<CollectedCafeData> getCollectedCafeDetails() {
//        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(dataFilePath);
        try (InputStream inputStream = new FileInputStream(dataFilePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<CollectedCafeData>>() {}.getType();
            return gson.fromJson(reader, listType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setFilteringCafeKeywords() {
        setStudyCafeKeywords();
        setFranchiseCafeKeywords();
    }

    private void setFranchiseCafeKeywords() {
        franchiseCafes = new ArrayList<>();
        String franchiseCafeKeywordFile = "data/cafe/franchise.txt";
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(franchiseCafeKeywordFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                franchiseCafes.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("프랜차이즈 키워드 파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }

    private void setStudyCafeKeywords() {
        studyCafes = new ArrayList<>();
        String studyCafeKeywordFile = "data/cafe/study-cafe.txt";
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(studyCafeKeywordFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                studyCafes.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("스터디카페 키워드 파일을 읽는 중 오류 발생: " + e.getMessage());
        }
    }

    @Override
    public PlaceType dataType() {
        return PlaceType.GENERAL_CAFE;
    }
}
