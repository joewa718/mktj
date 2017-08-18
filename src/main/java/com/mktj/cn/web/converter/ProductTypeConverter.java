package com.mktj.cn.web.converter;

import com.mktj.cn.web.enu.ProductType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ProductTypeConverter implements AttributeConverter<ProductType, Integer> {
    public Integer convertToDatabaseColumn(ProductType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public ProductType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return ProductType.fromCode(value);
    }
}