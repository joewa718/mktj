package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    public User findOne(Long id);

    public User save(User user);

    @Query("select u from User u where u.username = ?1 and u.disable=0")
    public User findByUsername(String username);

    @Query("select u from User u where u.username = ?1")
    public User findOneByUsername(String username);

    @Query("select u from User u where u.disable=0")
    public List<User> fetchAllEnableUser();

    @EntityGraph("UserAll")
    @Query("select u from User u order by u.id asc")
    public List<User> findAll();

    @EntityGraph("UserAll")
    Page<User> findAll(Specification<User> spec, Pageable pageable);
}
