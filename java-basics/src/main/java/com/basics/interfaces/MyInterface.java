package com.basics.interfaces;

public interface MyInterface {

    /**
     * jdk1.8中接口可以定义静态方法，直接通过接口调用，实现类不能调用此接口
     * jdk1.9允许接口拥有私有方法，使用场景：比如test1()和test2()都需要一个相同的输出语句，我们这个把这个输出语句抽象一个方法出来
     * 但是又不像被实现类调用
     */
    static void test1() {
        //原本实现
        //System.out.println("接口静态方法");

        //私有化方法实现
        sout();
    }

    static void test2() {
        //原本实现
        //System.out.println("接口静态方法");

        //私有化方法实现
        sout();
    }

    private static void sout() {
        System.out.println("接口静态方法");
    }
}
