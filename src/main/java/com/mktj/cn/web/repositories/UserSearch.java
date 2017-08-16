package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.User;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UserSearch {
  @PersistenceContext
  private EntityManager entityManager;
  public List search(String text) {
    FullTextEntityManager fullTextEntityManager =
        org.hibernate.search.jpa.Search.
        getFullTextEntityManager(entityManager);
    QueryBuilder queryBuilder =
        fullTextEntityManager.getSearchFactory()
        .buildQueryBuilder().forEntity(User.class).get();
    org.apache.lucene.search.Query query =
        queryBuilder
          .keyword()
          .onFields("nickname", "phone","email")
          .matching(text)
          .createQuery();
    org.hibernate.search.jpa.FullTextQuery jpaQuery =
        fullTextEntityManager.createFullTextQuery(query, User.class);
    @SuppressWarnings("unchecked")
    List results = jpaQuery.getResultList();
    return results;
  }

}