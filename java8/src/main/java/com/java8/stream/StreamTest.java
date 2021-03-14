package com.java8.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    public static void main(String[] args) {
        minmaxdistinct();
    }

    /**
     * 对元素一对一转换大写
     */
    private static void toUpperCase() {
        Stream<String> stream = Stream.of("a", "b", "c");
        stream.map(String::toUpperCase).forEach(System.out::println);
    }

    /**
     * 过滤stream，生成结果是一个新的stream
     */
    private static void filterNum() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(nums).filter(n -> n < 3).forEach(System.out::println);
    }

    /**
     * foreach之后stream被消费了，一个stream只能被消费一次
     */
    private static void foreach1() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(nums).forEach(System.out::println);
    }

    /**
     * foreach不能修改本地值，也不能通过break/return提前结束遍历
     */
    private static void foreach2() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(nums).forEach(e -> {
            System.out.println(e);
            return;
        });
    }

    /**
     * 对同一个stream多次消费
     */
    private static void peek() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Stream<Integer> stream = Arrays.stream(nums);
        stream
                .peek(System.out::println)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    /**
     * 把元素组合起来 sum min max avg等都是特殊的reduce
     */
    private static void reduce() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        //有初始值
        Integer sum = Arrays.stream(nums).reduce(0, Integer::sum);
        //无初始值,返回optional
        Integer sum1 = Arrays.stream(nums).reduce(Integer::sum).get();

        System.out.println(sum);
    }

    /**
     * 返回前n个元素
     */
    private static void limit() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(nums).limit(3).forEach(System.out::println);
    }

    /**
     * 跳过前n个元素
     */
    private static void skep() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(nums).skip(2).forEach(System.out::println);
    }

    /**
     * 排序，比普通排序强在可以通过stream进行各种map、filter
     */
    private static void sorted() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        Arrays.stream(nums).sorted(Integer::compareTo).limit(3).forEach(System.out::println);
        Arrays.stream(nums).sorted(Integer::compareTo).skip(2).forEach(System.out::println);
    }

    /**
     * 取最大最小值以及排序
     */
    private static void minmaxdistinct() {
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 6};
        int a = Arrays.stream(nums).max(Comparator.naturalOrder()).get();
        int b = Arrays.stream(nums).min(Comparator.naturalOrder()).get();
        System.out.println(a);
        System.out.println(b);
        Arrays.stream(nums).distinct().forEach(System.out::println);
    }
}
