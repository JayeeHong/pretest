package com.jayee.pretest.repository;

import com.jayee.pretest.domain.Keywords;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {
    private final EntityManager em;

    /**
     * 검색한 키워드 저장
     * @param keyword 검색어
     */
    public void save(Keywords keyword) {
        em.persist(keyword);
    }

    /**
     * 검색한 키워드 목록 조회
     * @return
     */
    public List<Keywords> findAll() {
        return em.createQuery("select k from Keywords k", Keywords.class).getResultList();
    }
}
