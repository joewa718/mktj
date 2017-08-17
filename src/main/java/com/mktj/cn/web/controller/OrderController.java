package com.mktj.cn.web.controller;

import com.mktj.cn.web.dto.EntryDTO;
import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.dto.ProductDTO;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.service.ProductService;
import com.mktj.cn.web.service.imp.OrdinaryOrderServiceImp;
import com.mktj.cn.web.service.imp.ServiceOrderServiceImp;
import com.mktj.cn.web.util.OrderStatus;
import com.mktj.cn.web.util.OrderType;
import com.mktj.cn.web.util.ProductType;
import com.mktj.cn.web.vo.OrderVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.OperationNotSupportedException;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController extends BaseController {
    @Qualifier("ordinaryOrderServiceImp")
    @Autowired
    OrderService ordinaryOrderServiceImp;
    @Qualifier("serviceOrderServiceImp")
    @Autowired
    OrderService serviceOrderServiceImp;
    @Autowired
    ProductService productService;
    @ApiOperation(value = "下订单")
    @RequestMapping(value = "/applyOrder", method = RequestMethod.POST)
    public ResponseEntity<Object> applyOrder(@ModelAttribute OrderVo orderVo) {
        String phone = super.getCurrentUser().getUsername();
        OrderService orderService = null;
        try {
            Product product = productService.getProductById(orderVo.getProductId());
            if (product == null) {
                throw new OperationNotSupportedException("无法找到对应的商品");
            }
            if(product.getProductType() == ProductType.普通产品){
                orderService = ordinaryOrderServiceImp;
            }else{
                orderService = serviceOrderServiceImp;
            }
            OrderDTO order = orderService.transactionOrder(phone, orderVo);
            return new ResponseEntity<>(order,HttpStatus.OK);
        } catch (OperationNotSupportedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "获取订单明细")
    @RequestMapping(value = "/getOrder", method = RequestMethod.POST)
    public ResponseEntity<OrderDTO> getOrder(@RequestParam long orderId) {
        String phone = super.getCurrentUser().getUsername();
        OrderDTO orderDTO = ordinaryOrderServiceImp.getOrder(phone, orderId);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单的类型汇总订单各种状态的量")
    @RequestMapping(value = "/groupByOrderTypeAndOrderStatusAndUser", method = RequestMethod.POST)
    public ResponseEntity<List<EntryDTO<String,Long>>> groupByOrderTypeAndOrderStatusAndUser(@RequestParam OrderType orderType) {
        String phone = super.getCurrentUser().getUsername();
        OrderService orderService = null;
        if(orderType == OrderType.进货订单){
            orderService = ordinaryOrderServiceImp;
        }else{
            orderService = serviceOrderServiceImp;
        }
        List<EntryDTO<String,Long>> orderDTOList =  orderService.groupOrderStatusCountByAndOrder(phone,orderType);
        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单状态查询订单")
    @RequestMapping(value = "/findByOrderTypeAndOrderStatusAndUser", method = RequestMethod.POST)
    public ResponseEntity<List<OrderDTO>> findByOrderTypeAndOrderStatusAndUser(@RequestParam OrderType orderType, @RequestParam OrderStatus orderStatus) {
        String phone = super.getCurrentUser().getUsername();
        OrderService orderService = null;
        if(orderType == OrderType.进货订单){
            orderService = ordinaryOrderServiceImp;
        }else{
            orderService = serviceOrderServiceImp;
        }
        List<OrderDTO> orderDTOList = orderService.findByOrderTypeAndOrderStatusAndUser(orderType, orderStatus, phone);
        return new ResponseEntity<>(orderDTOList, HttpStatus.OK);
    }

    @ApiOperation(value = "根据订单类型获取订单总量")
    @RequestMapping(value = "/countByOrderTypeAndUser", method = RequestMethod.POST)
    public ResponseEntity<Long> countByOrderTypeAndUser(@RequestParam OrderType orderType) {
        String phone = super.getCurrentUser().getUsername();
        OrderService orderService = null;
        if(orderType == OrderType.进货订单){
            orderService = ordinaryOrderServiceImp;
        }else{
            orderService = serviceOrderServiceImp;
        }
        Long count = orderService.countByOrderTypeAndUser(phone, orderType);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
