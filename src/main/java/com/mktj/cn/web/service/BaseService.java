package com.mktj.cn.web.service;

import com.mktj.cn.web.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.List;

public abstract class BaseService {

    public static final String SORT_PREFIX = "+";
    @Autowired
    private SessionRegistry sessionRegistry;

    protected void removeSession(User user) {
        for (Object userDetail : sessionRegistry.getAllPrincipals()) {
            String userName = ((org.springframework.security.core.userdetails.User) userDetail)
                    .getUsername();
            if (userName.equals(user.getUsername())) {
                removeSession(userDetail);
            }
        }
    }


    protected void removeSession(Object principal) {
        List<SessionInformation> sessionInformationList = sessionRegistry
                .getAllSessions(principal, false);
        for (SessionInformation sessionInformation : sessionInformationList) {
            sessionInformation.expireNow();
        }
    }

    private PageRequest buildPageRequest(int pageNumber, int pageSize) {
        return new PageRequest(pageNumber - 1, pageSize);
    }

    private PageRequest buildPageRequest(int pageNumber, int pageSize, Sort sort) {
        return new PageRequest(pageNumber - 1, pageSize, sort);
    }


}
