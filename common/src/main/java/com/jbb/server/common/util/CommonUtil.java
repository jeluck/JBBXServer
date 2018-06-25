package com.jbb.server.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommonUtil {
    public static boolean isNullOrEmpty(Collection<?> obj) {
        return (obj == null) || obj.isEmpty();
    }

    public static boolean isNullOrEmpty(Map<?, ?> obj) {
        return (obj == null) || obj.isEmpty();
    }

    public static boolean isNullOrEmpty(Object[] obj) {
        return (obj == null) || (obj.length == 0);
    }

    public static boolean isNullOrEmpty(int[] obj) {
        return (obj == null) || (obj.length == 0);
    }

    public static boolean isNullOrEmpty(byte[] obj) {
        return (obj == null) || (obj.length == 0);
    }

    public static <T> Set<T> addToSet(Set<T> c, T t) {
        if (c == null)
            c = new HashSet<>();
        c.add(t);
        return c;
    }

    public static <T> List<T> addToList(List<T> c, T t) {
        if (c == null)
            c = new ArrayList<>();
        c.add(t);
        return c;
    }

    public static boolean inArray(int value, int[] arr) {
        if (arr == null || arr.length == 0)
            return false;
        for (int v : arr)
            if (value == v)
                return true;
        return false;
    }

    public static boolean inArray(Object value, Object[] arr) {
        if (arr == null || arr.length == 0)
            return false;

        if (value == null) {
            for (Object v : arr)
                if (v == null)
                    return true;
        } else {
            for (Object v : arr)
                if (value.equals(v))
                    return true;
        }

        return false;
    }

    public static int[] toIntArray(Collection<Integer> list, int defaultValue) {
        if ((list == null) || list.isEmpty())
            return null;

        int[] arr = new int[list.size()];
        int i = 0;
        for (Integer l : list) {
            arr[i] = l == null ? defaultValue : l;
            i++;
        }

        return arr;
    }

    public static byte[] getNonEmptyOrNull(byte[] arr) {
        return ((arr != null) && (arr.length == 0)) ? null : arr;
    }

    public static int[] getNonEmptyOrNull(int[] arr) {
        return ((arr != null) && (arr.length == 0)) ? null : arr;
    }

    public static <T> T[] getNonEmptyOrNull(T[] arr) {
        return ((arr != null) && (arr.length == 0)) ? null : arr;
    }

    public static <T> List<T> getNonEmptyOrNull(List<T> list) {
        return ((list != null) && list.isEmpty()) ? null : list;
    }

}
