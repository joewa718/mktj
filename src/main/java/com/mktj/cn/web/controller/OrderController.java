package com.mktj.cn.web.controller;

import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.service.ProductService;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.vo.OrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {

    @Autowired
    OrderService orderService;
    @ApiOperation(value = "下订单")
    @RequestMapping(value = "/applyOrder", method = RequestMethod.POST)
    public ResponseEntity applyOrder(@ModelAttribute OrderVo orderVo) {
        String phone = super.getCurrentUser().getUsername();
        orderService.transactionOrder(phone,orderVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "获取订单明细")
    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public ResponseEntity<OrderDTO> getOrder(@RequestParam long orderId) {
        String phone = super.getCurrentUser().getUsername();
        OrderDTO orderDTO = orderService.getOrder(phone,orderId);
        return new ResponseEntity<>(orderDTO,HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单状态查询订单")
    @RequestMapping(value = "/getOrderListByOrderStatus", method = RequestMethod.POST)
    public ResponseEntity<List<OrderDTO>> getOrderListByOrderStatus(@RequestParam OrderStatus orderStatus) {
        String phone = super.getCurrentUser().getUsername();
        List<OrderDTO> orderDTOList = orderService.findByOrderStatusAndUser(orderStatus,phone);
        return new ResponseEntity<>(orderDTOList,HttpStatus.OK);
    }
}