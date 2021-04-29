package com.recommender.custom;

import java.util.HashMap;

import java.util.Map;

import java.util.Set;

public class GetScore implements Base {

    //方法参数，一个是源数据，一个是通过源数据得到的相似度矩阵
    //得到预测评分矩阵，先循环行userid，得到每一个userid的K个近邻用户和相似度，再得到目标用户的预测项目

    public double[][] getScore(int[][] user_movie_base,double[][] combineMatrix ){

        //保存每个用户对评分为0的项目的预测值
        double[][] matrix = new double[PREFROWCOUNT][COLUMNCOUNT];

        //循环userid
        for (int i = 0; i < PREFROWCOUNT; i++) {

            //得到每一个userid的K个邻近相似度极其userid
            //存放K个最近邻userId
            int[] id = new int[KNEIGHBOUR];

            //产生一个临时相似度矩阵变量，是为了相似度排序时和userid对应
            double[] tempSimilarity = new double[combineMatrix[i].length];

            for (int j = 0; j < tempSimilarity.length; j++) {
                tempSimilarity[j] = combineMatrix[i][j];
            }


            //保存前K个相似度，从大到小
            double[] similarity = new double[KNEIGHBOUR];

            //存放K个邻近项目id
            int[] ids = new int[PREFROWCOUNT];

            for (int h = 0; h < PREFROWCOUNT; h++) {
                ids[h] = h;
            }

            for(int h=0;h<tempSimilarity.length;h++){
                for(int j=0;j<tempSimilarity.length-1-h;j++){

                    //如果后一个数小于前一个数交换
                    if(tempSimilarity[j]<tempSimilarity[j+1]){

                        double tmp=tempSimilarity[j];
                        tempSimilarity[j]=tempSimilarity[j+1];
                        tempSimilarity[j+1]=tmp;

                        int temp = ids[j];
                        ids[j] = ids[j+1];
                        ids[j+1] = temp;
                    }
                }
            }

            for (int h = 0;h < KNEIGHBOUR; h++) {
                similarity[h]=tempSimilarity[h];
            }

            for (int h = 0; h < KNEIGHBOUR; h++) {
                id[h]=ids[h];
            }

            //以上代码已经得到一个目标用户的K个相似度userid和相似度结束，并且已经排好顺序,分别是：数组id，和数组similarity

            //开始计算一个目标用户的推荐产品的预测评分,方法，K个邻近用户的相同商品的加权平均数

            //存放每件商品的id和商品评分*相似度
            Map<Integer, Double> map = new HashMap<Integer, Double>();

            //存放每件商品的id和相似度之和
            Map<Integer, Double> map2 = new HashMap<Integer, Double>();

            //按照k值得大小来循环
            for (int k = 0; k < KNEIGHBOUR; k++) {

                // 前k个近邻用户的推荐产品
                //数组id中的userid根据相似度大小顺序已经排好，从大到小
                int user_id = id[k];
                // 获取源数据K个邻近用户userid的所有评分
                int[] items = user_movie_base[user_id];
                //循环每件商品，如果相邻用户对某件商品的评分不为0，而目标用户的评分为0，该商品就为推荐商品
                for (int j = 0; j < COLUMNCOUNT; j++) {

                    if ((items[j] != 0) && (user_movie_base[i][j] == 0)){

                        //如果一件商品的值，已经保存在map集合的键中（键是唯一的，即不会和其他的数值一样），那么键对应的值，就会改变，加上该商品不用用户的相似度
                        if(map.containsKey(j)){

                            double d = map.get(j);
                            d+=similarity[k]*items[j];
                            map.put(j,d);//修改map中的值

                            double dd = map2.get(j);
                            dd+=similarity[k];
                            map2.put(j, dd);
                        }else{
                            //如果没有保存一件商品的id，那么开始保存
                            map.put(j, similarity[k]*items[j]);
                            map2.put(j, similarity[k]);
                        }
                    }
                }
            }
            //循环所有推荐商品
            Set<Integer> set = map.keySet();
            for(Integer key:set){
                matrix[i][key] = map.get(key)/map2.get(key);
            }
        }
        return matrix;
    }

}
