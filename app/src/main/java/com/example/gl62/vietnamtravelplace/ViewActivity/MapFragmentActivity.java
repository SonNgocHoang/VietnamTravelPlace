package com.example.gl62.vietnamtravelplace.ViewActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gl62.vietnamtravelplace.R;
import com.example.gl62.vietnamtravelplace.Retrofit.APIinterface;
import com.example.gl62.vietnamtravelplace.Retrofit.ApiClient;
import com.example.gl62.vietnamtravelplace.Retrofit.ListCategory;
import com.example.gl62.vietnamtravelplace.Retrofit.ListPlace;
import com.example.gl62.vietnamtravelplace.object.Category;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.maps.GoogleMap.*;

public class MapFragmentActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMarkerClickListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker, placeMarker;
    ImageView img_filter_out, img_filter_in;
    RelativeLayout RlFilter;

    List<Place> arlPlace = new ArrayList<>();
    List<Category> arlCategory = new ArrayList<>();
    APIinterface apIinterface = ApiClient.getClient().create(APIinterface.class);
    Call<ListPlace> callP = apIinterface.loadListP();
    Call<ListCategory> callC = apIinterface.loadListC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);
        init();
        setFilter();
        getPlacefromServer();
        getCategoryfromServer();

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        mapFrag.getMapAsync(this);
    }

    public void setMarker(final List<Place> listPlace) {
        for (int i = 0; i < listPlace.size(); i++) {
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


            final int finalI = i;
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

                    tvPlaceName.setText(listPlace.get(finalI).getNameVi());
                    tvDesc.setText(listPlace.get(finalI).getShortDescriptionVi());

                    return null;
                }
            });
            placeMarker = mGoogleMap.addMarker(markerOptions);
            mGoogleMap.setOnMarkerClickListener(this);
        }
    }

    public void init() {
        img_filter_in = (ImageView) findViewById(R.id.img_filter_in);
        img_filter_out = (ImageView) findViewById(R.id.img_filter_out);
        RlFilter = (RelativeLayout) findViewById(R.id.iclFilter);
    }

    public void setFilter() {
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
                .translationX(-25)
                .setDuration(0)
                .start();

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
                        .translationX(-25)
                        .setDuration(500)
                        .start();
            }
        });
    }

    public void getPlacefromServer() {

        callP.enqueue(new Callback<ListPlace>() {
            @Override
            public void onResponse(Call<ListPlace> call, Response<ListPlace> response) {
                if (response.isSuccessful()) {
                    List<Place> listPlace = response.body().getPlaces();
                    setMarker(listPlace);
                    setListPlace(listPlace);
                }
            }

            @Override
            public void onFailure(Call<ListPlace> call, Throwable t) {

            }
        });
    }

    public void getCategoryfromServer() {
        callC.enqueue(new Callback<ListCategory>() {
            @Override
            public void onResponse(Call<ListCategory> call, Response<ListCategory> response) {
                List<Category> listCategory = response.body().getCategories();
                setListCategory(listCategory);
            }

            @Override
            public void onFailure(Call<ListCategory> call, Throwable t) {

            }
        });
    }

    public void setListPlace(List<Place> listPlace) {
        arlPlace = listPlace;
    }

    public void setListCategory(List<Category> listCategory) {
        arlCategory = listCategory;
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
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
    }

    @Override
    public void onLocationChanged(Location location) {
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
//        }
        //optionally, stop location updates if only current location is needed
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

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
        placeMarker = marker;
        placeMarker.showInfoWindow();
        return false;
    }
}
