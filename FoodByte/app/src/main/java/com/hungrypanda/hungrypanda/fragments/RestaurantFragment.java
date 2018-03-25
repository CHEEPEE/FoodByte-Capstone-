package com.hungrypanda.hungrypanda.fragments;


import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungrypanda.hungrypanda.R;
import com.hungrypanda.hungrypanda.activities.RestuarantAndProductActivity;
import com.hungrypanda.hungrypanda.datamodels.StoreProfileModel;
import com.hungrypanda.hungrypanda.datamodels.StoreProfileModelwithLocation;
import com.hungrypanda.hungrypanda.mapModels.DeviceCurrentLocationMap;
import com.hungrypanda.hungrypanda.mapModels.RestaurantLocationMapModel;
import com.hungrypanda.hungrypanda.mapModels.StoreProfileInformationMap;
import com.hungrypanda.hungrypanda.mapModels.StoreProfileInformationMapWithLocation;
import com.hungrypanda.hungrypanda.recyclerviewAdapters.RecycleStoreProfilesAdapter;
import com.hungrypanda.hungrypanda.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * Created by Keji's Lab on 26/12/2017.
 */

public class RestaurantFragment extends Fragment {
    TextView lblRestaurantName,lblRestaurantAddress;
    ArrayList<StoreProfileModel> storeProfileModels = new ArrayList<>();
    public ArrayList<StoreProfileModelwithLocation> storeProfileModelwithLocationArrayList = new ArrayList<>();
    RecyclerView rvRestaurantList;
    DatabaseReference mDatabase;
    RecycleStoreProfilesAdapter recycleStoreProfilesAdapter;
    RecyclerView.LayoutManager layoutManager;
    Context context;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 14;
    private boolean mLocationPermissionGranted = false;
    private GeoDataClient mGeoDataClient;
    private Location mLastKnownLocation = new Location("");
    public PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_restaurant, container, false);
        lblRestaurantName = (TextView)rootView.findViewById(R.id.lblRestaurantName);
        RestuarantAndProductActivity restuarantAndProductActivity = (RestuarantAndProductActivity) getActivity() ;
        lblRestaurantAddress = (TextView) rootView.findViewById(R.id.lblRestaurantLocation);
        rvRestaurantList = (RecyclerView) rootView.findViewById(R.id.rvRestuarantsList);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        context = getActivity();
        getLocation();



        layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        rvRestaurantList.setLayoutManager(layoutManager);
        recycleStoreProfilesAdapter = new RecycleStoreProfilesAdapter(getContext(),storeProfileModelwithLocationArrayList,mLastKnownLocation);
        rvRestaurantList.setAdapter(recycleStoreProfilesAdapter);
        recycleStoreProfilesAdapter.notifyDataSetChanged();
        storeProfileModelwithLocationArrayList.clear();
        recycleStoreProfilesAdapter.notifyDataSetChanged();

        mGeoDataClient = Places.getGeoDataClient(getContext(), null);


        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        mDatabase.child(Utils.storeProfiles).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                storeProfileModels.clear();
                storeProfileModelwithLocationArrayList.clear();
                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    StoreProfileModel storeProfileModel = new StoreProfileModel();
                    StoreProfileInformationMap storeProfileInformationMap = dataSnapshot1.getValue(StoreProfileInformationMap.class);
                    storeProfileModel.setStoreName(storeProfileInformationMap.storeName);
                    storeProfileModel.setStoreBannerUrl(storeProfileInformationMap.storeBannerUrl);
                    storeProfileModel.setStoreProfileUrl(storeProfileInformationMap.storeProfileUrl);
                    storeProfileModel.setStoreAddress(storeProfileInformationMap.storeAddress);
                    storeProfileModel.setStoreContact(storeProfileInformationMap.storeContact);
                    storeProfileModel.setRestaurantID(storeProfileInformationMap.restaurantID);
                    storeProfileModels.add(storeProfileModel);
                    recycleStoreProfilesAdapter.notifyDataSetChanged();

                }
                mDatabase.child(Utils.storeProfiles).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        SmartLocation.with(context).location().start(new OnLocationUpdatedListener() {
                            @Override
                            public void onLocationUpdated(final Location location) {
                                storeProfileModelwithLocationArrayList.clear();
                                for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    final StoreProfileInformationMap storeProfileInformationMap = dataSnapshot1.getValue(StoreProfileInformationMap.class);
                                    FirebaseDatabase.getInstance().getReference().child(Utils.restaurantLocation).child(storeProfileInformationMap.restaurantID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            storeProfileModelwithLocationArrayList.remove(storeProfileInformationMap);
                                            RestaurantLocationMapModel restaurantLocationMapModel = dataSnapshot.getValue(RestaurantLocationMapModel.class);
                                            Location storeLocation = new Location("");
                                            storeLocation.setLatitude(restaurantLocationMapModel.locationLatitude);
                                            storeLocation.setLongitude(restaurantLocationMapModel.locationLongitude);
                                            StoreProfileModelwithLocation storeProfileModelwithLocation = new StoreProfileModelwithLocation();
                                            storeProfileModelwithLocation.setStoreName(storeProfileInformationMap.storeName);
                                            storeProfileModelwithLocation.setStoreBannerUrl(storeProfileInformationMap.storeBannerUrl);
                                            storeProfileModelwithLocation.setStoreProfileUrl(storeProfileInformationMap.storeProfileUrl);
                                            storeProfileModelwithLocation.setStoreAddress(storeProfileInformationMap.storeAddress);
                                            storeProfileModelwithLocation.setStoreContact(storeProfileInformationMap.storeContact);
                                            storeProfileModelwithLocation.setRestaurantID(storeProfileInformationMap.restaurantID);
                                            storeProfileModelwithLocation.setLocationRange(location.distanceTo(storeLocation));
                                            storeProfileModelwithLocationArrayList.add(storeProfileModelwithLocation);


                                            Collections.sort(storeProfileModelwithLocationArrayList, new Comparator<StoreProfileModelwithLocation>() {
                                                @Override
                                                public int compare(StoreProfileModelwithLocation storeProfileModelwithLocation, StoreProfileModelwithLocation t1) {

                                                    return t1.getLocationRange().compareTo(storeProfileModelwithLocation.getLocationRange());

                                                }
                                            });
                                            Collections.reverse(storeProfileModelwithLocationArrayList);

                                            Object[] st = storeProfileModelwithLocationArrayList.toArray();
                                            for (Object s : st) {
                                                if (storeProfileModelwithLocationArrayList.indexOf(s) != storeProfileModelwithLocationArrayList.lastIndexOf(s)) {
                                                    storeProfileModelwithLocationArrayList.remove(storeProfileModelwithLocationArrayList.lastIndexOf(s));
                                                }
                                            }
                                            recycleStoreProfilesAdapter.notifyDataSetChanged();


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });

                                }

                                ;
                            }

                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Object[] st = storeProfileModelwithLocationArrayList.toArray();
        for (Object s : st) {
            if (storeProfileModelwithLocationArrayList.indexOf(s) != storeProfileModelwithLocationArrayList.lastIndexOf(s)) {
                storeProfileModelwithLocationArrayList.remove(storeProfileModelwithLocationArrayList.lastIndexOf(s));
            }
        }
        recycleStoreProfilesAdapter.notifyDataSetChanged();

        return rootView;
    }

    private void getLocation(){

        SmartLocation.with(context).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
              mLastKnownLocation.setLatitude(location.getLatitude());
              mLastKnownLocation.setLongitude(location.getLongitude());
            }
        });


    }

}
