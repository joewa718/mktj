package com.mktj.cn.web.service;

import com.mktj.cn.web.dto.DeliveryAddressDTO;
import com.mktj.cn.web.dto.RealInfoDTO;
import com.mktj.cn.web.dto.UserDTO;
import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.exception.DuplicateAccountException;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.vo.DeliveryAddressVo;
import com.mktj.cn.web.vo.RealInfoVo;
import com.mktj.cn.web.vo.UserVo;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.OperationNotSupportedException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by zhanwa01 on 2017/4/12.
 */
public interface UserService {

    String updateFile(MultipartFile file, String filePath) throws Exception;

    User regWxUser(WxMpUser user);

    UserDTO regUser(UserVo userVo, HttpSession session) throws DuplicateAccountException, OperationNotSupportedException;

    void regRealInfo(String phone, RealInfoVo vo);

    UserDTO findUserByPhone(String phone);

    RealInfoDTO findRealInfoByPhone(String phone);

    List<DeliveryAddressDTO> findDeliveryAddressByPhone(String phone);

    void saveDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo);

    void editDeliveryAddress(String phone, DeliveryAddressVo deliveryAddressVo);

    void setDeliveryAddressDefault(String phone, long deliveryAddressId, boolean isDefault);

    void editPassword(String phone, String oldPassword, String password);

    void editPhoto(String phone, String photo);

    void editRoleType(String phone, RoleType roleType);

    void editReceiveMessage(String phone, boolean isReceiveMessage);

    void editNickname(String phone, String nickname);

    void editPhone(String phone,String new_phone);

    DeliveryAddressDTO getDefaultAddressByUser(String phone);

    String sendRegCode(String phone, String captcha, HttpSession session) throws Exception;

    String sendPwFoundCode(String phone, String captcha, HttpSession session) throws Exception;

    void foundPasswordNext(String phone,String pwFoundCode,HttpSession session) throws OperationNotSupportedException;

    void foundPassword(String password,HttpSession session) throws OperationNotSupportedException;

    UserDTO getByAuthorizationCode(String authorizationCode);

    Map<String,List<UserDTO>> findMyTeamUser(String person,String search);

    Map<String, List<UserDTO>> findMyNewTeamUser(String phone, String search);

    Map<String,List<UserDTO>> findMyZxTeamUser(String person, String search);

    void upgradeUerRoleType();

    void setWxLogin(String appId,boolean isWxLogin);
}
