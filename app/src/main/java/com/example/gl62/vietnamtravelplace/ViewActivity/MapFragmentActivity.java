package com.example.gl62.vietnamtravelplace.ViewActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gl62.vietnamtravelplace.R;
import com.example.gl62.vietnamtravelplace.Retrofit.APIinterface;
import com.example.gl62.vietnamtravelplace.Retrofit.ApiClient;
import com.example.gl62.vietnamtravelplace.Retrofit.ListCategoryAPI;
import com.example.gl62.vietnamtravelplace.Retrofit.ListPlaceAPI;
import com.example.gl62.vietnamtravelplace.object.Category;
import com.example.gl62.vietnamtravelplace.object.Cover;
import com.example.gl62.vietnamtravelplace.object.Place;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.GoogleMap.*;

public class MapFragmentActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener, OnInfoWindowClickListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker, placeMarker;

    ImageView img_filter_out, img_filter_in, img_scene, img_pagoda, img_atm, img_restaurant, img_fuel;
    RelativeLayout RlFilter;

    List<Place> arlPlace = new ArrayList<>();
    HashMap<String, Place> hmExtraDataMarker = new HashMap<>();
    HashMap<String, Marker> hmMarker = new HashMap<>();

    //For fuel because they doesn't have Category
    List<Place> arlFuel = new ArrayList<>();

    //this for filter
    List<ImageView> lFItemMenu = new ArrayList<>();
    List<Category> arlCategory = new ArrayList<>();

    //init retrofit api
    APIinterface apIinterface = ApiClient.getClient().create(APIinterface.class);
    Call<ListPlaceAPI> callP = apIinterface.loadListP();
    Call<ListCategoryAPI> callC = apIinterface.loadListC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);
        checkInternetConnection();
        init();
        getPlacefromServer();
        getCategoryfromServer();
        setUpFilter();

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);

        actionMenuFilter();

    }

    public void setMarker(List<Place> listPlace) {

        for (int i = 0; i < listPlace.size(); i++) {
            //get data of place
            LatLng latLng = new LatLng(Double.valueOf(listPlace.get(i).getLatitude()), Double.valueOf(listPlace.get(i).getLongitude()));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title(listPlace.get(i).getNameVi());
            markerOptions.position(latLng);
            switch (listPlace.get(i).getCategoryId()) {
                case 0:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category0));
                    break;
                case 1:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category1));
                    break;
                case 2:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category2));
                    break;
                case 3:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category3));
                    break;
                case 4:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category4));
                    break;
                case 5:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category5));
                    break;
                case 6:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category6));
                    break;
                case 7:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category7));
                    break;
                case 8:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category8));
                    break;
                case 9:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category9));
                    break;
                case 10:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category10));
                    break;
                case 11:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category11));
                    break;
                case 12:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category12));
                    break;
                case 13:
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.category13));
                    break;
                default:
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                    break;
            }
            placeMarker = mGoogleMap.addMarker(markerOptions);


            //put map extra data for marker
            hmExtraDataMarker.put(placeMarker.getId(), listPlace.get(i));
            hmMarker.put(listPlace.get(i).getId().toString(), placeMarker);
        }
        //set Listener for marker and inforwindow
        mGoogleMap.setOnMarkerClickListener(this);
        mGoogleMap.setOnInfoWindowClickListener(this);
    }

    public void init() {
        img_filter_in = (ImageView) findViewById(R.id.img_filter_in);
        img_filter_out = (ImageView) findViewById(R.id.img_filter_out);
        img_scene = (ImageView) findViewById(R.id.img_scene);
        img_atm = (ImageView) findViewById(R.id.img_atm);
        img_pagoda = (ImageView) findViewById(R.id.img_pagoda);
        img_fuel = (ImageView) findViewById(R.id.img_fuel);
        img_restaurant = (ImageView) findViewById(R.id.img_restaurant);
        RlFilter = (RelativeLayout) findViewById(R.id.iclFilter);

        lFItemMenu.add(img_scene);
        lFItemMenu.add(img_pagoda);
        lFItemMenu.add(img_restaurant);
        lFItemMenu.add(img_atm);
        lFItemMenu.add(img_fuel);

    }

    public void setUpFilter() {
        //hide from beginning
        img_filter_in.setVisibility(View.GONE);
        RlFilter.setVisibility(View.GONE);
        img_filter_out.setVisibility(View.VISIBLE);

        img_filter_in.animate()
                .scaleX(0)
                .scaleY(0)
                .start();

        RlFilter.animate()
                .translationX(-80)
                .setDuration(0)
                .start();

        img_filter_out.animate()
                .translationX(-32)
                .setDuration(0)
                .start();
        //show filter when click out btn
        img_filter_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_filter_out.setVisibility(View.GONE);
                RlFilter.setVisibility(View.VISIBLE);

                RlFilter.animate()
                        .translationX(0)
                        .setDuration(200)
                        .start();
                img_filter_in.setVisibility(View.VISIBLE);
                img_filter_in.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(200)
                        .start();
            }
        });
        //hide filter again
        img_filter_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_filter_in.setVisibility(View.GONE);
                RlFilter.setVisibility(View.GONE);
                img_filter_out.setVisibility(View.VISIBLE);

                img_filter_in.animate()
                        .scaleX(0)
                        .scaleY(0)
                        .setDuration(0)
                        .start();

                RlFilter.animate()
                        .translationX(-80)
                        .setDuration(200)
                        .start();

                img_filter_out.animate()
                        .translationX(-32)
                        .setDuration(500)
                        .start();
            }
        });
    }

    public void getPlacefromServer() {

        callP.enqueue(new Callback<ListPlaceAPI>() {
            @Override
            public void onResponse(Call<ListPlaceAPI> call, Response<ListPlaceAPI> response) {
                if (response.isSuccessful()) {
                    List<Place> listPlace = response.body().getPlaces();
                    setListPlace(listPlace);
                }
            }

            @Override
            public void onFailure(Call<ListPlaceAPI> call, Throwable t) {

            }
        });
    }

    public void getCategoryfromServer() {
        callC.enqueue(new Callback<ListCategoryAPI>() {
            @Override
            public void onResponse(Call<ListCategoryAPI> call, Response<ListCategoryAPI> response) {
                List<Category> listCategory = response.body().getCategories();
                setListCategory(listCategory);
            }

            @Override
            public void onFailure(Call<ListCategoryAPI> call, Throwable t) {

            }
        });
        Log.d("Activity", "getCategoryfromServer: ");
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapFragmentActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    public void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        boolean isConnected = networkInfo != null
                && networkInfo.isConnectedOrConnecting();
        if (!isConnected) {
            final View view = findViewById(R.id.clSnackbar);
            Snackbar snackbar = Snackbar
                    .make(view, "Internet Connection Failed", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public void setListCategory(List<Category> listCategory) {
        //set up list Category
        arlCategory = listCategory;
    }

    public void setListPlace(List<Place> listPlace) {
        //set up list Place
        arlPlace = listPlace;
        //set up all marker
        setMarker(arlPlace);
        List<Place> lPlace = new ArrayList<>();
        for (int i = 0; i < arlCategory.size(); i++) {
            for (int j = 0; j < arlPlace.size(); j++) {
                if (arlPlace.get(j).getCategoryId() == arlCategory.get(i).getId()) {
                    lPlace.add(arlPlace.get(j));
                }
            }
            //set all place.get(i).getCategory(i) contains with categoryid
            arlCategory.get(i).setlPlace(lPlace);
            //a new listPlace?
            lPlace = new ArrayList<>();
        }
        for (int i = 0; i <arlPlace.size() ; i++) {
            if (arlPlace.get(i).getCategoryId()==0) {
                arlFuel.add(arlPlace.get(i));
            }
        }
        Log.d("ACTIVITY", "setListPlace: "+arlFuel.size());
    }

    public void actionMenuFilter(){
        //set Filter Menu Action
        img_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterItemSelected(0);
            }
        });
        img_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterItemSelected(1);
            }
        });
        img_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterItemSelected(2);
            }
        });
        img_atm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterItemSelected(3);
            }
        });
        img_fuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFilterItemSelected(4);
            }
        });
    }

    public void onFilterItemSelected(int j) {
        for (int i = 0; i < arlCategory.size(); i++) {
            //get parent menu Sence
            if (j == 0 && (arlCategory.get(i).getId() == 2
                    || arlCategory.get(i).getId() == 4
                    || arlCategory.get(i).getId() == 5
                    || arlCategory.get(i).getId() == 8
                    || arlCategory.get(i).getId() == 10
                    || arlCategory.get(i).getId() == 12
                    || arlCategory.get(i).getId() == 13)) {
                List<Place> place = arlCategory.get(i).getlPlace();
                for (int k = 0; k < place.size(); k++) {
                    Marker marker = hmMarker.get(place.get(k).getId().toString());
                    if (marker.isVisible()) {
                        marker.setVisible(false);
                        img_scene.setAlpha(0.3f);
                    } else {
                        marker.setVisible(true);
                        img_scene.setAlpha(1f);
                    }
                }
            }
            //get parent menu Relics
            if (j == 1 && (arlCategory.get(i).getId() == 1
                    || arlCategory.get(i).getId() == 6
                    || arlCategory.get(i).getId() == 7
                    || arlCategory.get(i).getId() == 1)) {
                List<Place> place = arlCategory.get(i).getlPlace();
                for (int k = 0; k < place.size(); k++) {
                    Marker marker = hmMarker.get(place.get(k).getId().toString());
                    if (marker.isVisible()) {
                        marker.setVisible(false);
                        img_pagoda.setAlpha(0.3f);
                    } else {
                        marker.setVisible(true);
                        img_pagoda.setAlpha(1f);
                    }
                }
            }
            //get parent menu restaurant
            if (j == 2 && (arlCategory.get(i).getId() == 3
                    || arlCategory.get(i).getId() == 9)) {
                List<Place> place = arlCategory.get(i).getlPlace();
                for (int k = 0; k < place.size(); k++) {
                    Marker marker = hmMarker.get(place.get(k).getId().toString());
                    if (marker.isVisible()) {
                        marker.setVisible(false);
                        img_restaurant.setAlpha(0.3f);
                    } else {
                        marker.setVisible(true);
                        img_restaurant.setAlpha(1f);
                    }
                }
            }
            //get parent menu Atm
            if (j == 3) {
                final View v = findViewById(R.id.clSnackbar);
                Snackbar snackbar = Snackbar
                        .make(v, "Has no parent place", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
            //get parent menu Fuel
            if (j == 4) {
                for (int k = 0; k < arlFuel.size(); k++) {
                    Marker marker = hmMarker.get(arlFuel.get(k).getId().toString());
                    if (marker.isVisible()) {
                        marker.setVisible(false);
                        img_fuel.setAlpha(0.3f);
                    } else {
                        marker.setVisible(true);
                        img_fuel.setAlpha(1f);
                    }
                }
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(MAP_TYPE_HYBRID);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                mGoogleMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        //show snack when map load failed
        final View view = findViewById(R.id.clSnackbar);
        Snackbar snackbar = Snackbar
                .make(view, "Loadding Map Failed", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        //set last location
        mLastLocation = location;

        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));

        //optionally, stop location updates if only current location is needed
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.getTitle().equals("Current Position")) {
            //get Place data from hashmap
            final Place place = hmExtraDataMarker.get(marker.getId());
            List<Cover> arlCover = place.getCover();
            String url = "";
            for (int i = 0; i < arlCover.size(); i++) {
                url = arlCover.get(i).getUrl();
            }
            //Custom Adapter infowindow
            final String finalUrl = url;
            mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.information_window, null);
                    TextView tvPlaceName = (TextView) v.findViewById(R.id.tvPlaceName);
                    TextView tvDesc = (TextView) v.findViewById(R.id.tvDesc);
                    ImageView imgCover = (ImageView)v.findViewById(R.id.img_cover);

                    tvPlaceName.setText(place.getNameVi());
                    tvDesc.setText(place.getAddressVi());
                    Picasso.with(getBaseContext()).load("http://bwhere.vn/uploads/small/"+ finalUrl)
                            .placeholder(R.mipmap.ic_loading)
                            .into(imgCover);

                    return v;
                }
            });
            if (marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
        } else {
            mGoogleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.information_window, null);
                    TextView tvPlaceName = (TextView) v.findViewById(R.id.tvPlaceName);

                    tvPlaceName.setText("Your Current Location");
                    return v;
                }
            });
        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getBaseContext(),InforPlace.class);
        Place place = hmExtraDataMarker.get(marker.getId());
        intent.putExtra("extra_data_place",place);
        startActivity(intent);
    }
}
