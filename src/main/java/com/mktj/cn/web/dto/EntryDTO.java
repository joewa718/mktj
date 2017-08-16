package com.mktj.cn.web.dto;

/**
 * @author zhanwang
 * @create 2017-08-03 18:01
 **/
public class EntryDTO<K, V> {

    private K key;
    private V value;

    public EntryDTO(K key,V value){
        this.key = key;
        this.value = value;
    }
    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
