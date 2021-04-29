package com.recommender.custom;

import java.util.ArrayList;
import java.util.List;

/**
 * 比较两个用户相似度
 */
public class ComputeSimilarity {

    public double computeSimilarity(int[] item1, int[] item2) {

        //因为不知道两行userid的评分是否有效即都不为0，所以定义集合来储存不知道的有效评分
        List<Integer> list1 = new ArrayList<Integer>();
        List<Integer> list2 = new ArrayList<Integer>();

        for (int i = 0; i < item1.length; i++) {
            //如果相同列上有0就舍去
            if (item1[i] != 0 || item2[i] != 0) {
                //因为合格数据个数不确定，所以用集合表示
                list1.add(new Integer(item1[i]));
                list2.add(new Integer(item2[i]));
            }
        }
        //返回相似度值
        return new PearsonCorrelation().pearsonCorrelation(list1, list2);
    }
}
