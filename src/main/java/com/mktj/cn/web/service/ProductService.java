package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.ProductDTO;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface ProductService {

    Iterable<ProductDTO> getProductOrdinaryList();

    Iterable<ProductDTO> getProductPackageList();

    ProductDTO getProductById(long id);
}
