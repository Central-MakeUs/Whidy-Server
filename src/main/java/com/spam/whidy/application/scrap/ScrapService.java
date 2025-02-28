package com.spam.whidy.application.scrap;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.scrap.Scrap;
import com.spam.whidy.domain.scrap.ScrapRepository;
import com.spam.whidy.dto.scrap.ScrapDTO;
import com.spam.whidy.dto.scrap.ScrapRequest;
import com.spam.whidy.dto.scrap.ScrapSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;

    public Long save(ScrapRequest request) {
        Scrap scrap = request.toEntity();
        scrapRepository.save(scrap);
        return scrap.getId();
    }

    public void delete(Long requestUserId, Long scrapId){
        Scrap scrap = scrapRepository.findById(scrapId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.SCRAP_NOT_FOUND));
        if(scrap.isOwner(requestUserId)){
            throw new BadRequestException(ExceptionType.SCRAP_NOT_FOUND); // 자신의 스크랩이 아닌 경우 스크랩의 존재 여부를 알 필요도 없기 때문에 404로 응답
        }
        scrapRepository.delete(scrap);
    }

    public List<ScrapDTO> searchByConditionAndUser(Long userId, ScrapSearchCondition condition) {
        return scrapRepository.searchByConditionAndUser(userId, condition);
    }
}
