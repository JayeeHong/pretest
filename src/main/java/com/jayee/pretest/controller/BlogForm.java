package com.jayee.pretest.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BlogForm {
    private String blogName;
    private String contents;
    private String datetime;
    private String thumbnail;
    private String title;
    private String url;
}
