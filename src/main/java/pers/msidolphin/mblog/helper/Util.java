package pers.msidolphin.mblog.helper;

import java.util.*;

public class Util {

    private Util() {}

    public static final boolean isEmpty(Object o) {
        if (o == null) {
            return true;
        }
        if ((o instanceof String)) {
            String s = (String) o;
            return "".equals(s.trim());
        }
        if ((o instanceof List)) {
            return ((List) o).size() == 0;
        }
        if ((o instanceof Set)) {
            return ((Set) o).size() == 0;
        }
        if ((o instanceof Map)) {
            return ((Map) o).size() == 0;
        }
        if ((o instanceof Object[])) {
            return ((Object[]) o).length == 0;
        }
        return false;
    }

    public static final boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 数组转换为List
     * 没用Arrays.asList的原因是它返回的不是java.util.ArrayList类型
     * @param t
     * @param <T>
     * @return
     */
    public static <T>  List<T> asList(T ...t) {
        List<T> arrayList = new ArrayList<>();
        for(T e : t) {
            arrayList.add(e);
        }
        return arrayList;
    }

    /**
     * 返回两个List的差集
     * @param a
     * @param b
     * @param <T>
     * @return
     */
    public static <T> List<T> diff(List<T> a, List<T> b) {
        if(a != null && b != null) {
            List aCopy = new ArrayList(a);
            aCopy.removeAll(b);
            return aCopy;
        }
        return Collections.EMPTY_LIST;
    }
}
