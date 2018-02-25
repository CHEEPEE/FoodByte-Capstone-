package com.hungrypanda.hungrypanda.datamodels;

import java.util.Date;

/**
 * Created by Keji's Lab on 22/01/2018.
 */

public class MenuRestaurantUserRating {
    String restuarantId;
    String userId;
    String userReview;
    Float userRating;
    String username;
    String restaurantMenuItem;
    String itemMenuKey;
    Date ratingDateTime;

    public Date getRatingDateTime(){
        return ratingDateTime;
    }
    public String getItemMenuKey(){
        return itemMenuKey;
    }
    public String getRestuarantId(){
        return restaurantMenuItem;
    }
    public String getRestaurantId(){
        return restuarantId;
    }
    public String getUserId(){
        return userId;

    }
    public String getUsername(){
        return username;
    }
    public String userReview(){
        return userReview;
    }
    public Float getUserRating(){
        return userRating;
    }
    public void setRestuarantId(String restoId){
        this.restuarantId = restoId;
    }
    public void setUserId(String Uid){
        this.userId = Uid;
    }
    public void setUserReview(String userRev){
        this.userReview = userRev;
    }
    public void setUserRating(Float userRate){
        this.userRating = userRate;
    }
    public void setUsername(String usrname){
        this.username = usrname;
    }
    public void setRestaurantMenuItem(String itemMenuNam){
        this.restaurantMenuItem = itemMenuNam;
    }
    public void setItemMenuKey(String itemMenuKeyString){
        this.itemMenuKey = itemMenuKeyString;
    }
    public void setRatingDateTime(Date timeStamp){
        this.ratingDateTime = timeStamp;
    }

}

