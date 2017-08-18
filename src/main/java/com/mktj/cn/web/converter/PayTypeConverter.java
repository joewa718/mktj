package com.mktj.cn.web.converter;

import com.mktj.cn.web.enumerate.PayType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PayTypeConverter implements AttributeConverter<PayType, Integer> {
    public Integer convertToDatabaseColumn(PayType value) {
        if (value == null) {
            return null;
        }

        return value.getCode();
    }

    public PayType convertToEntityAttribute(Integer value) {
        if (value == null) {
            return null;
        }

        return PayType.fromCode(value);
    }
}