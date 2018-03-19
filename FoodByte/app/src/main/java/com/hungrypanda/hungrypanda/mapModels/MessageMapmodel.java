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

public class MessageMapmodel {

    public String msgkey;
    public String message;
    public String timestamp;
    public String username;
    public String userID;
    public String userImg;

    public MessageMapmodel(){

    }
    public MessageMapmodel(String key,String msg,String time,String username,String uid,String url){
        this.msgkey = key;
        this.message = msg;
        this.timestamp = time;
        this.username = username;
        this.userID = uid;
        this.userImg = url;
    }
    @Exclude
    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("msgkey",msgkey);
        result.put("message",message);
        result.put("timestamp",timestamp);
        result.put("username",username);
        result.put("userID",userID);
        result.put("userImg",userImg);
        return result;
    }
}
