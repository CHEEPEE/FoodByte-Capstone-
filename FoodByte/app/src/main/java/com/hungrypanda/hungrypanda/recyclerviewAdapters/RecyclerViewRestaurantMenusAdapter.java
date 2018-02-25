package com.hungrypanda.hungrypanda.recyclerviewAdapters;

import android.app.Dialog;
import android.content.Context;
import android.media.Image;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.hungrypanda.hungrypanda.AppModules.GlideApp;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.datamodels.CategoryModel;
import com.hungrypanda.hungrypanda.datamodels.MenuItemGrid;
import com.hungrypanda.hungrypanda.datamodels.MenuRestaurantUserRating;
import com.hungrypanda.hungrypanda.datamodels.RestaurantUserRating;
import com.hungrypanda.hungrypanda.mapModels.CategoryMapModel;
import com.hungrypanda.hungrypanda.mapModels.RestaurantMenuRatingMapModel;
import com.hungrypanda.hungrypanda.utils.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Keji's Lab on 26/12/2017.
 */

public class RecyclerViewRestaurantMenusAdapter extends RecyclerView.Adapter<RecyclerViewRestaurantMenusAdapter.MyViewHolder> {

    private ArrayList<MenuItemGrid> menuItemGridArrayList;
    private ArrayList<CategoryModel> categoryList = new ArrayList<>();
    private ArrayList<MenuRestaurantUserRating> menuRestaurantUserRatingsArray = new ArrayList<>();
    private Context context;
    private String restaurantId;
    DatabaseReference mDatabase;
    private MenuUserRatingRecycler menuUserRatingRecycler;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView menuLabel,menuPrice,lblMenuCategory,lblReviewNumber;
        public ImageView menuBanner;
        public RatingBar ratingBar;
        public MyViewHolder(View view){
            super(view);
            lblReviewNumber = (TextView) view.findViewById(R.id.reviewNumber);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBarRestoMenu);
            lblMenuCategory = (TextView) view.findViewById(R.id.lblMenuCategory);
            menuLabel = (TextView) view.findViewById(R.id.menuLabel);
            menuPrice = (TextView) view.findViewById(R.id.menuPrice);
            menuBanner = (ImageView) view.findViewById(R.id.menuBanner);


        }
    }

    public RecyclerViewRestaurantMenusAdapter(Context c, ArrayList<MenuItemGrid> menuItemGridArrayList,String restaurantId){
        this.menuItemGridArrayList = menuItemGridArrayList;
        this.context = c;
        this.restaurantId = restaurantId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menus_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewRestaurantMenusAdapter.MyViewHolder holder, final int position) {
        final MenuItemGrid menuItemGridModel = menuItemGridArrayList.get(position);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        GlideApp.with(context).load(menuItemGridModel.getItemBannerUrl()).placeholder(R.drawable.image_placeholder).centerCrop().into(holder.menuBanner);
        holder.menuLabel.setText(menuItemGridModel.getiName());
        holder.menuPrice.setText("₱ "+menuItemGridModel.getItemPrice());

       // holder.lblMenuCategory.setText(menuItemGridModel.getItemCategory());
        System.out.println(restaurantId);
        FirebaseDatabase.getInstance().getReference().child(Utils.storeItemCategory).child(restaurantId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    CategoryModel categoryModel = new CategoryModel();
                    CategoryMapModel categoryMapModel =dataSnapshot1.getValue(CategoryMapModel.class);
                    categoryModel.setKey(categoryMapModel.key);
                    categoryModel.setCategory(categoryMapModel.category);
                    categoryList.add(categoryModel);

                }
                for (int i = 0;i<categoryList.size();i++){

                    if (menuItemGridModel.getItemCategory().equals(categoryList.get(i).getKey())){
                        holder.lblMenuCategory.setText(categoryList.get(i).getCategory());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        final DatabaseReference getMenusRatings = FirebaseDatabase.getInstance().getReference().child(Utils.restaurantMenusUserRating).child(restaurantId).child(menuItemGridArrayList.get(position).getItemKey());
        getMenusRatings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int ratingTotal = 0;
                ArrayList<Float> ratingArray  = new ArrayList<>();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    RestaurantMenuRatingMapModel restaurantMenuRatingMapModel = dataSnapshot1.getValue(RestaurantMenuRatingMapModel.class);
                    ratingArray.add(restaurantMenuRatingMapModel.userRating);

                }
                for (int i = 0;i<ratingArray.size();i++){
                    ratingTotal += ratingArray.get(i);

                }

                Float aveRating = Float.parseFloat(ratingTotal+"")/ratingArray.size();
                System.out.println(aveRating);
                holder.ratingBar.setRating(aveRating);
                if (aveRating.toString().equals("NaN")){
                    holder.lblReviewNumber.setText("0");
                }else {
                    holder.lblReviewNumber.setText(aveRating.toString().substring(0,1));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        holder.menuBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.setContentView(R.layout.rate_menu_layout);
                final TextInputEditText reviewEditText = (TextInputEditText) dialog.findViewById(R.id.reviewEditText);
                RecyclerView menuRatingList = (RecyclerView) dialog.findViewById(R.id.rating_review_list);
                ImageView itemImage = (ImageView) dialog.findViewById(R.id.item_img);
                TextView lblItemName = (TextView) dialog.findViewById(R.id.item_name);
                Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
                final RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.rating_bar_menu);
                final RecyclerView ratingReviewList = (RecyclerView) dialog.findViewById(R.id.rating_review_list);

                menuUserRatingRecycler = new MenuUserRatingRecycler(context,menuRestaurantUserRatingsArray,restaurantId);
                menuRatingList.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
                menuRatingList.setAdapter(menuUserRatingRecycler);
                lblItemName.setText(menuItemGridModel.getiName());
                menuUserRatingRecycler.notifyDataSetChanged();

                GlideApp.with(context).load(menuItemGridModel.getItemBannerUrl()).placeholder(R.drawable.image_placeholder).centerCrop().into(itemImage);
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference menuRating =   mDatabase.child(Utils.restaurantMenusUserRating).child(restaurantId).child(menuItemGridArrayList.get(position).getItemKey());
                    RestaurantMenuRatingMapModel restaurantMenuRatingMapModel =
                            new RestaurantMenuRatingMapModel(restaurantId,mAuth.getCurrentUser().getUid()
                            ,reviewEditText.getText().toString(),ratingBar.getRating(),mAuth.getCurrentUser().getDisplayName(),
                                    menuItemGridModel.getiName(),menuItemGridModel.getItemKey()
                            ,null);
                        Map<String,Object> postValue = restaurantMenuRatingMapModel.toMap();
                        Map<String,Object> childupdates = new HashMap<>();
                        childupdates.put(mAuth.getUid(),postValue);
                        menuRating.updateChildren(childupdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                            }
                        });
                    }
                });


                getMenusRatings.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        menuRestaurantUserRatingsArray.clear();
                       for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                           MenuRestaurantUserRating menuRestaurantUserRating = new MenuRestaurantUserRating();
                           RestaurantMenuRatingMapModel restaurantMenuRatingMapModel = dataSnapshot1.getValue(RestaurantMenuRatingMapModel.class);
                           menuRestaurantUserRating.setUsername(restaurantMenuRatingMapModel.username);
                           menuRestaurantUserRating.setUserRating(restaurantMenuRatingMapModel.userRating);
                           menuRestaurantUserRating.setUserReview(restaurantMenuRatingMapModel.userReview);
                           menuRestaurantUserRatingsArray.add(menuRestaurantUserRating);
                           menuUserRatingRecycler.notifyDataSetChanged();
                       }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return menuItemGridArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);

    }
    public interface OnItemLongClickListener{
        void onItemLongClick(View view, int posistion);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickListener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private OnItemLongClickListener monItemLongClickListener;

    public void setonItemLongClickListener(OnItemLongClickListener monItemLongClickListener){
        this.monItemLongClickListener = monItemLongClickListener;
    }

}