package com.tcl.base.model;


import java.util.List;

/**
 * Created by yangfeihu on 2017/1/19.
 * 测试使用
 */

public class Student extends BaseBean{

    public List<info> infos;
    public static class  info{
        public String name;
        public int age;
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getAge() {
            return age;
        }
        public void setAge(int age) {
            this.age = age;
        }
    }

}
