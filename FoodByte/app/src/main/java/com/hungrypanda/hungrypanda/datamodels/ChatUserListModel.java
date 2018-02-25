package com.hungrypanda.hungrypanda.datamodels;

/**
 * Created by Keji's Lab on 26/11/2017.
 */

public class ChatUserListModel {

    String pushkey;
    String userID;
    String displayName;
    String timeStamp;
    String userPhoto;
    public String getPushkey(){
        return pushkey;
    }
    public String getUserID(){
        return userID;
    }
    public String getDisplayName(){
        return displayName;
    }
    public String getTimeStamp(){
        return timeStamp;
    }
    public String getUserPhoto(){
        return userPhoto;
    }
    public void setPushkey(String key){
        this.pushkey = key;
    }
    public void setUserID(String id){
        this.userID = id;
    }
    public void setDisplayName(String name){
        this.displayName = name;
    }
    public void setTimeStamp(String time){
        this.timeStamp = time;

    }
    public void setUserPhoto(String photo){
        this.userPhoto = photo;
    }

}
