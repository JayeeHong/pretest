package com.jayee.pretest.service;

import com.jayee.pretest.common.KakaoRestApiHelper;
import com.jayee.pretest.controller.Popular;
import com.jayee.pretest.domain.Keywords;
import com.jayee.pretest.repository.KakaoSearch;
import com.jayee.pretest.repository.SearchRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchService {

    @Resource
    private KakaoRestApiHelper kakaoRestApiHelper;
    private final SearchRepository searchRepository;

    /**
     * 블로그 API 조회
     * @param searchParam 검색조건
     * @return
     * @throws ParseException
     */
    public ResponseEntity<String> searchBlogList(@ModelAttribute KakaoSearch searchParam) {
        ResponseEntity<String> response = kakaoRestApiHelper.getSearchBlog(searchParam);

        return response;
    }

    /**
     * 저장한 검색어 조회 후 각 검색어별 카운트한 목록 반환
     * @return
     */
    public List<Popular> findKeywordsCount() {
        List<Keywords> keywords = searchRepository.findAll();
        List<Popular> popularList = new ArrayList<>();
        for (Keywords k : keywords) {
            Popular popular = new Popular();
            popular.setKeyword(k.getKeyword());
            popular.setCnt(1);

            popularList.add(popular);
        }

        List<Popular> returnPopularList = new ArrayList<>();
        Map<String, Integer> popularMap = popularList.stream().collect(Collectors.groupingBy(Popular::getKeyword, Collectors.summingInt(Popular::getCnt)));
        for (Map.Entry<String, Integer> popular : popularMap.entrySet()) {
            returnPopularList.add(new Popular(popular.getKey(), popular.getValue()));
        }

        returnPopularList = returnPopularList.stream().sorted(Comparator.comparingInt(Popular::getCnt).reversed()).collect(Collectors.toList());

        return returnPopularList;
    }

    /**
     * 검색어 저장
     * @param keyword 검색어
     */
    @Transactional
    public void saveKeyword(Keywords keyword) {
        searchRepository.save(keyword);
    }
}
