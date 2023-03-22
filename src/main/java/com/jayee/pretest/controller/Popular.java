package com.jayee.pretest.controller;

import lombok.*;

/**
 * 블로그 검색 인기순위 담을 객체
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Popular {
    private String keyword;
    private int cnt;
}
