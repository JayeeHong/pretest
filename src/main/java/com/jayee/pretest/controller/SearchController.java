package com.jayee.pretest.controller;

import com.jayee.pretest.domain.Keywords;
import com.jayee.pretest.repository.KakaoSearch;
import com.jayee.pretest.service.SearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    /**
     * 블로그 목록 조회 화면
     * @param search 검색 조건
     * @return
     */
    @GetMapping("/search/list")
    public String viewSearchList(@ModelAttribute KakaoSearch search, Model model) {
        model.addAttribute("searchParam", new KakaoSearch());
        model.addAttribute("blogFormList", new ArrayList<BlogForm>());
        model.addAttribute("blogMeta", new BlogMeta());

        return "search/list";
    }

    /**
     * 카카오 블로그 검색 API 조회
     * @param query 검색어
     * @param sort 정렬 (정확도, 최신순)
     * @param page 검색할 페이지
     * @return
     * @throws ParseException
     */
    @GetMapping("/search/blogPaging")
    public String searchBlogPaging(@RequestParam String query, @RequestParam(defaultValue = "accuracy") String sort, @RequestParam(defaultValue = "1") Integer page, Model model) throws ParseException {
        KakaoSearch search = new KakaoSearch();
        search.setQuery(query);
        search.setSort(sort);
        search.setPage(page);

        JSONObject jsonObject = getBlogJSONObj(search);
        List<BlogForm> blogFormList = getBlogFormList(jsonObject);
        BlogMeta blogMeta = getBlogMeta(jsonObject);

        //검색어 insert
        setSearchParamAndSave(search);

        model.addAttribute("searchParam", search);
        model.addAttribute("blogFormList", blogFormList);
        model.addAttribute("blogMeta", blogMeta);

        return "search/list";
    }

    /**
     * 블로그 조회 API (페이징 없음)
     * @param search 검색조건
     * @return
     * @throws ParseException
     */
    @GetMapping("/search/blog")
    public String searchBlog(@Valid @ModelAttribute KakaoSearch search, BindingResult bindingResult, Model model) throws ParseException {

        if (bindingResult.hasErrors()) {
            return "search/list";
        }

        JSONObject jsonObject = getBlogJSONObj(search);
        List<BlogForm> blogFormList = getBlogFormList(jsonObject);
        BlogMeta blogMeta = getBlogMeta(jsonObject);

        //검색어 insert
        setSearchParamAndSave(search);

        model.addAttribute("searchParam", search);
        model.addAttribute("blogFormList", blogFormList);
        model.addAttribute("blogMeta", blogMeta);

        return "search/list";
    }

    /**
     * 인기검색어 순위 조회 화면
     * @return
     */
    @GetMapping("/search/popular")
    public String viewSearchList(Model model) {

        List<Popular> popularList = searchService.findKeywordsCount();
        model.addAttribute("popularList", popularList);

        return "search/popular";
    }

    /**
     * 블로그 검색 API 조회 결과로 JSONObject 반환
     * @param search 검색조건
     * @return
     * @throws ParseException
     */
    private JSONObject getBlogJSONObj(KakaoSearch search) throws ParseException {
        ResponseEntity<String> responseEntity = searchService.searchBlogList(search);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(responseEntity.getBody().toString());

        return jsonObject;
    }

    /**
     * 블로그 검색 API 조회 결과로 검색한 블로그 리스트 반환
     * @param jsonObject 변환할 객체
     * @return
     */
    private List<BlogForm> getBlogFormList(JSONObject jsonObject) {
        JSONArray docuArray = (JSONArray) jsonObject.get("documents");

        List<BlogForm> blogFormList = new ArrayList<>();
        for (int i = 0; i < docuArray.size(); i++) {
            JSONObject docuObject = (JSONObject) docuArray.get(i);
            BlogForm blogForm = new BlogForm();
            blogForm.setBlogName(docuObject.get("blogname").toString());
            blogForm.setContents(docuObject.get("contents").toString());
            blogForm.setDatetime(docuObject.get("datetime").toString());
            blogForm.setThumbnail(docuObject.get("thumbnail").toString());
            blogForm.setTitle(docuObject.get("title").toString());
            blogForm.setUrl(docuObject.get("url").toString());

            blogFormList.add(blogForm);
        }

        return blogFormList;
    }

    /**
     * 블로그 검색 API 조회 결과로 검색한 블로그 메타정보 반환
     * @param jsonObject 변환할 객체
     * @return
     */
    private BlogMeta getBlogMeta(JSONObject jsonObject) {
        JSONObject metaObject = (JSONObject) jsonObject.get("meta");
        BlogMeta blogMeta = new BlogMeta();
        blogMeta.setIsEnd(Boolean.parseBoolean(metaObject.get("is_end").toString()));
        blogMeta.setPageableCount(Integer.parseInt(metaObject.get("pageable_count").toString()));
        blogMeta.setTotalCount(Integer.parseInt(metaObject.get("total_count").toString()));
        blogMeta.setTotalPage(Integer.parseInt(metaObject.get("pageable_count").toString()) / 10 + 1);

        return blogMeta;
    }

    /**
     * 검색조건 값 세팅 및 검색한 키워드 저장
     * @param search 검색조건
     */
    private void setSearchParamAndSave(KakaoSearch search) {
        Keywords keyword = new Keywords();
        keyword.setKeyword(search.getQuery());
        keyword.setDatetime(LocalDateTime.now());

        searchService.saveKeyword(keyword);
    }
}
