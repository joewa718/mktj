package com.mktj.cn.web.controller;

import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductController extends BaseController {
    @Autowired
    ProductService productService;
    @ApiOperation(value = "获取普通产品列表")
    @RequestMapping(value = "/getProductOrdinaryList", method = RequestMethod.POST)
    public ResponseEntity<Iterable<ProductDTO>> getProductOrdinaryList() {
        String phone = super.getCurrentUser().getUsername();
        Iterable<ProductDTO> productDTOList = productService.getProductOrdinaryList(phone);
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "获取套餐产品列表")
    @RequestMapping(value = "/getProductPackageList", method = RequestMethod.POST)
    public ResponseEntity<Iterable<ProductDTO>> getProductPackageList() {
        Iterable<ProductDTO> productDTOList = productService.getProductPackageList();
        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "获取产品详细信息")
    @RequestMapping(value = "/getProductById", method = RequestMethod.POST)
    public ResponseEntity<ProductDTO> getProductById(@RequestParam("id") long id) {
        String phone = super.getCurrentUser().getUsername();
        ProductDTO productDTO = productService.getProductDtoById(phone,id);
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
}
