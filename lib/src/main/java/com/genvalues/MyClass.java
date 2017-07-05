package com.genvalues;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class MyClass {

    public static void main(String[] args) {

        String test  = "yangfeihu";

      String test1 =  stringCopy(test);
        System.out.println(test1);
        System.out.println(test);
    }

    public final static Map mapCopy(Map map) {
        Map newMap = (Map)objectCopy(map);
        map.clear();
        return newMap;
    }

    public final static String stringCopy(String str) {
        String newString= (String)objectCopy(str);
        str = null;
        return newString;
    }

    public final static Object objectCopy(Object oldObj) {
        Object newObj = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(oldObj);
            ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(bi);
            newObj = oi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newObj;
    }
}









