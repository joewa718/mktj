package com.mktj.cn.web.mapper;

import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.po.DeliveryAddress;
import com.mktj.cn.web.vo.DeliveryAddressVo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeliveryAddressMapper {

    DeliveryAddressDTO deliveryAddressToDeliveryAddressDTO(DeliveryAddress deliveryAddress);

    DeliveryAddress deliveryAddressVoToDeliveryAddress(DeliveryAddressVo deliveryAddress);


    List<DeliveryAddressDTO> deliveryAddressToDeliveryAddressDTOList(List<DeliveryAddress> deliveryAddressList);

    List<DeliveryAddressDTO> deliveryAddressToDeliveryAddressDTOList(Iterable<DeliveryAddress> deliveryAddressList);
}