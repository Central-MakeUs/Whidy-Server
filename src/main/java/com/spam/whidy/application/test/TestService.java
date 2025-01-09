package com.spam.whidy.application.test;

import com.spam.whidy.domain.test.Test;
import com.spam.whidy.domain.test.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;

    public Long save(String string){
        Test test = Test.of(string);
        testRepository.save(test);
        return test.getId();
    }

    public List<Test> findList(){
        return testRepository.findAll();
    }
}
