package com.hungrypanda.hungrypanda.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.util.Util;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.datamodels.MessageDataModel;
import com.hungrypanda.hungrypanda.mapModels.ChatUserListMapModel;
import com.hungrypanda.hungrypanda.mapModels.MessageMapmodel;
import com.hungrypanda.hungrypanda.recyclerviewAdapters.MessageRecycleAdapter;
import com.hungrypanda.hungrypanda.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    String restaurantId;
    DatabaseReference mDataBase;
    FirebaseAuth mAuth;
    RecyclerView chatRecyclerView;
    MessageRecycleAdapter messageRecycleAdapter;
    Button btnSubmit;
    EditText inputMsg;
    ArrayList<MessageDataModel> messageDataModelsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        restaurantId  = getIntent().getExtras().getString("restoID");
        mDataBase = FirebaseDatabase.getInstance().getReference();
        System.out.println(restaurantId);
        chatRecyclerView = (RecyclerView) findViewById(R.id.chatListRecycleView);
        mAuth = FirebaseAuth.getInstance();

        btnSubmit = (Button) findViewById(R.id.btnSend);
        inputMsg = (EditText) findViewById(R.id.inputmsg);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = mDataBase.push().getKey();

                MessageMapmodel messageMapmodel = new MessageMapmodel(key
                        ,inputMsg.getText().toString(),Utils.getDateToStrig(),mAuth.getCurrentUser().getDisplayName(),mAuth.getUid());
                Map<String,Object> chatMsgValue = messageMapmodel.toMap();
                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put(key,chatMsgValue);
                mDataBase.child(Utils.chattingSystem).child(Utils.chatsList).child(restaurantId).child(mAuth.getUid()).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        inputMsg.setText("");
                        ChatUserListMapModel chatUserListMapModel = new ChatUserListMapModel(mDataBase.push().getKey(),mAuth.getUid(),mAuth.getCurrentUser().getDisplayName(),Utils.getDateToStrig(),mAuth.getCurrentUser().getPhotoUrl().toString());
                        Map<String,Object> userlistValue = chatUserListMapModel.toMap();
                        Map<String,Object> childUpdates = new HashMap<>();
                        childUpdates.put(mAuth.getUid(),userlistValue);
                        mDataBase.child(Utils.chattingSystem).child(Utils.chatUserList).child(restaurantId).updateChildren(childUpdates);
                    }
                });

            }
        });

        mDataBase.child(Utils.chattingSystem).child(Utils.chatsList).child(restaurantId).child(mAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageDataModel messageDataModel = new MessageDataModel();
                MessageMapmodel messageMapmodel = dataSnapshot.getValue(MessageMapmodel.class);
                messageDataModel.setMessage(messageMapmodel.message);
                messageDataModel.setUserID(messageMapmodel.userID);


                messageDataModelsArrayList.add(messageDataModel);
                messageRecycleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageRecycleAdapter = new MessageRecycleAdapter(ChatActivity.this,messageDataModelsArrayList,restaurantId);
        chatRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        chatRecyclerView.setAdapter(messageRecycleAdapter);
        messageRecycleAdapter.notifyDataSetChanged();




    }
}
