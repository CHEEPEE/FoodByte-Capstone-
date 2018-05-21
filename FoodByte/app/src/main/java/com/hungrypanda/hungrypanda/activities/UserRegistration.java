package com.hungrypanda.hungrypanda.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.hungrypanda.hungrypanda.AppModules.GlideApp;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.mapModels.ChatUserListMapModel;
import com.hungrypanda.hungrypanda.mapModels.MessageMapmodel;
import com.hungrypanda.hungrypanda.mapModels.UserProfileMapModel;
import com.hungrypanda.hungrypanda.utils.Utils;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRegistration extends AppCompatActivity {
    TextView username;
    FirebaseAuth mAuth;
    FirebaseUser user;
    CircleImageView userProfileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        userProfileIcon = (CircleImageView) findViewById(R.id.userIcon);
        username = (TextView) findViewById(R.id.userName);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        username.setText(user.getDisplayName());
        GlideApp.with(UserRegistration.this).load(user.getPhotoUrl()).placeholder(R.drawable.image_placeholder).centerCrop().into(userProfileIcon);
        saveProfile(user.getUid(),user.getDisplayName(),user.getPhoneNumber(),user.getPhotoUrl(),user.getEmail());

    }


    private void saveProfile(String userId, String userDisplayName, String userNumber, Uri userPhoto, String userEmail){

        UserProfileMapModel userProfileMapModel = new UserProfileMapModel(userId,userEmail,userNumber,userDisplayName,userPhoto.toString());
        Map<String,Object> value = userProfileMapModel.toMap();
        Map<String,Object> childUpdates = new HashMap<>();
        childUpdates.put(userId,value);
        FirebaseDatabase.getInstance().getReference().child(Utils.customersProfile).updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent i = new Intent(UserRegistration.this,RestuarantAndProductActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private boolean validate(){
        return true;
    }
}
