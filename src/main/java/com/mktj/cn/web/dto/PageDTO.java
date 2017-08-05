package com.mktj.cn.web.dto;

import java.util.List;

/**
 * Created by zhanwa01 on 2016/12/2.
 */
public class PageDTO<T> {
    private String message;
    private long totalElements;
    private List<T> Content;

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return Content;
    }

    public void setContent(List<T> content) {
        Content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
