package com.hungrypanda.hungrypanda.recyclerviewAdapters;

import android.content.Context;
import android.media.Rating;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungrypanda.hungrypanda.AppModules.GlideApp;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.datamodels.CategoryModel;
import com.hungrypanda.hungrypanda.datamodels.MenuItemGrid;
import com.hungrypanda.hungrypanda.datamodels.RestaurantUserRating;
import com.hungrypanda.hungrypanda.mapModels.CategoryMapModel;
import com.hungrypanda.hungrypanda.utils.Utils;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 26/12/2017.
 */

public class UserRatingRecycler extends RecyclerView.Adapter<UserRatingRecycler.MyViewHolder> {

    private ArrayList<RestaurantUserRating> restaurantUserRatingArray;
    private Context context;
    private String restaurantId;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView lblUsername,lblRatingReview;
        public RatingBar ratingBar;
        public MyViewHolder(View view){
            super(view);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            lblUsername = (TextView) view.findViewById(R.id.lblUsername);
            lblRatingReview = (TextView) view.findViewById(R.id.lblUserReview);

        }
    }

    public UserRatingRecycler(Context c, ArrayList<RestaurantUserRating> RestaurantUserRatingArray, String restaurantId){
        this.restaurantUserRatingArray = RestaurantUserRatingArray;
        this.context = c;
        this.restaurantId = restaurantId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rating_list,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final UserRatingRecycler.MyViewHolder holder, final int position) {
       RestaurantUserRating restaurantUserRating = restaurantUserRatingArray.get(position);
       holder.ratingBar.setRating(Float.parseFloat(restaurantUserRating.getUserRating().toString()));
       System.out.println(restaurantUserRating.userReview());
       holder.lblUsername.setText(restaurantUserRating.getUsername());
       holder.lblRatingReview.setText(restaurantUserRating.userReview());

    }

    @Override
    public int getItemCount() {
        return restaurantUserRatingArray.size();
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