package com.mktj.cn.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtility {
    public static <T> List<T> findAll(Predicate<T> criteria, List<T> list) {
        return list.stream().filter(criteria).collect(Collectors.<T>toList());
    }

    public static <T> Optional<T> findSingle(Predicate<T> criteria, List<T> list) {
        return list.stream().filter(criteria).findAny();
    }

    public static <T> Boolean exists(Predicate<T> criteria, List<T> list) {
        return list.stream().filter(criteria).findAny().isPresent();
    }

    public static <T> long count(Iterable<T> list) {
        long num = 0;
        if (list != null) {
            for (T single : list) {
                num++;
            }
        }
        return num;
    }

    public static <T> List<T> toList(Iterable<T> iterable) {
        if (iterable instanceof List) {
            return (List<T>) iterable;
        }
        ArrayList<T> list = new ArrayList<>();
        if (iterable != null) {
            for (T e : iterable) {
                list.add(e);
            }
        }
        return list;
    }
}
