package com.ortizguerra.realsapp.ui.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ortizguerra.realsapp.R;

import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.ui.createproperty.CreatePropertyActivity;
import com.ortizguerra.realsapp.ui.property.DetailsActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TODO = "";
    private GoogleMap mMap;
    private PropertyService service;
    private FusedLocationProviderClient fusedLocationClient;
    private Marker myMarker;
    private Map options = new HashMap();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Intent i = getIntent();
        options = (Map) i.getSerializableExtra("options");

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        service = ServiceGenerator.createService(PropertyService.class);
        String a=getCurrentLocation();
        Call<ResponseContainer<PropertyResponse>> callGeo = service.listGeo(options);
        callGeo.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (response.isSuccessful()){
                    for(PropertyResponse prop : response.body().getRows()){
                        System.out.println(prop.getLoc());
                        if(prop.getLoc() == null){


                            String lat = "37.3803677";
                            String lon = "-6.0071807999999995";
                            System.out.println(lat);
                            System.out.println(lon);
                            LatLng loc = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                            mMap.addMarker(new MarkerOptions().position(loc).title("Marker in "+prop.getAddress()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                        }else {
                            String[] parts = prop.getLoc().split(",");
                            if (parts.length == 1) {
                                String lat = "37.3803677";
                                String lon = "-6.0071807999999995";
                                LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in " + prop.getAddress()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                            } else {
                                String lat = parts[0];
                                String lon = parts[1];
                                System.out.println(lat);
                                System.out.println(lon);
                                LatLng loc = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

                                mMap.addMarker(new MarkerOptions().position(loc).title("Marker in " + prop.getAddress())).setTag(prop.getId());

                                mMap.setOnMarkerClickListener(marker -> {
                                    System.out.println("AQUIIIIIIIIIIIIIII");
                                    Intent details = new Intent(getApplicationContext(), DetailsActivity.class);
                                    details.putExtra("property",Objects.requireNonNull(marker.getTag()).toString());
                                    startActivity(details);
                                    return false;
                                });
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));



                            }
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                Log.e("error", "error in map petition near");

            }
        });



    }

    public String getCurrentLocation() {
        final String[] currentLoc = new String[1];
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            String lat = "37.3803677";
            String lon = "-6.0071807999999995";
            currentLoc[0] = lon+ "," + lat;
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.ubication)
                    .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {

                            currentLoc[0] = location.getLongitude() + "," + location.getLatitude();
                        }
                    }
                });

        return currentLoc[0];
    }
}
