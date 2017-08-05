package com.mktj.cn.web.service;

/**
 * Created by zhanwa01 on 2017/6/30.
 */
public interface O2OCacheService {
    String executeCacheBaseData();

    void clear2ndLevelHibernateCache();
}
