package com.basics.singleton;

/**
 * 饿汗式
 * 类加载到内存就实例化一个单例
 * 缺点：不管是否用到，类装载时就完成实例化
 */
public class Singleton1 {

    private static final Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {}

    public static Singleton1 getInstance() {
        return INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(Singleton1.getInstance().hashCode());
            }).start();
        }
    }
}
