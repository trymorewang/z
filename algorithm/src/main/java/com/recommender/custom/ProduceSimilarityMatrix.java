package com.recommender.custom;

public class ProduceSimilarityMatrix implements Base {


    //在计算MAE会用到
    public double[][] produceSimilarityMatrix(int[][] preference) {

        //行和列都是所有的用户，因为是每一行和每一行相比，所以得到的相似矩阵为正方形
        double[][] similarityMatrix = new double[PREFROWCOUNT][PREFROWCOUNT];

        for (int i = 0; i < PREFROWCOUNT; i++) {
            for (int j = 0; j < PREFROWCOUNT; j++) {
                if (i == j) {
                    continue;
                }
                //数据是两行之间对比，其实只需要填满相似度矩阵的左下方或者右上方即可（减少重复运算）
                //参数是从第一行开始，和其他每一行比较相似度
                similarityMatrix[i][j] = new ComputeSimilarity().computeSimilarity(preference[i], preference[j]);
            }
        }
        return similarityMatrix;//返回相似度矩阵
    }

    //计算某个userId的相似度矩阵，用户之间的相似度是每个用户的每件商品评分的相似度，也就是说相似度矩阵是行是用户列也是用户，是正方形矩阵，对角线上的值都为1
    //参数i是输入的userid
    public double[] produceSimilarityMatrix(int[][] preference, int i) {
        //定义一个相似度矩阵，行和列都是所有的用户，因为是每一行和每一行相比，所以得到的相似矩阵为正方形
        double[] similarityMatrix = new double[PREFROWCOUNT];
        //循环和其他userId对比其所有商品
        for (int j = 0; j < PREFROWCOUNT; j++) {
            //不比较同行，i-1是因为数组索引比userid小1
            if (j == (i - 1)) {
                //跳出循环，继续下一次循环
                continue;
            }
            //参数是从第一行开始，和其他每一行比较相似度
            similarityMatrix[j] = new ComputeSimilarity().computeSimilarity(preference[i - 1], preference[j]);

        }
        //返回相似度矩阵，只有在userid-1行有数据，其他行列数据都为0，因为只是userid-1行和其他行对比
        return similarityMatrix;
    }

    //根据性别属性，产生用户性别属性相似度
    public double[] produceSimilarityMatrixGener(int[] preference, int userId) {

        //定义一个相似度矩阵，行和列都是所有的用户，因为是每一行和每一行相比，所以得到的相似矩阵为正方形
        double[] similarityMatrix = new double[PREFROWCOUNT];

        //循环和其他userId对比其所有商品
        for (int j = 0; j < PREFROWCOUNT; j++) {
            //不比较同行，i-1是因为数组索引比userid小1
            if (j == (userId - 1)) {
                //跳出循环，继续下一次循环
                continue;
            }

            if (preference[j] == preference[userId - 1]) {
                similarityMatrix[j] = 1;
            } else {
                similarityMatrix[j] = 0;
            }
        }

        return similarityMatrix;//返回相似度矩阵，只有在userid-1行有数据，其他行列数据都为0，因为只是userid-1行和其他行对比

    }

    //基于项目
    public double[] produceSimilarityMatrixItems(int[][] preference, int i) {
        double[] similarityMatrix = new double[COLUMNCOUNT];
        for (int j = 0; j < COLUMNCOUNT; j++) {
            //不比较同行
            if (j == (i - 1)) {
                //跳出循环，继续下一次循环
                continue;
            }
            //参数是从第一行开始，和其他每一行比较相似度
            similarityMatrix[j] = new ComputeSimilarity().computeSimilarity(preference[i - 1], preference[j]);

        }
        //返回相似度矩阵，只有在userid-1行有数据，其他行列数据都为0，因为只是userid-1行和其他行对比
        return similarityMatrix;
    }
}
