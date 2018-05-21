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

public class UserProfileMapModel {
    public String userId;
    public String userEmail;
    public String userPhoneNumber;
    public String userDisplayName;
    public String userImageUrl;


    public UserProfileMapModel(){

    }
    public UserProfileMapModel(String userId, String userEmail, String userPhoneNumber, String displayName, String imgUrl){
        this.userId = userId;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.userDisplayName = displayName;
        this.userImageUrl = imgUrl;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("userId",userId);
        result.put("userEmail",userEmail);
        result.put("userPhoneNumber",userPhoneNumber);
        result.put("userDisplayName",userDisplayName);
        result.put("userImageUrl",userImageUrl);

        return result;
    }
}
