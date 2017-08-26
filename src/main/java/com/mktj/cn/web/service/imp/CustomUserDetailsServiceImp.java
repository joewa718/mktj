package com.mktj.cn.web.service.imp;

import com.mktj.cn.web.dto.UserSecurityDTO;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("customUserDetailsService")
public class CustomUserDetailsServiceImp implements UserDetailsService {
    //普通用户权限
    public final static String ROLE_USER = "ROLE_USER";
    //管理员权限
    public final static String ROLE_ADMIN = "ROLE_ADMIN";
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserSecurityDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(username);
        if (user == null) {
            user = userRepository.findByAppId(username);
            if (user != null) {
                if(user.getIs_wxLogin() != null && user.getIs_wxLogin() == true){
                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(ROLE_USER));
                    return new UserSecurityDTO(user.getAppId(), user.getWxPassword(), authorities, user);
                }else{
                    throw new UsernameNotFoundException("username not found");
                }
            }else{
                throw new UsernameNotFoundException("username not found");
            }
        }else{
            if(user.getIs_wxLogin() != null && user.getIs_wxLogin() == true){
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(ROLE_USER));
                return new UserSecurityDTO(user.getAppId(), user.getWxPassword(), authorities, user);
            }else{
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(ROLE_USER));
                return new UserSecurityDTO(user.getPhone(), user.getPassword(), authorities, user);
            }
        }
    }

}
