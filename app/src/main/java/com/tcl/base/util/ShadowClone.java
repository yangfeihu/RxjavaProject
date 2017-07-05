package com.tcl.base.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by yangfeihu on 2017/5/8.
 */

public class ShadowClone {
    public final static Map mapCopy(Map<String,Object> map) {
        Map<String,Object> newMap = new HashMap();

        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Object value =  entry.getValue();
            newMap.put(key,value);
       }
       map.clear();
       return newMap;
    }

}
