package com.mktj.cn.util;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

/**
 * Created by waxi7002 on 6/16/2017.
 */
@Component
public class CustomQuery {

    @PersistenceContext
    EntityManager entityManager;

    /**
     * * 查询数据集合
     * @param sql 查询sql sql中的参数用:name格式
     * @param params 查询参数map格式，key对应参数中的:name
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List queryListEntity(String sql, Map<String, Object> params, Class c) {
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        SQLQuery query = session.createSQLQuery(sql);
        if (params != null) {
            for (String key : params.keySet()) {
                Object param = params.get(key);
                if (param instanceof List) {
                    query.setParameterList(key, (List) param);
                } else {
                    query.setParameter(key, params.get(key));
                }
            }
        }
        query.setResultTransformer(Transformers.aliasToBean(c));
        List<T> result = query.list();
        return result;
    }
}
