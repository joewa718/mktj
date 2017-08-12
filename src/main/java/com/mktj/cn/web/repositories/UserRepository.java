package com.mktj.cn.web.repositories;
import com.mktj.cn.web.po.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends CrudRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findOne(Long id);

    User save(User user);

    @Query("select u from User u where u.phone = ?1 and u.disable=0")
    public User findByPhone(String phone);
}
