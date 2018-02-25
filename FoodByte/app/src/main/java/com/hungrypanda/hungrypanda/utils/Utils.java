package com.hungrypanda.hungrypanda.utils;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Keji's Lab on 26/12/2017.
 */
public class Utils {
    public static String chatUserList = "chatUserList";
    public static String storeItemCategory = "storeItemCategory";
    public static String chattingSystem = "chattingSystem";
    public static String chatsList = "chatLists";
    public static String restaurantMenusUserRating = "restuarantMenusUserRating";
    public static String restaurantRating = "restaurantRatings";
    public static String storeProfiles = "storeProfiles";
    public static String restaurantItems = "restaurantItems";
    public static String restaurantLocation = "restaurantLocation";
    public static String deviceCurrentLocation = "deviceCurrentLocation";
    public static String testImageUrl ="https://firebasestorage.googleapis.com/v0/b/android-based-pos.appspot.com/o/images%2FrestaurantItems%2FBcUYltmzioggwtH0SCUYlKP9XWr2%2FIMG_20171127_222809.jpgandroid.os.ParcelFileDescriptor%24AutoCloseInputStream%40d2eb373%2FIMG_20171127_222809.jpg?alt=media&token=f3133eed-a3fc-4a66-9b11-8a827a23e1f5";
    public static void toster(Context context, String Text){
        Toast.makeText(context,Text,Toast.LENGTH_LONG).show();
    }
    public static class productItems{
        public static String iName = "iName";
        public static String itemCategory = "itemCategory";
        public static String itemPrice = "itemPrice";
        public static String itemBannerUrl = "itemBannerUrl";
        public static String itemKey = "itemKey";
    }

    public static class storeProfilesItem{
        public static String storeAddress ="storeAddress";
        public static String storeName = "storeName";
        public static String storeBannerUrl = "storeBannerUrl";
        public static String storeProfileUrl = "storeProfileUrl";
        public static String storeContact = "storeContact";
        public static String restaurantID ="restaurantID";

    }

    public Location getDeviceLocation(Context context){

        return null;
    }
    public static String getDateToStrig(){
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());
        String getDate = c.getTime().toString().substring(4,10);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime()).toString();

        return Utils.formatTheDate(formattedDate);
    }

    private static String formatTheDate(String date){
        // "yyyy.MM.dd.HH.mm.ss" present format to be converted
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);
        month = Utils.getMonthInWords(month);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat datef = new SimpleDateFormat("KK:mm a");
// you can get seconds by adding  "...:ss" to it
        datef.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String localTime = datef.format(currentLocalTime);
        System.out.println(month+" "+day+", "+year+" at "+localTime);
        return month+" "+day+", "+year+" at "+localTime;
    }

    private static String getMonthInWords(String num){
        int num_month = Integer.parseInt(num);
        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return months[num_month-1];
    }

}