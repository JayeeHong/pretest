package com.jayee.pretest.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 블로그 검색 시 검색한 키워드 저장 테이블
 */
@Entity
@Table(name = "keywords")
@Getter
@Setter
@SequenceGenerator(name = "seq_keyword", sequenceName = "seq_keyword", allocationSize = 1)
public class Keywords {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_keyword")
    @Column(name = "keyword_id")
    private Long id;
    private String keyword;
    private LocalDateTime datetime;
}
