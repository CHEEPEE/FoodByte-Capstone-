package com.hungrypanda.hungrypanda.recyclerviewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.datamodels.MessageDataModel;
import com.hungrypanda.hungrypanda.datamodels.RestaurantUserRating;
import com.hungrypanda.hungrypanda.mapModels.MessageMapmodel;

import java.util.ArrayList;

/**
 * Created by Keji's Lab on 26/12/2017.
 */

public class MessageRecycleAdapter extends RecyclerView.Adapter<MessageRecycleAdapter.MyViewHolder> {

    private ArrayList<MessageDataModel> messageDataModelArrayList;
    private Context context;
    private String restaurantId;



    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView chatBubbleUser,chatBubbleOther;
        public MyViewHolder(View view){
            super(view);
            chatBubbleUser = (TextView) view.findViewById(R.id.chatBubbleUser);
            chatBubbleOther = (TextView) view.findViewById(R.id.chatBubbleOther);

        }
    }

    public MessageRecycleAdapter(Context c, ArrayList<MessageDataModel> messageDataModelArrayList, String restaurantId){
        this.messageDataModelArrayList = messageDataModelArrayList;
        this.context = c;
        this.restaurantId = restaurantId;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatbubblelist,parent,false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MessageRecycleAdapter.MyViewHolder holder, final int position) {
       MessageDataModel messageDataModel = messageDataModelArrayList.get(position);
        if (messageDataModel.getUserID().equals(FirebaseAuth.getInstance().getUid())){
            holder.chatBubbleUser.setText(messageDataModel.getMessage());
            holder.chatBubbleOther.setVisibility(View.INVISIBLE);
        }else {
            holder.chatBubbleOther.setText(messageDataModel.getMessage());
            holder.chatBubbleUser.setVisibility(View.INVISIBLE);
        }

        // System.out.println(messageDataModel.getMessage());
     /*  holder.ratingBar.setRating(Float.parseFloat(restaurantUserRating.getUserRating().toString()));
       System.out.println(restaurantUserRating.userReview());
       holder.lblUsername.setText(restaurantUserRating.getUsername());
       holder.lblRatingReview.setText(restaurantUserRating.userReview());*/


    }

    @Override
    public int getItemCount() {
        return messageDataModelArrayList.size();
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