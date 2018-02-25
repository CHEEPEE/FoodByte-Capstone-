package com.hungrypanda.hungrypanda.datamodels;

/**
 * Created by Keji's Lab on 22/01/2018.
 */

public class RestaurantUserRating {
    String restuarantId;
    String userId;
    String userReview;
    Integer userRating;
    String username;

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
    public Integer getUserRating(){
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
    public void setUserRating(Integer userRate){
        this.userRating = userRate;
    }
    public void setUsername(String usrname){
        this.username = usrname;
    }
}

