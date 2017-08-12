package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.DeliveryAddress;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DeliveryAddressRepository extends CrudRepository<DeliveryAddress, Long>, JpaSpecificationExecutor<DeliveryAddress> {

    DeliveryAddress findOneByIdAndUser(long id, User user);

    void deleteAllByIdAndUser(long id, User user);
}
