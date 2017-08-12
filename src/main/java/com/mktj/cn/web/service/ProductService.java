package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.dto.UserDTO;

import java.util.List;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface ProductService {

    Iterable<ProductDTO> getProductOrdinaryList();

    Iterable<ProductDTO> getProductPackageList();
}
