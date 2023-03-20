package com.jayee.pretest.common;

import com.jayee.pretest.repository.KakaoSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoRestApiHelper {

    @Value("${api-key.kakao}")
    private String kakaoApiKey;
    private static final String KAKAO_SEARCH_URL = "https://dapi.kakao.com/v2/search/blog";

    public ResponseEntity<String> getSearchBlog(KakaoSearch searchParam) throws ParseException {
        String queryString = "?query=" + URLEncoder.encode(searchParam.getQuery());

        if (!Objects.isNull(searchParam.getPage())) {
            queryString += "&page=" + searchParam.getPage();
        }

        if (!Objects.isNull(searchParam.getSize())) {
            queryString += "&size=" + searchParam.getSize();
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "KakaoAK " + kakaoApiKey);
        headers.add("Content-Type", "charset=UTF-8");

        URI url = URI.create(KAKAO_SEARCH_URL + queryString);
        RequestEntity<String> request = new RequestEntity<>(headers, HttpMethod.GET, url);
        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        return response;
    }
}
