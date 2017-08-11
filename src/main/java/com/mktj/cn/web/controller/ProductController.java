package com.mktj.cn.web.controller;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController extends BaseController {
    @Autowired
    ProductService productService;
    @ApiOperation(value = "获取所有产品列表")
    @RequestMapping(value = "/getProductList", method = RequestMethod.POST)
    public ResponseEntity<Iterable<ProductDTO>> getProductList() {
        Iterable<ProductDTO> productDTOList = productService.getProductList();
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
}
