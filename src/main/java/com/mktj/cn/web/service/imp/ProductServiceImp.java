package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.mapper.ProductMapper;
import com.mktj.cn.web.repositories.ProductRepository;
import com.mktj.cn.web.service.ProductService;
import com.mktj.cn.web.util.ProductType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Override
    public Iterable<ProductDTO> getProductOrdinaryList() {
        return productMapper.productToProductDTOList(productRepository.getProductListByProductType(ProductType.普通产品));
    }

    @Override
    public Iterable<ProductDTO> getProductPackageList() {
        return productMapper.productToProductDTOList(productRepository.getProductListByProductType(ProductType.套餐产品));
    }
}
