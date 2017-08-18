package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.mapper.ProductMapper;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.ProductRepository;
import com.mktj.cn.web.repositories.UserRepository;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.service.ProductService;
import com.mktj.cn.web.util.ProductType;
import com.mktj.cn.web.util.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author zhanwang
 * @create 2017-08-09 13:27
 **/
@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;
    @Autowired
    OrderService ordinaryOrderServiceImp;
    @Autowired
    UserRepository userRepository;

    @Override
    public Iterable<ProductDTO> getProductOrdinaryList(String phone) {
        User user = userRepository.findByPhone(phone);
        List<Product> productList =productRepository.getProductListByProductType(ProductType.普通产品);
        productList.forEach(product -> {
            product.setRetailPrice(ordinaryOrderServiceImp.getProductPrice(user.getRoleType(),product));
        });
        return productMapper.productToProductDTOList(productList);
    }

    @Override
    public Iterable<ProductDTO> getProductPackageList() {
        return productMapper.productToProductDTOList(productRepository.getProductListByProductType(ProductType.套餐产品));
    }

    @Override
    public ProductDTO getProductDtoById(String phone,long id) {
        User user = userRepository.findByPhone(phone);
        Product product = productRepository.findOne(id);
        product.setRetailPrice(ordinaryOrderServiceImp.getProductPrice(user.getRoleType(),product));
        return productMapper.productToProductDTO(product);
    }

    @Override
    public Product getProductById(long id) {
        return productRepository.findOne(id);
    }
}
