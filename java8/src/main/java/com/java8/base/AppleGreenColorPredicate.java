package com.java8.base;

public class AppleGreenColorPredicate implements ApplePredicate {
    @Override
    public boolean test(Apple apple){
        return "green".equals(apple.getColor());
    }
}
