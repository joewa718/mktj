package com.mktj.cn.util;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by waxi7002 on 2017/3/26.
 */
public class GsonUtil {
    /**
     * dtoList  conversion to List<Map<String, Object>>
     *
     * @param list dtoList
     * @return List<Map<String, Object>>
     */
    public static <T> List<Map<String, Object>> getListWithMap(List<T> list) {
        List<Map<String, Object>> gsonList = new ArrayList<Map<String, Object>>();
        if (null == list || list.isEmpty()) {
            return gsonList;
        }
        Gson gson = new Gson();
        String jsonStr = gson.toJson(list);
        gsonList = gson.fromJson(jsonStr, new ArrayList<Map<String, Object>>().getClass());
        return gsonList;
    }
}
