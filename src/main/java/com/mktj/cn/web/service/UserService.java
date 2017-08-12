package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.exception.DuplicateAccountException;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.service.imp.UserServiceImp;
import com.mktj.cn.web.util.RoleType;
import com.mktj.cn.web.vo.DeliveryAddressVo;
import com.mktj.cn.web.vo.RealInfoVo;
import com.mktj.cn.web.vo.UserVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface UserService {

    String updateFile(MultipartFile file, String filePath) throws Exception;

    UserDTO regUser(UserVo user) throws DuplicateAccountException;

    void regRealInfo(String username,RealInfoVo vo);

    UserDTO findUserByPhone(String username);

    RealInfoDTO findRealInfoByPhone(String username);

    List<DeliveryAddressDTO> findDeliveryAddressByPhone(String username);

    void saveDeliveryAddress(String username,DeliveryAddressVo deliveryAddressVo);

    void editDeliveryAddress(String username,DeliveryAddressVo deliveryAddressVo);

    void setDeliveryAddressDefault(String username,long deliveryAddressId,boolean isDefault);

    void editPassword(String username,String oldPassword,String password);

    void editPhoto(String username,String photo);

    void editRoleType(String username,RoleType roleType);

    void editReceiveMessage(String username,boolean isReceiveMessage);

    void editNickname(String username,String nickname);
}
