package com.mktj.cn.web.controller;

import com.mktj.cn.web.dto.EntryDTO;
import com.mktj.cn.web.dto.OrderDTO;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.enumerate.OrderStatus;
import com.mktj.cn.web.enumerate.OrderType;
import com.mktj.cn.web.po.Product;
import com.mktj.cn.web.service.OrderService;
import com.mktj.cn.web.service.ProductService;
import com.mktj.cn.web.service.ReportService;
import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.vo.OrderVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController extends BaseController {
    @Autowired
    ReportService reportService;
    @Autowired
    UserService userService;
    @ApiOperation(value = "获取团队成员分布")
    @RequestMapping(value = "/analysisMemberDistribution", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Long>> analysisMemberDistribution(){
        String phone = super.getCurrentUser().getUsername();
        Map<String,Long> map = reportService.analysisMemberDistribution(phone);
        return new ResponseEntity<>(map,HttpStatus.OK);
    }
    @ApiOperation(value = "获取团队直系成员分布")
    @RequestMapping(value = "/analysisImmediateMemberDistribution", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,Long>> analysisImmediateMemberDistribution(){
        String phone = super.getCurrentUser().getUsername();
        Map<String,Long> map = reportService.analysisImmediateMemberDistribution(phone);
        return new ResponseEntity<>(map,HttpStatus.OK);

    }

    @ApiOperation(value = "获取团队新成员分布")
    @RequestMapping(value = "/analysisNewMemberDistribution", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,Long>> analysisNewMemberDistribution(){
        String phone = super.getCurrentUser().getUsername();
        Map<String,Long> map = reportService.analysisNewMemberDistribution(phone);
        return new ResponseEntity<>(map,HttpStatus.OK);

    }

    @ApiOperation(value = "获取个人进货和销售量")
    @RequestMapping(value = "/analysisOrderVolume", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,List<EntryDTO<String,Long>>>> analysisOrderVolume(){
        String phone = super.getCurrentUser().getUsername();
        Map<String,List<EntryDTO<String,Long>>> map = reportService.analysisOrderVolume(phone);
        return new ResponseEntity<>(map,HttpStatus.OK);

    }

    @ApiOperation(value = "获取个人进货和销售量")
    @RequestMapping(value = "/analysisOrderShare", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,List<EntryDTO<String,Double>>>> analysisOrderShare(){
        String phone = super.getCurrentUser().getUsername();
        Map<String,List<EntryDTO<String,Double>>> map = reportService.analysisOrderShare(phone);
        return new ResponseEntity<>(map,HttpStatus.OK);

    }

    @ApiOperation(value = "获取我的团队人员")
    @RequestMapping(value = "/findMyTeamUser", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,List<UserDTO>>> findMyTeamUser(@RequestParam(value = "search",required = false) String search){
        String phone = super.getCurrentUser().getUsername();
        Map<String, List<UserDTO>> userDTOList = userService.findMyTeamUser(phone,search);
        return new ResponseEntity<>(userDTOList,HttpStatus.OK);

    }

    @ApiOperation(value = "获取我的新团队人员")
    @RequestMapping(value = "/findMyNewTeamUser", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,List<UserDTO>>> findMyNewTeamUser(@RequestParam(value = "search",required = false) String search){
        String phone = super.getCurrentUser().getUsername();
        Map<String,List<UserDTO>> userDTOList = userService.findMyNewTeamUser(phone,search);
        return new ResponseEntity<>(userDTOList,HttpStatus.OK);

    }

    @ApiOperation(value = "获取我的直系团队人员")
    @RequestMapping(value = "/findMyZxTeamUser", method = RequestMethod.POST)
    public  ResponseEntity<Map<String,List<UserDTO>>> findMyZxTeamUser(@RequestParam(value = "search",required = false) String search){
        String phone = super.getCurrentUser().getUsername();
        Map<String,List<UserDTO>> userDTOList = userService.findMyZxTeamUser(phone,search);
        return new ResponseEntity<>(userDTOList,HttpStatus.OK);

    }
}
