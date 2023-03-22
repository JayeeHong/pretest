package com.jayee.pretest.controller;

import lombok.Getter;
import lombok.Setter;

/**
 * 카카오 API 리턴값 : 페이지, 글 갯수 정보
 */
@Getter
@Setter
public class BlogMeta {
    private Boolean isEnd;
    private int pageableCount;
    private int totalCount;
    private int totalPage;
}
