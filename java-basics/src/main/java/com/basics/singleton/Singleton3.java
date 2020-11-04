package com.basics.singleton;

/**
 * 枚举保证单例
 *
 * 无偿提供序列化机制，绝对防止多次实例化，即使是面对复杂序列化或者反射🐓的时候
 * 不过如果Singleton必须拓展一个超类，而不是拓展Enum的时候，则不适宜使用这个方法
 */
public enum Singleton3 {

    INSTACE;

    public void m() {
        System.out.println("m");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                System.out.println(Singleton3.INSTACE.hashCode());
            }).start();
        }
    }

}
