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

    protected PageRequest getPageRequest(com.mktj.cn.web.request.PageRequest request) {
        PageRequest pageRequest;
        if (request.getSort() != null) {
            if (request.getSort().startsWith(SORT_PREFIX)) {
                Sort sort = buildSort(request);
                pageRequest = this.buildPageRequest(request.getPage(), request.getSize(), sort);
            } else {
                Sort sort = buildSort(request);
                pageRequest = this.buildPageRequest(request.getPage(), request.getSize(), sort);
            }
        } else {
            Sort sort = buildSort(request);
            pageRequest = this.buildPageRequest(request.getPage(), request.getSize(), sort);
        }
        return pageRequest;
    }

    protected Sort buildSort(com.mktj.cn.web.request.PageRequest request) {
        Sort sort = null;
        if (request.getSort() != null) {
            if (request.getSort().startsWith(SORT_PREFIX)) {
                sort = new Sort(Sort.Direction.ASC, request.getSort().substring(1, request.getSort().length()));
            } else {
                sort = new Sort(Sort.Direction.DESC, request.getSort().substring(1, request.getSort().length()));
            }
        } else {
            sort = new Sort(Sort.Direction.ASC, "id");
        }
        return sort;
    }


}
