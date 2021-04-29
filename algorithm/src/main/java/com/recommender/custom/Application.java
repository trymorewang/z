package com.recommender.custom;

import java.util.Arrays;

import java.util.HashMap;

import java.util.Map;

import java.util.Scanner;

import java.util.Set;

public class Application implements Base {

    public static void main(String[] args) {

        // 输入userId，并获取
        System.out.println("请输入一个用户Id（1、2、3……943）");
        Scanner scanner = new Scanner(System.in);

        //获取得到输入的userId
        int userId = scanner.nextInt();

        // 从文件中读取数据
        int[][] user_movie_base = new int[PREFROWCOUNT][COLUMNCOUNT];

        //读取文件中的数据
        user_movie_base = new ReadFile().readFile(BASE);

        //产生相似度矩阵
        double[] similarityMatrix = new ProduceSimilarityMatrix().produceSimilarityMatrix(user_movie_base, userId);

        // 知道每个用户之间的相似度值之后，开始获取每隔相似值对应的userId，然后和相似值关联，再根据相似值排序，即得到相似爱好的userId，然后再输出相似推荐的商品
        int[] id = new int[KNEIGHBOUR];//存放K个最近邻userId

        //产生一个临时相似度矩阵变量，是为了相似度排序时和userid对应
        double[] tempSimilarity = new double[similarityMatrix.length];

        System.arraycopy(similarityMatrix, 0, tempSimilarity, 0, tempSimilarity.length);

        Arrays.sort(tempSimilarity);//排序，升序

        int flag = 0;//临时变量

        double[] similarity = new double[KNEIGHBOUR];//保存前K个相似度，从大到小

        for (int m = tempSimilarity.length - 1; m >= tempSimilarity.length - KNEIGHBOUR; m--) {
            for (int j = 0; j < similarityMatrix.length; j++) {
                if (similarityMatrix[j] == tempSimilarity[m] && similarityMatrix[j] != 0.0) {
                    similarity[flag] = tempSimilarity[m];
                    //保存前K个相似度的userid
                    id[flag] = j;
                    flag++;
                }
            }
        }

        System.out.println("相似度最近的" + KNEIGHBOUR + "个用户是：");
        System.out.print("近邻用户");
        //格式化输出"%25s"是占多少位
        System.out.printf("%25s", "相似度");
        System.out.printf("%30s\n", "推荐产品");
        //存放每件商品的id和期望值，是键值对关系，即一对一
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < KNEIGHBOUR; i++) {//按照k值得大小来循环

            // 前k个近邻用户的推荐产品
            //数组id中的userid根据相似度大小顺序已经排好，从大到小
            int user_id = id[i];
            // 获取源数据K个邻近用户userid的所有评分
            int[] items = user_movie_base[user_id];
            String str = "";
            //循环每件商品，如果相邻用户对某件商品的评分不为0，而目标用户的评分为0，该商品就为推荐商品
            for (int j = 0; j < COLUMNCOUNT; j++) {
                if ((items[j] != 0) && (user_movie_base[userId - 1][j] == 0)) {
                    str += " " + (j + 1);//将推荐商品的id保存在一个字符串中，可以直接输出

                    //此时，可以通过循环计算某一件推荐商品的评分用户的相似度期望

                    //开始计算期望，将相同商品的相似度相加，并保存在map集合中

                    //如果一件商品的值，已经保存在map集合的键中（键是唯一的，即不会和其他的数值一样），那么键对应的值，就会改变，加上该商品不用用户的相似度
                    if (map.containsKey(j + 1)) {
                        double d = map.get(j + 1);
                        d += similarity[i];
                        //修改map中的值
                        map.put(j + 1, d);
                    } else {
                        //如果没有保存一件商品的id，那么开始保存
                        map.put(j + 1, similarity[i]);
                    }
                }
            }

            System.out.print(id[i] + 1);
            //输出的同时格式化数据
            System.out.printf("%16s\t", String.format("%.2f", similarity[i] * 100) + "%");
            //输出每个用户的推荐商品
            System.out.println(str);
        }

        //选择最好的推荐商品,期望加权
        //循环map集合的键
        Map<Integer, Double> map2 = new HashMap<>(); //保存商品id和加权期望,因为还要对加权期望排序，要和商品id对应

        double s1 = 0;
        double s2 = 0;

        Set<Integer> set = map.keySet();//获取map集合中的所有键，输出是一个set集合
        for (int key : set) {//循环map中的所有键
            for (int i = 0; i < KNEIGHBOUR; i++) {
                int score = user_movie_base[id[i]][key - 1];//map中的键是商品id，i是userid，获取评分
                s1 += score * map.get(key);
                s2 += score;
            }
            map2.put(key, s1 / s2);//保存加权期望值，和商品id对应
        }

        Object[] arr = map2.values().toArray();//获取map2中所有的值，也就是每件商品的加权期望
        Arrays.sort(arr);//升序排列，调用系统数据包中的函数，自动排列数组
        set = map2.keySet();//获取商品id
        int max = 0;//最佳推荐项目id
        for (int key : set) {//循环商品id，根据最大的加权期望，找到商品id
            if (map2.get(key) == arr[arr.length - 1]) {
                max = key;
                break;
            }
        }

        System.out.println("最值得推荐的商品是：" + max);

        // 误差率
        // 462个用户的实际评分
        int[][] test = new ReadFile().readFile(TEST);
        //获取任意两行之间的相似度矩阵
        double[][] similarityMatrix2 = new ProduceSimilarityMatrix().produceSimilarityMatrix(user_movie_base);

        double[][] matrix = new GetScore().getScore(user_movie_base, similarityMatrix2);

        double[] mae = new ProduceMAE().produceMAE(matrix, test);

        //平均绝对误差，通过两大组数据的相似度矩阵对比而来
        double Mae = 0.0, MAE = 0.0;

        for (double v : mae) {
            Mae += v;
        }

        MAE = Mae / TESTROWCOUNT;
        System.out.println("MAE=:" + MAE);
    }

}
