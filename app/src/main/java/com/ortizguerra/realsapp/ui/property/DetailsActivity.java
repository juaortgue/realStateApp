package com.ortizguerra.realsapp.ui.property;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.model.MyPropertyResponse;
import com.ortizguerra.realsapp.model.PhotoResponse;
import com.ortizguerra.realsapp.model.PhotoUploadResponse;
import com.ortizguerra.realsapp.model.PropertyOneResponse;
import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.model.ResponseContainerOneRow;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PhotoService;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.ui.createproperty.CreatePropertyActivity;
import com.ortizguerra.realsapp.ui.myproperties.OwnPropertiesRecyclerViewAdapter;
import com.ortizguerra.realsapp.util.UtilToken;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback{
    private ImageView imageView, imageViewLeftArrow, imageViewRightArrow, imageViewDeletePhoto;
    private Context ctx;
    public static final int READ_REQUEST_CODE = 42;
    private PropertyResponse propertyResponse;
    private MyPropertyResponse ownProperty;
    private int count=0;
    private String jwt;
    private GoogleMap mMap;
    public Intent intentDates;
    private MapView mapViewDetail;
    private PhotoService servicePhoto;
    private GoogleMap gmap;
    Uri uriSelected;
    private PropertyService propertyService;
    private FloatingActionButton addPhoto;
    private PhotoService photoService;
    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";
    private TextView textViewSize,textViewAddress, textViewCityProvinceZipcode, textViewPrice, textViewRooms, textViewDescription, textViewTitle, textViewCategory;
    private int position;
    private String idProperty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        intentDates = getIntent();
        idProperty=intentDates.getStringExtra("property");




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Cabecera)));
        loadItems();

        count=0;
        loadProperty();


        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapViewDetail = findViewById(R.id.mapViewDetails);
        mapViewDetail.onCreate(mapViewBundle);
        mapViewDetail.getMapAsync(this);



    }
    public PropertyResponse castFromOwnToNormalProperty(MyPropertyResponse ownProperty){
        PropertyResponse p = new PropertyResponse();
        int rooms;
        p.setFav(false);
        p.setPhotos(ownProperty.getPhotos());
        p.setAddress(ownProperty.getAddress());
        p.setCategoryId(ownProperty.getCategoryId());
        p.setCity(ownProperty.getCity());
        p.setDescription(ownProperty.getDescription());
        p.setFavs(null);
        p.setId(ownProperty.getId());
        p.setLoc(ownProperty.getLoc());
        p.setOwnerId(null);
        p.setPrice(ownProperty.getPrice());
        p.setProvince(ownProperty.getProvince());
        p.setRooms(ownProperty.getRooms());
        p.setSize(ownProperty.getSize());
        p.setTitle(ownProperty.getTitle());
        p.setZipcode(ownProperty.getZipcode());


        return p;
    }
    public PropertyResponse castFromOnePropertyToNormalProperty(PropertyOneResponse ownProperty){
        PropertyResponse p = new PropertyResponse();
        int rooms;
        p.setFav(false);
        p.setPhotos(ownProperty.getPhotos());
        p.setAddress(ownProperty.getAddress());
        p.setCategoryId(ownProperty.getCategoryId());
        p.setCity(ownProperty.getCity());
        p.setDescription(ownProperty.getDescription());
        p.setFavs(null);
        p.setId(ownProperty.getId());
        p.setLoc(ownProperty.getLoc());
        p.setOwnerId(null);
        p.setPrice(ownProperty.getPrice());
        p.setProvince(ownProperty.getProvince());
        //rooms=(int) ownProperty.getRooms();
        p.setRooms(ownProperty.getRooms());
        p.setSize(ownProperty.getSize());
        p.setTitle(ownProperty.getTitle());
        p.setZipcode(ownProperty.getZipcode());


        return p;
    }


    public void loadItems() {
        jwt= UtilToken.getToken(this);
        textViewAddress=findViewById(R.id.textViewAddressDetail);
        textViewCityProvinceZipcode=findViewById(R.id.textViewCityZipcodeProvinceDetail);
        textViewPrice=findViewById(R.id.textViewPriceDetail);
        textViewRooms=findViewById(R.id.textViewRoomsDetail);
        textViewDescription=findViewById(R.id.textViewDescriptionDetail);
        textViewTitle=findViewById(R.id.textViewTitleDetail);
        textViewCategory=findViewById(R.id.textViewCategoryDetail);
        textViewCityProvinceZipcode=findViewById(R.id.textViewCityZipcodeProvinceDetail);
        imageView=findViewById(R.id.imageViewPicture);
        imageViewLeftArrow=findViewById(R.id.imageViewLeftArrow);
        imageViewRightArrow=findViewById(R.id.imageViewRightArrow);
        textViewSize=findViewById(R.id.textViewSizeDetail);
        mapViewDetail=findViewById(R.id.mapViewDetails);

        imageViewDeletePhoto=findViewById(R.id.deletePhoto);
        addPhoto=findViewById(R.id.addPhotoDetails);



        ctx=this;

    }
    public void createAndShowDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.deleteDialog)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deletePhoto();
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
    public void deletePhoto() {
        position=0;
        servicePhoto = ServiceGenerator.createService(PhotoService.class);
        Call<ResponseContainer<PhotoResponse>> callList = servicePhoto.getAll();
        callList.enqueue(new Callback<ResponseContainer<PhotoResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PhotoResponse>> call, Response<ResponseContainer<PhotoResponse>> response) {
                position=0;
                if (response.isSuccessful()) {
                    if (propertyResponse.getPhotos().size()>0){
                        for (PhotoResponse photo : response.body().getRows()) {
                            if (photo.getImgurlink().equals(propertyResponse.getPhotos().get(count))) {
                                PhotoService servicePhotoDelete = ServiceGenerator.createService(PhotoService.class, jwt, AuthType.JWT);
                                Call<PhotoResponse> callDelete = servicePhotoDelete.delete(photo.getId());
                                callDelete.enqueue(new Callback<PhotoResponse>() {
                                    @Override
                                    public void onResponse(Call<PhotoResponse> call, Response<PhotoResponse> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(DetailsActivity.this, "Photo deleted", Toast.LENGTH_SHORT).show();

                                        }else {
                                            Toast.makeText(DetailsActivity.this, "no is.Successful DELETE", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PhotoResponse> call, Throwable t) {
                                        Toast.makeText(DetailsActivity.this, "Failure DELETE", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                            position =position+1;
                        }
                    }

                }else {
                    Toast.makeText(DetailsActivity.this, "no is.Successful GET", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PhotoResponse>> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Failure GET", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @SuppressLint("WrongConstant")
    public void setItems(){
        String price = String.valueOf(propertyResponse.getPrice());
        price = price +" €";
        textViewPrice.setText(price);
        String rooms = String.valueOf(propertyResponse.getRooms());
        rooms = rooms+ " rooms ";
        textViewRooms.setText(rooms);
        if (propertyResponse.getAddress()!=null){
            textViewAddress.setText(propertyResponse.getAddress().toString());

        }else{
            textViewAddress.setText("Address not available.");
        }
        textViewCityProvinceZipcode.setText(propertyResponse.getCity()+", "+propertyResponse.getProvince()+" - "+propertyResponse.getZipcode());
        textViewDescription.setText(propertyResponse.getDescription());
        textViewTitle.setText(propertyResponse.getTitle());
        textViewCategory.setText(propertyResponse.getCategoryId().getName());
        String size = String.valueOf(propertyResponse.getSize());
        textViewSize.setText(size+" m^2");
        if (UtilToken.getToken(this)==null){
            addPhoto.setEnabled(false);
            imageViewDeletePhoto.setEnabled(false);
        }else{
            if (propertyResponse.getPhotos().size()>0){
                imageViewDeletePhoto.setEnabled(true);
                imageViewDeletePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createAndShowDeleteDialog();
                    }
                });
            }else{
                imageViewDeletePhoto.setEnabled(false);
            }
        }

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performFileSearch();

            }
        });





    }
    public void performFileSearch() {

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("image/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i("Filechooser URI", "Uri: " + uri.toString());
            }
            uriSelected = uri;
        }

        uploadPhoto();
    }
    public void uploadPhoto() {
        if (uriSelected!=null){
            try {
                InputStream inputStream = getContentResolver().openInputStream(uriSelected);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                int cantBytes;
                byte[] buffer = new byte[1024 * 4];

                while ((cantBytes = bufferedInputStream.read(buffer, 0, 1024 * 4)) != -1) {
                    baos.write(buffer, 0, cantBytes);
                }


                RequestBody requestFile =
                        RequestBody.create(
                                MediaType.parse(getContentResolver().getType(uriSelected)), baos.toByteArray());


                MultipartBody.Part body =
                        MultipartBody.Part.createFormData("photo", "photo", requestFile);

                RequestBody propertyId = RequestBody.create(MultipartBody.FORM, propertyResponse.getId());

                PhotoService servicePhoto = ServiceGenerator.createService(PhotoService.class, jwt, AuthType.JWT);
                Call<PhotoUploadResponse> callPhoto = servicePhoto.upload(body, propertyId);
                callPhoto.enqueue(new Callback<PhotoUploadResponse>() {
                    @Override
                    public void onResponse(Call<PhotoUploadResponse> call, Response<PhotoUploadResponse> response) {
                        int limit=1;
                        if (response.isSuccessful()) {
                            Toast.makeText(ctx, "Picture uploaded", Toast.LENGTH_SHORT).show();
                            Log.d("Uploaded", "Éxito");
                            Log.d("Uploaded", response.body().toString());
                            propertyResponse.getPhotos().add(response.body().getImgurlink());
                            if (propertyResponse.getPhotos().size()==limit){
                                Glide

                                        .with(ctx)
                                        .load(propertyResponse.getPhotos().get(0))
                                        .centerCrop()
                                        .apply(new RequestOptions()
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true))
                                        .into(imageView);
                                imageViewLeftArrow.setEnabled(true);
                                imageViewRightArrow.setEnabled(true);
                                reloadArrows();
                                imageViewDeletePhoto.setEnabled(true);
                                imageViewDeletePhoto.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        createAndShowDeleteDialog();
                                    }
                                });

                            }



                        } else {
                            Toast.makeText(ctx, "Error in request", Toast.LENGTH_SHORT).show();

                            Log.e("Upload error", response.errorBody().toString());
                        }

                    }

                    @Override
                    public void onFailure(Call<PhotoUploadResponse> call, Throwable t) {
                        Log.e("Upload error", t.getMessage());

                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void loadProperty(){
        propertyService= ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainerOneRow<PropertyOneResponse>> call = propertyService.getOne(idProperty);
        call.enqueue(new Callback<ResponseContainerOneRow<PropertyOneResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainerOneRow<PropertyOneResponse>> call, Response<ResponseContainerOneRow<PropertyOneResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.e("error response", "code error");
                    Toast.makeText(ctx, "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("successful response", "code error");
                    propertyResponse = castFromOnePropertyToNormalProperty(response.body().getRows());
                    propertyResponse.setPhotos(response.body().getRows().getPhotos());
                    if (mMap!=null){
                        LatLng position = obtainLatLong();
                        mMap.addMarker(new MarkerOptions()
                                .position(position)
                                .title(propertyResponse.getAddress())
                                .snippet("com.ortizguerra.realsapp")
                                .draggable(true)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
                        );
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));


                    }
                    setItems();
                    if (propertyResponse.getPhotos().size()!=0){

                        Glide

                                .with(ctx)
                                .load(propertyResponse.getPhotos().get(0))
                                .centerCrop()
                                .apply(new RequestOptions()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true))
                                .into(imageView);
                    }else{

                        Glide
                                .with(ctx)

                                .load("https://www.riverside.org.uk/wp-content/themes/rd-riverside/grunticon/png/no-images-placeholder.png")
                                .centerCrop()
                                .into(imageView);



                    }

                    if (propertyResponse.getPhotos().size()==0){
                        imageViewLeftArrow.setEnabled(false);
                        imageViewRightArrow.setEnabled(false);

                    }else{
                        reloadArrows();

                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseContainerOneRow<PropertyOneResponse>> call, Throwable t) {
                Log.e("failure", "failure in petition");

            }
        });
    }
    public LatLng obtainLatLong(){
        String loc =propertyResponse.getLoc();
        String[] locs =loc.split(",");
        locs[0].trim();
        locs[1].trim();
        float latitud = Float.parseFloat(locs[0]);
        float longitud = Float.parseFloat(locs[1]);

        LatLng position = new LatLng(latitud, longitud);
        return position;
    }
    public void changePictureRight(){

        ctx=this;
        if (count>=propertyResponse.getPhotos().size()-1){
            count=0;
        }else{
            count++;
        }
        Glide
                .with(ctx)
                .load(propertyResponse.getPhotos().get(count))
                .centerCrop()
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(imageView);



    }
    public void changePictureLeft(){
        ctx=this;
        count--;
        if (count<0){
            count=propertyResponse.getPhotos().size()-1;
        }
        Glide
                .with(ctx)
                .load(propertyResponse.getPhotos().get(count))
                .centerCrop()
                .apply(new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true))
                .into(imageView);

    }
    public void reloadArrows(){
        imageViewRightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePictureRight();


            }


        });
        imageViewLeftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePictureLeft();

            }


        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        gmap = googleMap;
        gmap.setMinZoomPreference(10);
        mMap=googleMap;
        if (propertyResponse!=null){




            LatLng position =obtainLatLong();
            googleMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(propertyResponse.getAddress())
                    .snippet("com.ortizguerra.realsapp")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location))
            );
            gmap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapViewDetail.onSaveInstanceState(mapViewBundle);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapViewDetail.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapViewDetail.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapViewDetail.onStop();
    }
    @Override
    protected void onPause() {
        mapViewDetail.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapViewDetail.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapViewDetail.onLowMemory();
    }

}
