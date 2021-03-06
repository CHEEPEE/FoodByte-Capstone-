package com.hungrypanda.hungrypanda.mapModels;

/**
 * Created by Keji's Lab on 22/01/2018.
 */

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class RestaurantMenuRatingMapModel {
    public String restaurantId;
    public String userId;
    public String userReview;
    public Float userRating;
    public String username;
    public String itemMenu;
    public String itemMenukey;
    private Date ItemTimeDate;
    public String imgUrl;

    public RestaurantMenuRatingMapModel(){

    }
    public RestaurantMenuRatingMapModel(String restaurantId, String userId,
                                        String userReview, Float userRating,
                                        String usrname,String menu,String itemkey,Date timeStamp,String imgUri){
        this.restaurantId = restaurantId;
        this.userId = userId;
        this.userReview = userReview;
        this.userRating = userRating;
        this.username = usrname;
        this.itemMenu = menu;
        this.itemMenukey = itemkey;
        this.ItemTimeDate = timeStamp;
        this.imgUrl = imgUri;

    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("restaurantID",restaurantId);
        result.put("userId",userId);
        result.put("userReview",userReview);
        result.put("userRating",userRating);
        result.put("username",username);
        result.put("imgUrl",imgUrl);
        result.put("ItemTimeDate",ItemTimeDate);
        return result;
    }
}
