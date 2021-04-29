package com.recommender.mahout.user;

import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.*;
import org.apache.mahout.cf.taste.impl.similarity.*;
import org.apache.mahout.cf.taste.impl.neighborhood.*;
import org.apache.mahout.cf.taste.impl.recommender.*;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.*;
import org.apache.mahout.cf.taste.neighborhood.*;
import org.apache.mahout.cf.taste.recommender.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.List;

public class RecommenderIntro {

    public static void main(String[] args) throws Exception {
        Resource classPathResource = new ClassPathResource("u1.base");
        File file = classPathResource.getFile();
        DataModel model = new FileDataModel(file);
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        LongPrimitiveIterator userIds = recommender.getDataModel().getUserIDs();
        LongPrimitiveIterator itemIds = recommender.getDataModel().getItemIDs();
        while (itemIds.hasNext()) {
            System.out.println(itemIds.nextLong());
        }
        List<RecommendedItem> recommendedItems = recommender.recommend(1, 20);

        for (RecommendedItem recommendedItem : recommendedItems) {
            System.out.println(recommendedItem);
        }
    }
}
