package com.mktj.cn.web.repositories;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.po.Product;
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
public interface ProductRepository extends CrudRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Query("select Product from Product p where p.isOffShelf = false and p.productType = ProductType.普通产品")
    List<Product> getProductOrdinaryList();
    @Query("select Product from Product p where p.isOffShelf = false and p.productType = ProductType.套餐产品")
    List<Product> getProductPackageList();

}
