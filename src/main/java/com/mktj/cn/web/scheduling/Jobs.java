package com.mktj.cn.web.scheduling;

import com.mktj.cn.web.enumerate.RoleType;
import com.mktj.cn.web.po.User;
import com.mktj.cn.web.repositories.TeamOrganizationRepository;
import com.mktj.cn.web.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Jobs {
    @Autowired
    TeamOrganizationRepository teamOrganizationRepository;
    @Autowired
    UserRepository userRepository;
    @Scheduled(cron = "0 15 * * * ?")
    public void cronJob() {
        List<Long> ids = teamOrganizationRepository.getHigherIdList();
        ids.forEach(id -> {
            String offlineUser = teamOrganizationRepository.processCalRoleType(id);
            String[] branch = offlineUser.split("|");
            if(branch.length > 2){
                int u_count = 0;
                int zxu_count = 0;
                for (int i = 2; i < branch.length; i++) {
                    String[] levelGroup = branch[i].split(":");
                    String[] uidList = levelGroup[1].split(",");
                    u_count += uidList.length;
                    if(i==2){
                        zxu_count= u_count;
                    }
                }
                if(u_count > 12 && zxu_count >4){
                    User user =userRepository.findOne(id);
                    user.setRoleType(RoleType.高级合伙人);
                    userRepository.save(user);
                }
            }
        });


    }
}