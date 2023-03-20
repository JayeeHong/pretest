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

    @GetMapping("/search/list")
    public String viewSearchList(@ModelAttribute KakaoSearch search, Model model) {
        model.addAttribute("searchParam", new KakaoSearch());
        model.addAttribute("blogFormList", new ArrayList<BlogForm>());
        model.addAttribute("blogMeta", new BlogMeta());

        return "search/list";
    }

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

    @GetMapping("/search/popular")
    public String viewSearchList(Model model) {

        List<Popular> popularList = searchService.findKeywordsCount();
        model.addAttribute("popularList", popularList);

        return "search/popular";
    }

    private JSONObject getBlogJSONObj(KakaoSearch search) throws ParseException {
        ResponseEntity<String> responseEntity = searchService.searchBlogList(search);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(responseEntity.getBody().toString());

        return jsonObject;
    }

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

    private BlogMeta getBlogMeta(JSONObject jsonObject) {
        JSONObject metaObject = (JSONObject) jsonObject.get("meta");
        BlogMeta blogMeta = new BlogMeta();
        blogMeta.setIsEnd(Boolean.parseBoolean(metaObject.get("is_end").toString()));
        blogMeta.setPageableCount(Integer.parseInt(metaObject.get("pageable_count").toString()));
        blogMeta.setTotalCount(Integer.parseInt(metaObject.get("total_count").toString()));
        blogMeta.setTotalPage(Integer.parseInt(metaObject.get("pageable_count").toString()) / 10 + 1);

        return blogMeta;
    }

    private void setSearchParamAndSave(KakaoSearch search) {
        Keywords keyword = new Keywords();
        keyword.setKeyword(search.getQuery());
        keyword.setDatetime(LocalDateTime.now());

        searchService.saveKeyword(keyword);
    }
}
