package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.util.DateUtil;
import com.mktj.cn.web.util.encrypt.AESCryptUtil;
import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.exception.DuplicateAccountException;
import com.mktj.cn.web.mapper.DeliveryAddressMapper;
import com.mktj.cn.web.mapper.RealInfoMapper;
import com.mktj.cn.web.mapper.UserMapper;
import com.mktj.cn.web.po.*;
import com.mktj.cn.web.repositories.DeliveryAddressRepository;
import com.mktj.cn.web.repositories.UserRepository;
import com.mktj.cn.web.service.BaseService;
import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.util.GenerateRandomCode;
import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.util.SmsSender;
import com.mktj.cn.web.vo.DeliveryAddressVo;
import com.mktj.cn.web.vo.RealInfoVo;
import com.mktj.cn.web.vo.UserVo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
@Service
@Scope("prototype")
public class UserServiceImp extends BaseService implements UserService {
    private final static Logger log = LoggerFactory.getLogger(UserServiceImp.class);
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
    @Autowired
    SmsSender smsSender;

    public String updateFile(MultipartFile file, String filePath) throws Exception {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            fileName = UUID.randomUUID() + suffixName;
            fileName = fileName.toLowerCase();
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
    public UserDTO regWxUser(WxMpUser wxMpUser) {
        User user = userRepository.findByPhone(wxMpUser.getOpenId());
        if (user == null) {
            user = new User();
        }
        user.setNickname(wxMpUser.getNickname());
        user.setPassword(AESCryptUtil.encrypt("123456"));
        user.setHeadPortrait(wxMpUser.getHeadImgUrl());
        user.setDisable(false);
        user.setAuthorizationCode(generateAuthCode());
        user = userRepository.save(user);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public UserDTO regUser(UserVo userVo, HttpSession session) throws DuplicateAccountException, OperationNotSupportedException {
        User user = userRepository.findByPhone(userVo.getPhone());
        if (user != null) {
            throw new DuplicateAccountException("duplicate account phone.");
        }
        String regCode = (String) session.getAttribute("regCode");
        if (userVo.getRegCode() == null || !userVo.getRegCode().equals(regCode)) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long regCodeTime = (Long) session.getAttribute("regCodeTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - regCodeTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        user = userMapper.userToUserVo(userVo);
        user.setPassword(AESCryptUtil.encrypt(user.getPassword()));
        user.setDisable(false);
        user.setRegTime(DateUtil.getCurrentDate());
        user = userRepository.save(user);
        session.removeAttribute("regCode");
        session.removeAttribute("regCodeTime");
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void regRealInfo(String phone, RealInfoVo realInfoVo) {
        User user = userRepository.findByPhone(phone);
        RealInfo realInfo = realInfoMapper.realInfoToVoRealInfo(realInfoVo);
        if (user.getRealInfo() == null) {
            user.setRealInfo(realInfo);
            realInfo.setUser(user);
        } else {
            RealInfo old_realInfo = user.getRealInfo();
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
    public void editPassword(String phone, String oldPassword, String password) {
        User user = userRepository.findByPhone(phone);
        if (!AESCryptUtil.encrypt(oldPassword).equals(user.getPassword())) {
            throw new RuntimeException("原密码不正确");
        }
        user.setPassword(AESCryptUtil.encrypt(password));
        userRepository.save(user);
    }

    @Override
    public void editPhoto(String phone, String photo) {
        User user = userRepository.findByPhone(phone);
        user.setHeadPortrait(photo);
        userRepository.save(user);
    }

    @Override
    public void editReceiveMessage(String phone, boolean isReceiveMessage) {
        User user = userRepository.findByPhone(phone);
        user.setReceiveMessage(isReceiveMessage);
        userRepository.save(user);
    }


    @Override
    public UserDTO findUserByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return userMapper.userToUserDTO(user);
    }

    @Override
    public RealInfoDTO findRealInfoByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return realInfoMapper.realInfoToRealInfoDTO(user.getRealInfo());
    }

    @Override
    public List<DeliveryAddressDTO> findDeliveryAddressByPhone(String phone) {
        User user = userRepository.findByPhone(phone);
        return deliveryAddressMapper.deliveryAddressToDeliveryAddressDTOList(user.getDeliveryAddressList());
    }

    @Override
    public void saveDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo) {
        User user = userRepository.findByPhone(phone);
        Optional<Set<DeliveryAddress>> deliveryAddressList = Optional.ofNullable(user.getDeliveryAddressList());
        deliveryAddressList.orElse(new TreeSet<>());
        if (deliveryAddressVo.getIsDefault()) {
            deliveryAddressList.get().forEach(deliveryAddress -> {
                deliveryAddress.setIsDefault(false);
            });
        }
        DeliveryAddress deliveryAddress = deliveryAddressMapper.deliveryAddressVoToDeliveryAddress(deliveryAddressVo);
        deliveryAddress.setUser(user);
        deliveryAddressList.get().add(deliveryAddress);
        userRepository.save(user);
    }

    @Override
    public void editDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo) {
        User user = userRepository.findByPhone(phone);
        for (DeliveryAddress deliveryAddress : user.getDeliveryAddressList()) {
            if (deliveryAddressVo.getIsDefault()) {
                deliveryAddress.setIsDefault(false);
            }
            if (deliveryAddressVo.getId() == deliveryAddress.getId()) {
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
    public void setDeliveryAddressDefault(String phone, long deliveryAddressId, boolean isDefault) {
        User user = userRepository.findByPhone(phone);
        for (DeliveryAddress deliveryAddress : user.getDeliveryAddressList()) {
            if (isDefault) {
                deliveryAddress.setIsDefault(false);
            }
            if (deliveryAddressId == deliveryAddress.getId()) {
                deliveryAddress.setIsDefault(isDefault);
            }
        }
        userRepository.save(user);
    }

    @Override
    public void editRoleType(String phone, RoleType roleType) {
        User user = userRepository.findByPhone(phone);
        user.setRoleType(roleType);
        userRepository.save(user);
    }

    @Override
    public void editNickname(String phone, String nickname) {
        User user = userRepository.findByPhone(phone);
        user.setNickname(nickname);
        userRepository.save(user);
    }

    @Override
    public DeliveryAddressDTO getDefaultAddressByUser(String phone) {
        User user = userRepository.findByPhone(phone);
        DeliveryAddress deliveryAddress = deliveryAddressRepository.findOneByIsDefaultAndUser(true, user);
        return deliveryAddressMapper.deliveryAddressToDeliveryAddressDTO(deliveryAddress);
    }

    @Override
    public String sendRegCode(String phone, String captcha, HttpSession session) throws Exception {
        try {
            String session_captcha = (String) session.getAttribute("captcha");
            if (captcha == null || !captcha.equals(session_captcha)) {
                throw new OperationNotSupportedException("图片验证码不正确");
            }
            String code = GenerateRandomCode.getRandNum(6);
            smsSender.sendRegCode(phone, code);
            return code;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String sendPwFoundCode(String phone, String captcha, HttpSession session) throws Exception {
        try {
            String session_captcha = (String) session.getAttribute("captcha");
            if (captcha == null || !captcha.equals(session_captcha)) {
                throw new OperationNotSupportedException("图片验证码不正确");
            }
            String code = GenerateRandomCode.getRandNum(6);
            smsSender.sendPwFoundCode(phone, code);
            return code;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void foundPasswordNext(String phone, String pwFoundCode, HttpSession session) throws OperationNotSupportedException {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new OperationNotSupportedException("未找到用户手机号");
        }
        String session_pwFoundCode = (String) session.getAttribute("sendPwFoundCode");
        if (pwFoundCode == null || !pwFoundCode.equals(session_pwFoundCode)) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long sendPwFoundTime = (Long) session.getAttribute("sendPwFoundTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - sendPwFoundTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        session.setAttribute("foundPhone", phone);
    }

    @Override
    public void foundPassword(String password, HttpSession session) throws OperationNotSupportedException {
        String phone = (String) session.getAttribute("foundPhone");
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new OperationNotSupportedException("未找到用户手机号");
        }
        String session_pwFoundCode = (String) session.getAttribute("sendPwFoundCode");
        if (session_pwFoundCode == null) {
            throw new OperationNotSupportedException("手机验证码不正确");
        }
        Long sendPwFoundTime = (Long) session.getAttribute("sendPwFoundTime");
        long cur_time = DateUtil.getCurrentDate().getTime();
        if ((cur_time - sendPwFoundTime) / 1000 > 600) {//10分钟
            throw new OperationNotSupportedException("手机验证码已经过期");
        }
        user.setPassword(AESCryptUtil.encrypt(password));
        userRepository.save(user);
        session.removeAttribute("sendPwFoundCode");
        session.removeAttribute("sendPwFoundTime");
    }

    @Override
    public UserDTO getByAuthorizationCode(String authorizationCode) {
        User user = userRepository.getByAuthorizationCode(authorizationCode);
        if (user == null) {
            throw new RuntimeException("您的验证码无效,未查询到用户信息");
        }
        return userMapper.userToUserDTO(user);
    }

    @Override
    public void upgradeUerRoleType() {
        List<User> updateList=new ArrayList<>();
        List<User> userList = userRepository.findByLessThanRoleType(RoleType.高级合伙人);
        for (User user : userList) {
            Long zxCount = userRepository.findSumByOneLevelOrgPath(getEqualStr(user));
            if(zxCount >= 12){
                if (user.getAuthorizationCode() == null) {
                    user.setAuthorizationCode(generateAuthCode());
                }
                user.setRoleType(RoleType.高级合伙人);
                updateList.add(user);
            }else{
                Long allCount = userRepository.findSumByLikeOrgPath(getLikeStr(user));
                if(allCount >= 12 && zxCount >= 4){
                    if (user.getAuthorizationCode() == null) {
                        user.setAuthorizationCode(generateAuthCode());
                    }
                    user.setRoleType(RoleType.高级合伙人);
                    updateList.add(user);
                }
            }
        }
        userRepository.save(updateList);
    }
    @Override
    public Map<String, List<UserDTO>> findMyTeamUser(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findByLikeOrgPath(getLikeStr(user));
        return getMyTeamList(search, userList);
    }

    @Override
    public Map<String, List<UserDTO>> findMyNewTeamUser(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findByLikeOrgPath(getLikeStr(user));
        return getMyTeamList(search, userList);
    }

    @Override
    public Map<String, List<UserDTO>> findMyZxTeamUser(String phone, String search) {
        User user = userRepository.findByPhone(phone);
        List<User> userList = userRepository.findByOneLevelOrgPath(getEqualStr(user));
        return getMyTeamList(search, userList);
    }

    private Map<String, List<UserDTO>> getMyTeamList(String search, List<User> userList) {
        Map<String, List<UserDTO>> result = new LinkedHashMap<>();
        result.put(RoleType.高级合伙人.getName(), new ArrayList<>());
        result.put(RoleType.准合伙人.getName(), new ArrayList<>());
        result.put(RoleType.合伙人.getName(), new ArrayList<>());
        result.put(RoleType.天使.getName(), new ArrayList<>());
        if (search != null) {
            userList = userList.stream().filter(u -> search.equals(u.getPhone()) || (u.getRealInfo() != null && search.equals(u.getRealInfo().getRealName()))).collect(Collectors.toList());
        }
        userList.forEach(u -> {
            if (result.containsKey(u.getRoleType().getName())) {
                result.get(u.getRoleType().getName()).add(userMapper.userToUserDTO(u));
            }
        });
        return result;
    }
}
