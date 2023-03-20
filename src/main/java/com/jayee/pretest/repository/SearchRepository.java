package com.jayee.pretest.repository;

import com.jayee.pretest.controller.Popular;
import com.jayee.pretest.domain.Keywords;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SearchRepository {
    private final EntityManager em;

    public void save(Keywords keyword) {
        em.persist(keyword);
    }

    public List<Keywords> findKeywordsCount() {
        return em.createQuery("select k.keyword, COUNT(k.keyword) from Keywords k group by k.keyword", Keywords.class)
                .getResultList();
    }

    public List<Keywords> findAll() {
        return em.createQuery("select k from Keywords k", Keywords.class).getResultList();
    }
//    public List<Keywords> findByKeyword(String keyword) {
//        return em.createQuery("select k from Keywords k where k.keyword = :keyword", Keywords.class)
//                .setParameter("keyword", keyword)
//                .getResultList();
//    }
}
