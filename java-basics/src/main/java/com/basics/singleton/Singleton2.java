package com.basics.singleton;

/**
 * 懒汉式
 * 静态内部类实现单例模式（JVM保证，虚拟机加载类的时候只加载一次）
 * 加载外部类时不会加载内部类，实现懒加载
 */
public class Singleton2 {

    /**
     * 私有构造器仅被调用一次，如果在此处实例化共有的静态final域，享有特权的客户端可以借助AccessibleObject.setAccessible方法通过反射机制
     * 调用私有构造器，抵御这种🐓的方式是通过修改构造器，让它在第二次被要求创建实例的时候抛出异常
     */

    private Singleton2() {
    }

    private static class SingletonHolder {
        private final static Singleton2 INSTANCE = new Singleton2();
    }

    public static Singleton2 getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(Singleton2.getInstance().hashCode());
            }).start();
        }
    }
}
