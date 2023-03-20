package com.jayee.pretest.repository;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Kakao 검색 API 파라미터
 */
@Getter
@Setter
public class KakaoSearch {
    @NotBlank(message="키워드는 필수입니다.")
    private String query; //검색 질의어
    private String sort; //결과문서 정렬 방식 - accuracy(정확도순), recency(최신순)
    private Integer page; //결과 페이지 번호, 1~50 사이의 값, 기본값 1
    private Integer size; //한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본값 10
}
