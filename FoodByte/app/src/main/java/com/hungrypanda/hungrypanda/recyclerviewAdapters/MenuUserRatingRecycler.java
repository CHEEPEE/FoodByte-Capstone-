package com.hungrypanda.hungrypanda.recyclerviewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hungrypanda.hungrypanda.AppModules.GlideApp;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.datamodels.MenuRestaurantUserRating;
import com.hungrypanda.hungrypanda.datamodels.RestaurantUserRating;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Keji's Lab on 26/12/2017.
 */

public class MenuUserRatingRecycler extends RecyclerView.Adapter<MenuUserRatingRecycler.MyViewHolder> {

    private ArrayList<MenuRestaurantUserRating> menuRestaurantUserRatings;
    private Context context;
    private String restaurantId;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView lblUsername,lblRatingReview,lbl_rating_num;
        public RatingBar ratingBar;
        public CircleImageView circleImageView;
        public MyViewHolder(View view){
            super(view);
            circleImageView = (CircleImageView) view.findViewById(R.id.circleImageView);
            lbl_rating_num = (TextView) view.findViewById(R.id.lbl_rating_num);
            ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            lblUsername = (TextView) view.findViewById(R.id.lblUsername);
            lblRatingReview = (TextView) view.findViewById(R.id.lblUserReview);

        }
    }

    public MenuUserRatingRecycler(Context c, ArrayList<MenuRestaurantUserRating> menuRestaurantUserRatings, String restaurantId){
        this.menuRestaurantUserRatings = menuRestaurantUserRatings;
        this.context = c;
        this.restaurantId = restaurantId;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_rating_list,parent,false);
        return new MyViewHolder(itemView);

    }
    @Override
    public void onBindViewHolder(final MenuUserRatingRecycler.MyViewHolder holder, final int position) {
       MenuRestaurantUserRating menuRestaurantUserRatingModel = menuRestaurantUserRatings.get(position);
       holder.ratingBar.setRating(Float.parseFloat(menuRestaurantUserRatingModel.getUserRating().toString()));
       System.out.println(menuRestaurantUserRatingModel.userReview());
       holder.lblUsername.setText(menuRestaurantUserRatingModel.getUsername());
       holder.lblRatingReview.setText(menuRestaurantUserRatingModel.userReview());
       holder.lbl_rating_num.setText(menuRestaurantUserRatingModel.getUserRating().toString());
       try {
           GlideApp.with(context).load(menuRestaurantUserRatingModel.getUserImageUrl()).placeholder(R.drawable.image_placeholder).centerCrop().into(holder.circleImageView);
       }catch (NullPointerException e){

       }

    }

    @Override
    public int getItemCount() {
        return menuRestaurantUserRatings.size();
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