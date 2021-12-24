package com.java8.base;

import java.util.ArrayList;
import java.util.List;

public class BaseTest {

    public static List<Apple> filterApple(List<Apple> inventory, ApplePredicate p) {
        List<Apple> result = new ArrayList<>();
        inventory.forEach(a -> {
            if (p.test(a)) {
                result.add(a);
            }
        });
        return result;
    }

    public static void main(String[] args) {
        List<Apple> inventory = new ArrayList<>();
        filterApple(inventory, new AppleGreenColorPredicate());
    }
}
