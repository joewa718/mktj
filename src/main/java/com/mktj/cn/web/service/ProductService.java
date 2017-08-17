package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.po.Product;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface ProductService {

    Iterable<ProductDTO> getProductOrdinaryList(String phone);

    Iterable<ProductDTO> getProductPackageList();

    ProductDTO getProductDtoById(long id);

    Product getProductById(long id);
}
