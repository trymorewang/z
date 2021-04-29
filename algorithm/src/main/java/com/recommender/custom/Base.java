package com.recommender.custom;

public interface Base {

    /**
     * number of neighbors最近邻个数
     */
    int KNEIGHBOUR = 10;

    /**
     * number of items 项目总数
     */
    int COLUMNCOUNT = 1682;

    /**
     * number of users in base训练集上的用户数目
     */
    int PREFROWCOUNT = 943;

    /**
     * number of users in test测试集上的用户数目
     */
    int TESTROWCOUNT = 462;

    /**
     * 训练集
     */
    String BASE = "u1.base";

    /**
     * base数据集的行数
     */
    int BASE_LINE = 80000;

    /**
     * 测试集
     */
    String TEST = "u1.test";

    /**
     * test数据集的行数
     */
    int TEST_LINE = 20000;

    /**
     * 用户属性集
     */
    String BASE_GENRE = "u.user";

    /**
     * 用户属性集
     */
    String BASE_ITEMS_GENRE = "u.item";

    /**
     * test数据集的行数
     */
    int ITEMS_GENRE_LINE = 19;
}
