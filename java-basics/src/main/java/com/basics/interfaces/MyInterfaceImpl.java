package com.basics.interfaces;

public class MyInterfaceImpl implements MyInterface, YourInterface {

    public static void main(String[] args) {
        MyInterface.test1();

        YourInterface.test1();
        //MyInterface.test2();
    }
}
