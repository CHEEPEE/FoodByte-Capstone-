package com.hungrypanda.hungrypanda.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.datamodels.MenuItemGrid;
import com.hungrypanda.hungrypanda.datamodels.RestaurantUserRating;
import com.hungrypanda.hungrypanda.datamodels.StoreProfileModel;
import com.hungrypanda.hungrypanda.mapModels.MenusItemMapModel;
import com.hungrypanda.hungrypanda.mapModels.RatingMapModel;
import com.hungrypanda.hungrypanda.mapModels.RestaurantLocationMapModel;
import com.hungrypanda.hungrypanda.mapModels.StoreProfileInformationMap;
import com.hungrypanda.hungrypanda.recyclerviewAdapters.RecyclerViewRestaurantMenusAdapter;
import com.hungrypanda.hungrypanda.recyclerviewAdapters.UserRatingRecycler;
import com.hungrypanda.hungrypanda.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScrollingActivity extends AppCompatActivity {
private  String restaurantID;
TextView lblRestaurantName,lblRestorLocation;
DatabaseReference mDatabase;
CircleImageView restaurantIcon;
ImageView storeProfileBackground,ic_review,ic_map,ic_chat;
TextView ic_rating;
private Context context;
FirebaseAuth mAuth;

Activity applicationActivty;
private ArrayList<StoreProfileModel> arrayStoreProfile = new ArrayList<>();
private ArrayList<MenuItemGrid> menuItemGridArrayList = new ArrayList<>();
RecyclerView rv_MenuItemList;
ArrayList<Integer> ArrayRating =new ArrayList<>();
UserRatingRecycler userRatingRecycler;
ArrayList<RestaurantUserRating> restaurantUserRatingsArray = new ArrayList<>();
RecyclerView userRatingListRV;



RecyclerViewRestaurantMenusAdapter recyclerViewRestaurantMenusAdapter;
RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        context = ScrollingActivity.this;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        restaurantID = getIntent().getExtras().getString("key");
        lblRestaurantName = (TextView) findViewById(R.id.lblRestoName);
        lblRestorLocation = (TextView)findViewById(R.id.lblRestoLocation);
        restaurantIcon = (CircleImageView) findViewById(R.id.restaurantIcon);
        storeProfileBackground = (ImageView) findViewById(R.id.storeProfileBackground);
        rv_MenuItemList = (RecyclerView) findViewById(R.id.rv_menuItemList);
        ic_review = (ImageView) findViewById(R.id.ic_raviews);
        ic_rating = (TextView) findViewById(R.id.ic_rating);
        ic_map = (ImageView) findViewById(R.id.ic_map);
        ic_chat = (ImageView) findViewById(R.id.chat);


        System.out.println(mAuth.getCurrentUser().getDisplayName());
        mDatabase.child(Utils.storeProfiles).child(restaurantID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StoreProfileModel storeProfileModel = new StoreProfileModel();
                StoreProfileInformationMap storeProfileInformationMap = dataSnapshot.getValue(StoreProfileInformationMap.class);
                storeProfileModel.setStoreName(storeProfileInformationMap.storeName);
                storeProfileModel.setStoreBannerUrl(storeProfileInformationMap.storeBannerUrl);
                storeProfileModel.setStoreProfileUrl(storeProfileInformationMap.storeProfileUrl);
                storeProfileModel.setStoreAddress(storeProfileInformationMap.storeAddress);
                storeProfileModel.setStoreContact(storeProfileInformationMap.storeContact);
                storeProfileModel.setRestaurantID(storeProfileInformationMap.restaurantID);
                arrayStoreProfile.add(storeProfileModel);
                lblRestaurantName.setText(arrayStoreProfile.get(0).getStoreName());
                lblRestorLocation.setText(arrayStoreProfile.get(0).getStoreAddress());
                Glide.with(context).load(arrayStoreProfile.get(0).getStoreProfileUrl()).into(restaurantIcon);
                Glide.with(context).load(arrayStoreProfile.get(0).getStoreBannerUrl()).into(storeProfileBackground);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ic_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ScrollingActivity.this,ChatActivity.class);
                i.putExtra("restoID",arrayStoreProfile.get(0).getRestaurantID());
                startActivity(i);
                finish();

            }
        });
        mDatabase.child(Utils.restaurantRating).child(restaurantID

        ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayRating.clear();
            restaurantUserRatingsArray.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RestaurantUserRating restaurantUserRating = new RestaurantUserRating();
                    RatingMapModel ratingMapModel = dataSnapshot1.getValue(RatingMapModel.class);
                    ArrayRating.add(ratingMapModel.userRating);
                    System.out.println(ratingMapModel.userRating+"");
                    restaurantUserRating.setUserRating(ratingMapModel.userRating);
                    restaurantUserRating.setUserReview(ratingMapModel.userReview);
                    restaurantUserRating.setUsername(ratingMapModel.username);
                    restaurantUserRatingsArray.add(restaurantUserRating);
                }
                ic_rating.setText((""+aveRating(ArrayRating)).substring(0,3));
                System.out.println(aveRating(ArrayRating)+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ic_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child(Utils.restaurantLocation).child(restaurantID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        RestaurantLocationMapModel restaurantLocationMapModel = dataSnapshot.getValue(RestaurantLocationMapModel.class);
                        double restoLat = restaurantLocationMapModel.locationLatitude;
                        double restoLong = restaurantLocationMapModel.locationLongitude;

                        Intent i  = new Intent(ScrollingActivity.this,PolyActivity.class);
                        i.putExtra("lat",restoLat);
                        i.putExtra("long",restoLong);
                        startActivity(i);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back_orange_24dp));

        mDatabase.child(Utils.restaurantItems).child(restaurantID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                menuItemGridArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    MenuItemGrid menuItemGrid = new MenuItemGrid();
                    MenusItemMapModel menusItemMapModel = dataSnapshot1.getValue(MenusItemMapModel.class);
                    menuItemGrid.setiName(menusItemMapModel.itemName);
                    menuItemGrid.setItemBannerUrl(menusItemMapModel.itemBannerURL);
                    menuItemGrid.setItemCategory(menusItemMapModel.itemCategory);
                    menuItemGrid.setItemKey(menusItemMapModel.itemKey);
                    menuItemGrid.setItemPrice(menusItemMapModel.itemPrice);
                    menuItemGrid.setItemCode(menusItemMapModel.itemCode);
                    if (menusItemMapModel.itemPublic){
                        menuItemGridArrayList.add(menuItemGrid);
                    }
                }
                recyclerViewRestaurantMenusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerViewRestaurantMenusAdapter = new RecyclerViewRestaurantMenusAdapter(context,menuItemGridArrayList,restaurantID);
        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rv_MenuItemList.setLayoutManager(layoutManager);
        rv_MenuItemList.setAdapter(recyclerViewRestaurantMenusAdapter);
        rv_MenuItemList.setNestedScrollingEnabled(false);
        applicationActivty = ScrollingActivity.this;
        final ratingAndReviewDialog alert = new ratingAndReviewDialog();
        ic_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.showDialog(applicationActivty);
            }
        });
        ic_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ratingListDialog ratingListDialog = new ratingListDialog();
                ratingListDialog.showDialog(applicationActivty);
            }
        });


    }

    public class ratingListDialog{
        TextView lblUsername,lblUserRating;

        public void showDialog(Activity activity) {
            final Dialog dialog = new Dialog(activity);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.user_rating_dialog);


            userRatingRecycler = new UserRatingRecycler(context,restaurantUserRatingsArray,restaurantID);
            RecyclerView.LayoutManager dlayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
            userRatingListRV = (RecyclerView) dialog.findViewById(R.id.UserRatingRV);
            userRatingListRV.setLayoutManager(dlayoutManager);
            userRatingListRV.setAdapter(userRatingRecycler);

            dialog.show();

        }
    }
    public class ratingAndReviewDialog {
        int ratingNumber=0;
        ImageView starOne,starTwo,starThree,starFour,starFive;
        EditText review;
        Button done;
        public void showDialog(Activity activity){
            final Dialog dialog = new Dialog(activity);

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.write_review);
            starOne = (ImageView) dialog.findViewById(R.id.star1);
            starTwo = (ImageView) dialog.findViewById(R.id.star2);
            starThree = (ImageView) dialog.findViewById(R.id.star3);
            starFour = (ImageView) dialog.findViewById(R.id.star4);
            starFive = (ImageView) dialog.findViewById(R.id.star5);
            review  =(EditText) dialog.findViewById(R.id.input_review);
            done = (Button)dialog.findViewById(R.id.bnt_done);
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ratingNumber ==0){

                    }else {
                        //restorating -> restoid -> userid -> userRating
                        // -> userReview
                        RatingMapModel ratingMapModel = new RatingMapModel(restaurantID,mAuth.getUid(),review.getText().toString(),ratingNumber,mAuth.getCurrentUser().getDisplayName());
                        Map<String,Object> postValue = ratingMapModel.toMap();
                        Map<String,Object> childUpdates = new HashMap<>();
                        childUpdates.put(mAuth.getUid(),postValue);
                        mDatabase.child(Utils.restaurantRating).child(restaurantID).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                            }
                        });



                    }
                }
            });
            starOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setStarRating(1);
                    ratingNumber =1;
                }
            });
            starTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingNumber =2;
                    setStarRating(2);
                }
            });
            starThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingNumber =3;
                    setStarRating(3);
                }
            });
            starFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingNumber =4;
                    setStarRating(4);
                }
            });
            starFive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ratingNumber =5;
                    setStarRating(5);
                }
            });
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
        }
        public void setStarRating(int rating){
            ratingNumber = rating;
            switch (rating){
                case 1:
                    starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    starFive.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    break;
                case 2:
                    starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    starFive.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    break;
                case 3:
                    starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    starFive.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    break;

                case 4:
                    starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starFive.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border_black_24dp));
                    break;

                case 5:
                    starOne.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starTwo.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starThree.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starFour.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
                    starFive.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_black_24dp));
            }
        }

    }
    private Double aveRating(ArrayList<Integer> rating){
        double totalRating = 0;
        for (int i = 0;i<rating.size();i++){
            totalRating =totalRating +rating.get(i);
        }

        return totalRating/rating.size();
    }




}
