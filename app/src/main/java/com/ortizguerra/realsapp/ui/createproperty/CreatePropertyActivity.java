package com.ortizguerra.realsapp.ui.createproperty;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.dto.CreatePropertyDto;
import com.ortizguerra.realsapp.model.CategoryResponse;
import com.ortizguerra.realsapp.model.CreatePropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.model.UserResponse;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.CategoryService;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.ui.common.DashboardActivity;
import com.ortizguerra.realsapp.util.UtilToken;
import com.ortizguerra.realsapp.util.Validator;
import com.ortizguerra.realsapp.util.data.GeographySpain;
import com.ortizguerra.realsapp.util.geography.Geocode;
import com.ortizguerra.realsapp.util.geography.GeographyListener;
import com.ortizguerra.realsapp.util.geography.GeographySelector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Integer.parseInt;

public class CreatePropertyActivity extends AppCompatActivity implements View.OnClickListener, GeographyListener {
    public static final int READ_REQUEST_CODE = 42;
    private EditText title, description, price, size, zipcode, address, editTextRooms;
    private String fullAddress, jwt, loc;
    private TextView tvRegion;
    private TextView tvProvincia;
    private TextView tvMunicipio;
    private FragmentTransaction fragmentChanger;

    PropertyService service;
    Uri uriSelected;
    UserResponse me;
    private Button btProbar, btnAdd;
    private Spinner categories;
    private List<CategoryResponse> listCategories = new ArrayList<>();
    private FloatingActionButton addPhoto;
    CreatePropertyDto responseP;
    private String idUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_property_activity);
        jwt = UtilToken.getToken(this);
        idUsuario=UtilToken.getId(this);
        loadItems();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btProbar) {
            GeographySelector gs = new GeographySelector(CreatePropertyActivity.this);
            gs.setOnGeograpySelectedListener(CreatePropertyActivity.this);
            FragmentManager fm = getSupportFragmentManager();
            gs.show(fm, "geographySelector");
        }
    }
    public void loadItems(){
        uriSelected = null;
        addPhoto = findViewById(R.id.addPhoto);


        btnAdd = findViewById(R.id.add_property);
        btProbar = (Button) findViewById(R.id.btProbar);
        btProbar.setOnClickListener(this);

        title = findViewById(R.id.title_edit);
        description = findViewById(R.id.description_edit);
        price = findViewById(R.id.price_edit);
        size = findViewById(R.id.size_edit);
        address = findViewById(R.id.address_edit);
        zipcode = findViewById(R.id.zipcode_edit);
        editTextRooms = findViewById(R.id.editTextRooms);
        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvProvincia = (TextView) findViewById(R.id.tvProvincia);
        tvMunicipio = (TextView) findViewById(R.id.tvMunicipio);

        categories = findViewById(R.id.spinner_category);
        loadAllCategories();
        /*addPhoto.setOnClickListener(v -> {
            performFileSearch();
        });*/

        btnAdd.setOnClickListener(v -> {
            if (validate()){
                makeProperty();
            }

/*            new android.os.Handler().postDelayed(
                    () -> Log.i("tag", "This'll run 800 milliseconds later"),
                    800);*/

        });
    }
    @Override
    public void onGeographySelected(Map<String, String> hm) {
        tvRegion.setText(hm.get(GeographySpain.REGION));
        tvProvincia.setText(hm.get(GeographySpain.PROVINCIA));
        tvMunicipio.setText(hm.get(GeographySpain.MUNICIPIO));
    }
    public void loadAllCategories() {
        CategoryService serviceC = ServiceGenerator.createService(CategoryService.class);
        Call<ResponseContainer<CategoryResponse>> callC = serviceC.listCategories();

        callC.enqueue(new Callback<ResponseContainer<CategoryResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<CategoryResponse>> call, Response<ResponseContainer<CategoryResponse>> response) {
                if (response.isSuccessful()) {
                    int spinnerPosition = 1;
                    Log.d("successCategory", "Got category");
                    listCategories = response.body().getRows();
                    System.out.println(listCategories);
                    List<String> namesC = new ArrayList<>();

                 /*   for (CategoryResponse category : listCategories) {
                        namesC.add(category.getName());
                    }*/
                    ArrayAdapter<CategoryResponse> adapter =
                            new ArrayAdapter<>(CreatePropertyActivity.this, android.R.layout.simple_spinner_dropdown_item, listCategories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories.setAdapter(adapter);
                    categories.setSelection(listCategories.size() - 1);
                } else {
                    Toast.makeText(CreatePropertyActivity.this, "Error loading categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<CategoryResponse>> call, Throwable t) {
                Toast.makeText(CreatePropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void makeProperty() {
        fullAddress = "Calle " + address.getText().toString() + ", " + zipcode.getText().toString() + " " + " " + tvProvincia.getText().toString() + ", España";
        try {
            loc = getLoc(fullAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        CreatePropertyDto create = new CreatePropertyDto();
        CategoryResponse chosen = (CategoryResponse) categories.getSelectedItem();
        create.setTitle(title.getText().toString());
        create.setDescription(description.getText().toString());
        create.setAddress(address.getText().toString());
        create.setZipcode(zipcode.getText().toString());
        create.setCity(tvMunicipio.getText().toString());
        create.setPrice(Long.parseLong(price.getText().toString()));
        create.setSize(Long.parseLong(size.getText().toString()));
        create.setProvince(tvProvincia.getText().toString());
        //create.setOwnerId(idUsuario);
        create.setCategoryId(chosen.getId());
        create.setLoc(loc);
        String rooms="";
        rooms=editTextRooms.getText().toString();

        create.setRooms(parseInt(rooms));

        addProperty(create);

    }
    public String getLoc(String fullAddress) throws IOException {
        String loc = Geocode.getLatLong(CreatePropertyActivity.this, fullAddress);
        return loc;
    }
    public void addProperty(CreatePropertyDto create) {
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);


        Call<CreatePropertyResponse> call = service.create(create);
        call.enqueue(new Callback<CreatePropertyResponse>() {
            @Override
            public void onResponse(Call<CreatePropertyResponse> call, Response<CreatePropertyResponse> response) {
                //uploadPhoto(response.body());
                if (response.isSuccessful()) {
                    //uploadPhoto(response.body());
                    //tratamiento de imágenes aquí, coger el id de la response y
                    //añadírsela a las imágenes subidas

                    System.out.println(response.body());

                    Toast.makeText(CreatePropertyActivity.this, "Created", Toast.LENGTH_SHORT).show();
                    Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(dashboard);
                } else {
                    Toast.makeText(CreatePropertyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CreatePropertyResponse> call, Throwable t) {
                Toast.makeText(CreatePropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean validate(){
        Validator.clearError(title);
        Validator.clearError(description);
        Validator.clearError(price);
        Validator.clearError(address);
        Validator.clearError(zipcode);
        Validator.clearError(size);
        Validator.clearError(editTextRooms);
        Validator.clearError(tvRegion);
        Validator.clearError(tvProvincia);
        Validator.clearError(tvMunicipio);



        String empty="It is empty", onlyLetters="Only letters.";
        boolean isCorrect=true;
        if (!Validator.isNotEmpty(title)){
            Validator.setError(title, empty);
            isCorrect=false;

        }else if(!Validator.onlyLetters(title)){
            Validator.setError(title, onlyLetters);
            isCorrect=false;

        }
        if (!Validator.isNotEmpty(address)){
            Validator.setError(address, empty);
            isCorrect=false;


        }
        if (!Validator.isNotEmpty(description)){
            Validator.setError(description, empty);
            isCorrect=false;

        }else if(!Validator.onlyLetters(description)){
            Validator.setError(description, onlyLetters);
            isCorrect=false;

        }
        if (!Validator.isNotEmpty(price)){
            Validator.setError(price, empty);
            isCorrect=false;

        }
        if (!Validator.isNotEmpty(size)){
            Validator.setError(size, empty);
            isCorrect=false;

        }
        if (!Validator.isNotEmpty(zipcode)){
            Validator.setError(zipcode, empty);
            isCorrect=false;

        }
        if (!Validator.isNotEmpty(editTextRooms)){
            Validator.setError(editTextRooms, empty);
            isCorrect=false;

        }
        if (categories.getSelectedItem()==null){
            //spinnerName.getSelectedItem()
            isCorrect=false;
        }
        String prueba = tvRegion.getText().toString();
        if (tvRegion.getText().toString()==""){
            isCorrect=false;
            Validator.setError(tvRegion, empty);
        }
        if (tvMunicipio.getText().toString()==""){
            isCorrect=false;
            Validator.setError(tvMunicipio, empty);


        }
        if (tvProvincia.getText().toString()==""){
            isCorrect=false;
            Validator.setError(tvProvincia, empty);


        }
        return isCorrect;

    }


}
