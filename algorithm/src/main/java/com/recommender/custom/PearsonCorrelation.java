package com.recommender.custom;

import java.util.List;

/**
 * 余弦算法计算相似度
 *
 * @author line
 */

public class PearsonCorrelation implements Base {


    // 通过余弦求相邻值，对比两行数据，方法有很多，列举的是余弦方法，也可用皮尔森方法
    public double pearsonCorrelation(List<Integer> a, List<Integer> b) {// 返回某两行的相似度值

        double sum1 = 0;
        double sum2 = 0;

        Object[] a2 = a.toArray();
        Object[] b2 = b.toArray();

        int aimcha;
        int usercha;
        double wei = 0;

        for (int j = 0; j < a.size(); j++) {
            aimcha = (Integer) a2[j];
            usercha = (Integer) b2[j];
            sum1 += aimcha * aimcha;
            sum2 += usercha * usercha;
        }

        for (int i = 0; i < a.size(); i++) {

            double light = 0;
            double right = 0;

            aimcha = (Integer) a2[i];
            usercha = (Integer) b2[i];
            light = aimcha / Math.sqrt(sum1);
            right = usercha / Math.sqrt(sum2);
            wei += light * right;

        }
        //相似度值
        return wei;
    }

}