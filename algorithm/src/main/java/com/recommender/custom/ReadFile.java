package com.recommender.custom;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;

import java.io.File;

import java.io.FileReader;

public class ReadFile implements Base {

//从文件中读取数据，以“ ”划分

    public int[][] readFile(String fileName) {

        int[][] user_movie = new int[PREFROWCOUNT][COLUMNCOUNT];//存放数据

        try {

            Resource classPathResource = new ClassPathResource(fileName);
            File file = classPathResource.getFile();

            FileReader fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);

            String line = "";

            while (br.ready()) {

                line = br.readLine();//按行获取数据

                String[] data = line.split("\t");//以“TAB”符来分割每行的四个数据数据获取userid，score，product

                int[] ddd = new int[4];

                for (int j = 0; j < data.length; j++) {

                    ddd[j] = Integer.parseInt(data[j]);

                }

                user_movie[ddd[0] - 1][ddd[1] - 1] = ddd[2];//因为数组的索引是从0开始，而商品和用户id是从1开始，故减去1

            }

        } catch (Exception ex) {

            ex.printStackTrace();//如果方法出现错误，会被抓住，在控制台输出错误原因

        }

        return user_movie;

    }

//从文件中读取数据,以“|”划分

    public int[] readFileGener(String fileName) {

        int[] user_genre_base = new int[PREFROWCOUNT];//存放数据

        try {

            Resource classPathResource = new ClassPathResource(fileName);
            File file = classPathResource.getFile();

            FileReader fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);

            String line = "";

            int i = 0;

            while (br.ready()) {

                line = br.readLine();//按行获取数据

                String[] data = line.split("\\|");

                if (data[2].equals("M")) {//男性设为1

                    user_genre_base[i] = 1;

                } else {
                    user_genre_base[i] = 0;//女性
                }

                i++;

            }

        } catch (Exception ex) {

            ex.printStackTrace();//如果方法出现错误，会被抓住，在控制台输出错误原因

        }

        return user_genre_base;

    }

//获取items-user矩阵

    public int[][] readFileItems(String fileName) {

        int[][] items_movie = new int[COLUMNCOUNT][PREFROWCOUNT];//存放数据

        try {

            File file = new File(fileName);

            FileReader fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);

            String line = "";

            while (br.ready()) {//矩阵中循环列

                line = br.readLine();

                String[] data = line.split("\t");

                int itemsId = Integer.parseInt(data[1]);

                int userId = Integer.parseInt(data[0]);

                items_movie[itemsId - 1][userId - 1] = Integer.parseInt(data[2]);

            }

        } catch (Exception ex) {

            ex.printStackTrace();//如果方法出现错误，会被抓住，在控制台输出错误原因

        }

        return items_movie;

    }

//获取items-gener矩阵

    public int[][] readFileItemsGener(String fileName) {

        int[][] items_movie = new int[COLUMNCOUNT][ITEMS_GENRE_LINE];//存放数据

        try {

            File file = new File(fileName);

            FileReader fr = new FileReader(file);

            BufferedReader br = new BufferedReader(fr);

            String line = "";

            while (br.ready()) {//矩阵中循环列

                line = br.readLine();

                String[] data = line.split("\\|");

                int itemsId = Integer.parseInt(data[0]);

                int j = 0;

                for (int i = data.length - ITEMS_GENRE_LINE; i < data.length; i++) {

                    items_movie[itemsId - 1][j] = Integer.parseInt(data[i]);

                    j++;

                }

            }

        } catch (Exception ex) {

            ex.printStackTrace();//如果方法出现错误，会被抓住，在控制台输出错误原因

        }

        return items_movie;

    }

}