package com.mktj.cn.web.service.imp;

import com.mktj.cn.util.encrypt.AESCryptUtil;
import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.exception.DuplicateAccountException;
import com.mktj.cn.web.mapper.DeliveryAddressMapper;
import com.mktj.cn.web.mapper.RealInfoMapper;
import com.mktj.cn.web.mapper.UserMapper;
import com.mktj.cn.web.po.DeliveryAddress;
import com.mktj.cn.web.po.RealInfo;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.DeliveryAddressRepository;
import com.mktj.cn.web.repositories.UserRepository;
import com.mktj.cn.web.service.BaseService;
import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.util.RoleType;
import com.mktj.cn.web.vo.DeliveryAddressVo;
import com.mktj.cn.web.vo.RealInfoVo;
import com.mktj.cn.web.vo.UserVo;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
@Service
@Scope("prototype")
public class UserServiceImp extends BaseService implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RealInfoMapper realInfoMapper;
    @Autowired
    DeliveryAddressMapper deliveryAddressMapper;

    public String updateFile(MultipartFile file, String filePath) throws Exception {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID() + suffixName;
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            file.transferTo(dest);
            return fileName;
        }
        throw new Exception("上传文件不存在");
    }

    @Override
    public UserDTO regUser(UserVo userVo) throws DuplicateAccountException {
        User user =userRepository.findByPhone(userVo.getUsername());
        if(user != null){
            throw new DuplicateAccountException("duplicate account phone.");
        }
        user = userMapper.userToUserVo(userVo);
        user.setPassword(AESCryptUtil.encrypt(user.getPassword()));
        user.setDisable(false);
        user.setAuthorizationCode(generateRandomCode(user.getPhone()));
        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void regRealInfo(String username, RealInfoVo realInfoVo) {
        User user =userRepository.findByPhone(username);
        RealInfo realInfo = realInfoMapper.realInfoToVoRealInfo(realInfoVo);
        if(user.getRealInfo() == null){
            user.setRealInfo(realInfo);
            realInfo.setUser(user);
        }else{
            RealInfo old_realInfo =user.getRealInfo();
            old_realInfo.setIdCard(realInfo.getIdCard());
            old_realInfo.setIdCardPhotoBack(realInfo.getIdCardPhotoBack());
            old_realInfo.setIdCardPhotoFront(realInfo.getIdCardPhotoFront());
            old_realInfo.setBirthday(realInfo.getBirthday());
            old_realInfo.setSex(realInfo.getSex());
            old_realInfo.setRealName(realInfo.getRealName());
            old_realInfo.setCity(realInfo.getCity());
            old_realInfo.setProvince(realInfo.getProvince());
            old_realInfo.setRegion(realInfo.getRegion());
            old_realInfo.setOccupation(realInfo.getOccupation());
        }
        userRepository.save(user);
    }

    @Override
    public void editPassword(String username,String oldPassword,String password) {
        User user =userRepository.findByPhone(username);
        if(!oldPassword.equals(user.getPassword())){
            throw new RuntimeException("原密码不正确");
        }
        user.setPassword(AESCryptUtil.encrypt(password));
        userRepository.save(user);
    }

    @Override
    public void editPhoto(String username, String photo) {
        User user =userRepository.findByPhone(username);
        user.setHeadPortrait(photo);
        userRepository.save(user);
    }

    @Override
    public UserDTO findUserByPhone(String username) {
        User user = userRepository.findByPhone(username);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public RealInfoDTO findRealInfoByPhone(String username) {
        User user = userRepository.findByPhone(username);
        return realInfoMapper.realInfoToRealInfoDTO(user.getRealInfo());
    }

    @Override
    public List<DeliveryAddressDTO> findDeliveryAddressByPhone(String username) {
        User user = userRepository.findByPhone(username);
        return deliveryAddressMapper.deliveryAddressToDeliveryAddressDTOList(user.getDeliveryAddressList());
    }

    @Override
    public void saveDeliveryAddress(String username,DeliveryAddressVo deliveryAddressVo) {
        User user = userRepository.findByPhone(username);
        Optional<List> deliveryAddressList =Optional.ofNullable(user.getDeliveryAddressList());
        deliveryAddressList.orElse(new ArrayList<>());
        DeliveryAddress deliveryAddress =deliveryAddressMapper.deliveryAddressVoToDeliveryAddress(deliveryAddressVo);
        deliveryAddress.setUser(user);
        deliveryAddressList.get().add(deliveryAddress);
        userRepository.save(user);
    }

    @Override
    public void editDeliveryAddress(String username,DeliveryAddressVo deliveryAddressVo) {
        User user = userRepository.findByPhone(username);
        for(DeliveryAddress deliveryAddress : user.getDeliveryAddressList()){
            if(deliveryAddressVo.getIsDefault()){
                deliveryAddress.setIsDefault(false);
            }
            if(deliveryAddressVo.getId() == deliveryAddress.getId()){
                deliveryAddress.setPhone(deliveryAddressVo.getPhone());
                deliveryAddress.setRegion(deliveryAddressVo.getRegion());
                deliveryAddress.setDeliveryMan(deliveryAddressVo.getDeliveryMan());
                deliveryAddress.setCity(deliveryAddressVo.getCity());
                deliveryAddress.setProvince(deliveryAddressVo.getProvince());
                deliveryAddress.setDetailed(deliveryAddressVo.getDetailed());
                deliveryAddress.setIsDefault(deliveryAddressVo.getIsDefault());

            }
        }
        userRepository.save(user);
    }

    @Override
    public void setDeliveryAddressDefault(String username,long deliveryAddressId, boolean isDefault) {
        User user = userRepository.findByPhone(username);
        for(DeliveryAddress deliveryAddress : user.getDeliveryAddressList()){
            if(isDefault){
                deliveryAddress.setIsDefault(false);
            }
            if(deliveryAddressId == deliveryAddress.getId()){
                deliveryAddress.setIsDefault(isDefault);
            }
        }
        userRepository.save(user);
    }

    @Override
    public void editRoleType(String username, RoleType roleType ) {
        User user = userRepository.findByPhone(username);
        user.setRoleType(roleType);
        userRepository.save(user);
    }

}
