package com.hungrypanda.hungrypanda.mapModels;

/**
 * Created by Keji's Lab on 22/01/2018.
 */

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class RatingMapModel {
    public String restaurantId;
    public String userId;
    public String userReview;
    public Integer userRating;
    public String username;
    public String imgUri;

    public RatingMapModel(){

    }
    public RatingMapModel(String restaurantId, String userId,String userReview,Integer userRating,String usrname,String img){
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.userReview = userReview;
        this.userRating = userRating;
        this.username = usrname;
        this.imgUri = img;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("restaurantID",restaurantId);
        result.put("userId",userId);
        result.put("userReview",userReview);
        result.put("userRating",userRating);
        result.put("username",username);
        result.put("imgUri",imgUri);

        return result;
    }
}
