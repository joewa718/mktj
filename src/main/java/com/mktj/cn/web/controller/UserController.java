package com.mktj.cn.web.controller;

import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.exception.DuplicateAccountException;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.vo.DeliveryAddressVo;
import com.mktj.cn.web.vo.LoginVo;
import com.mktj.cn.web.vo.RealInfoVo;
import com.mktj.cn.web.vo.UserVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController {
    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    @Value("${photo.path}")
    private String filePath;
    @Autowired
    UserService userService;

    @ApiOperation(value = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile( @RequestParam(value = "file") MultipartFile file) throws ServletException, IOException {
        String photo = null;
        try {
            photo = userService.updateFile(file, filePath);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @ApiOperation(value = "上传头像专用，注册后才能上传头像")
    @RequestMapping(value = "/uploadPhoto", method = RequestMethod.POST)
    public ResponseEntity<String> uploadPhoto( @RequestParam(value = "file") MultipartFile file) throws ServletException, IOException {
        String phone = super.getCurrentUser().getUsername();
        String photo = null;
        try {
            photo = userService.updateFile(file, filePath);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        userService.editPhoto(phone,photo);
        return new ResponseEntity<>(photo, HttpStatus.OK);
    }

    @ApiOperation(value = "注册用户")
    @RequestMapping(value = "/regUser", method = RequestMethod.POST)
    public ResponseEntity<Object> regUser(@ModelAttribute UserVo user) throws ServletException, IOException {
        UserDTO userDTO = null;
        try {
            userDTO = userService.regUser(user);
        } catch (DuplicateAccountException e) {
            log.error(e.getMessage(),e);
            return new ResponseEntity<>("账户已存在", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "注册实名认证,支持新增和修改")
    @RequestMapping(value = "/regRealInfo", method = RequestMethod.POST)
    public ResponseEntity<Object> regRealInfo(@ModelAttribute RealInfoVo realInfoVo) throws ServletException, IOException {
        String phone = super.getCurrentUser().getUsername();
        try {
            userService.regRealInfo(phone,realInfoVo);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return new ResponseEntity<>("注册实名认证失败:"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }



    @ApiOperation(value = "用户登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public void login(@RequestParam String username, @RequestParam String password, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login");
        requestDispatcher.forward(request, response);
    }

    @ApiOperation(value = "获取登录用户信息")
    @RequestMapping(value = "/getLoginUser", method = RequestMethod.POST)
    public ResponseEntity<UserDTO> getLoginUser() {
        String phone = super.getCurrentUser().getUsername();
        UserDTO user = userService.findUserByPhone(phone);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation(value = "获取登录用户的认证信息")
    @RequestMapping(value = "/getLoginUserRealInfo", method = RequestMethod.POST)
    public ResponseEntity<RealInfoDTO> getLoginUserRealInfo() {
        String phone = super.getCurrentUser().getUsername();
        RealInfoDTO realInfoDTO = userService.findRealInfoByPhone(phone);
        return new ResponseEntity<>(realInfoDTO, HttpStatus.OK);
    }

    @ApiOperation(value = "获得登录用户所有的收货地址列表")
    @RequestMapping(value = "/getLoginDeliveryAddressList", method = RequestMethod.POST)
    public ResponseEntity<List<DeliveryAddressDTO>> getLoginDeliveryAddressList() {
        String phone = super.getCurrentUser().getUsername();
        List<DeliveryAddressDTO> deliveryAddressList = userService.findDeliveryAddressByPhone(phone);
        return new ResponseEntity<>(deliveryAddressList, HttpStatus.OK);
    }

    @ApiOperation(value = "保存收货地址")
    @RequestMapping(value = "/saveDeliverAddress", method = RequestMethod.POST)
    public ResponseEntity saveDeliverAddress(@ModelAttribute DeliveryAddressVo deliverAddressVo) {
        String phone = super.getCurrentUser().getUsername();
        userService.saveDeliveryAddress(phone, deliverAddressVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "修改收货地址")
    @RequestMapping(value = "/updateDeliverAddress", method = RequestMethod.POST)
    public ResponseEntity updateDeliverAddress(@ModelAttribute DeliveryAddressVo deliverAddressVo) {
        String username = super.getCurrentUser().getUsername();
        userService.editDeliveryAddress(username, deliverAddressVo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "改动:增加原密码校验，如果原密码不正确提供500,并且给出 原密码不正确的提示!")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public ResponseEntity<String> updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("password") String password) {
        String phone = super.getCurrentUser().getUsername();
        try {
            userService.editPassword(phone, oldPassword, password);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @ApiOperation(value = "设置默认收货地址")
    @RequestMapping(value = "/setDeliveryIsDefault", method = RequestMethod.POST)
    public ResponseEntity setDeliveryIsDefault(@RequestParam("deliveryAddressId") long deliveryAddressId, @RequestParam("isDefault") boolean isDefault) {
        String phone = super.getCurrentUser().getUsername();
        userService.setDeliveryAddressDefault(phone, deliveryAddressId, isDefault);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
