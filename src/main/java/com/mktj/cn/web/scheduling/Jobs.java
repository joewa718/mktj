package com.mktj.cn.web.scheduling;

import com.mktj.cn.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Jobs {
    @Autowired
    UserService userService;
    @Scheduled(cron = "0 1 * * * ?")
    public void cronJob() {
        userService.updateUserRoleType();
    }
}