package com.mktj.cn.web.scheduling;

import com.mktj.cn.web.service.UserService;
import com.mktj.cn.web.service.imp.UserServiceImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Jobs {
    private final static Logger log = LoggerFactory.getLogger(Jobs.class);
    @Autowired
    UserService userService;
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void cronJob() {
        log.debug("upgradeUerRoleType begin");
        userService.upgradeUerRoleType();
        log.debug("upgradeUerRoleType end");
    }
}