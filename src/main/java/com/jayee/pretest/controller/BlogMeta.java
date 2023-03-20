package com.jayee.pretest.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogMeta {
    private Boolean isEnd;
    private int pageableCount;
    private int totalCount;
    private int totalPage;
}
