package com.mktj.cn.web.repositories;

import com.mktj.cn.web.po.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select p from Product p where isOffShelf = false and productType = 1")
    List<Product> getProductOrdinaryList();
    @Query("select p from Product p where isOffShelf = false and productType = 2")
    List<Product> getProductPackageList();

}
