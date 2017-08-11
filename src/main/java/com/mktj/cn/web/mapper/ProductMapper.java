package com.mktj.cn.web.mapper;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.po.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDTO productToProductDTO(Product product);

    List<ProductDTO> productToProductDTOList(List<Product> productList);

    List<ProductDTO> productToProductDTOList(Iterable<Product> productList);
}