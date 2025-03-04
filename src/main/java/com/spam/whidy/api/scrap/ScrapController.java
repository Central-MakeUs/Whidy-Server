package com.spam.whidy.api.scrap;

import com.spam.whidy.application.scrap.ScrapService;
import com.spam.whidy.common.config.auth.Auth;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.dto.scrap.ScrapDTO;
import com.spam.whidy.dto.scrap.ScrapRequest;
import com.spam.whidy.dto.scrap.ScrapSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "장소 스크랩")
@RequestMapping("/api/scrap")
public class ScrapController {

    private final ScrapService scrapService;

    @PostMapping
    @Operation(summary = "스크랩 저장")
    public Long save(@Auth LoginUser loginUser, @RequestBody @Valid ScrapRequest request){
        return scrapService.save(loginUser.getUserId(), request);
    }

    @DeleteMapping("/{scrapId}")
    @Operation(summary = "스크랩 삭제")
    public void delete(@Auth LoginUser loginUser, @PathVariable Long scrapId){
        scrapService.delete(loginUser.getUserId(), scrapId);
    }

    @GetMapping
    @Operation(summary = "스크랩 조회", description = "Keyword 는 장소 명, 장소 주소 검색에 사용된다.")
    public List<ScrapDTO> searchByCondition(@Auth LoginUser loginUser, ScrapSearchCondition condition){
        return scrapService.searchByConditionAndUser(loginUser.getUserId(), condition);
    }
}
