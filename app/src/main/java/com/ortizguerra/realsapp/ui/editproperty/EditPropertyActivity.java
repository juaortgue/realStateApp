package com.ortizguerra.realsapp.ui.editproperty;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
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
import com.ortizguerra.realsapp.dto.EditPropertyDto;
import com.ortizguerra.realsapp.model.CategoryResponse;
import com.ortizguerra.realsapp.model.EditPropertyResponse;
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

public class EditPropertyActivity extends AppCompatActivity implements View.OnClickListener, GeographyListener {
    private EditText title, description, price, size, zipcode, address, rooms;
    private String fullAddress, jwt, loc;
    private TextView tvRegion;
    private TextView tvProvincia;
    private TextView tvMunicipio;
    PropertyService service;
    Uri uriSelected;
    UserResponse me;
    private Button btProbar, btnEdit;
    private Spinner categories;
    String id;
    private List<CategoryResponse> listCategories = new ArrayList<>();
    private FloatingActionButton addPhoto;
    EditPropertyDto propertyDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_property);
        jwt = UtilToken.getToken(EditPropertyActivity.this);

        Intent i = getIntent();
        propertyDto = (EditPropertyDto) i.getSerializableExtra("property");
        id = i.getStringExtra("idProperty");
        loadItems();
        setItems();

        btnEdit.setOnClickListener(v -> {
            if (validate()){
                editProperty();
            }else{
                Toast.makeText(EditPropertyActivity.this, "There are any mistakes.", Toast.LENGTH_SHORT).show();
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
        Validator.clearError(rooms);
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
        if (!Validator.isNotEmpty(rooms)){
            Validator.setError(rooms, empty);
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
    public void editProperty() {
        fullAddress = "Calle " + address.getText().toString() + ", " + zipcode.getText().toString() + " " + " " + tvProvincia.getText().toString() + ", España";
        try {
            loc = getLoc(fullAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

        EditPropertyDto edited = propertyDto;
        CategoryResponse chosen = (CategoryResponse) categories.getSelectedItem();
        edited.setTitle(title.getText().toString());
        edited.setDescription(description.getText().toString());
        edited.setAddress(address.getText().toString());
        edited.setZipcode(zipcode.getText().toString());
        edited.setCity(tvMunicipio.getText().toString());
        edited.setPrice(Long.parseLong(price.getText().toString()));
        edited.setSize(Long.parseLong(size.getText().toString()));
        edited.setProvince(tvProvincia.getText().toString());
        edited.setCategoryId(chosen.getId());
        edited.setLoc(loc);
        edited.setRooms(Long.parseLong(rooms.getText().toString()));

        editProperty(edited);
    }
    public void editProperty(EditPropertyDto edited) {
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);


        Call<EditPropertyResponse> call = service.edit(id, edited);
        call.enqueue(new Callback<EditPropertyResponse>() {
            @Override
            public void onResponse(Call<EditPropertyResponse> call, Response<EditPropertyResponse> response) {

                if (response.isSuccessful()) {

                    Toast.makeText(EditPropertyActivity.this, "edited", Toast.LENGTH_SHORT).show();
                    Intent dashboard = new Intent(getApplicationContext(), DashboardActivity.class);
                    startActivity(dashboard);
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EditPropertyResponse> call, Throwable t) {
                  Toast.makeText(EditPropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setItems() {
        title.setText(propertyDto.getTitle());
        description.setText(propertyDto.getDescription());
        price.setText(String.valueOf(propertyDto.getPrice()));
        address.setText(propertyDto.getAddress());
        size.setText(String.valueOf(propertyDto.getSize()));
        zipcode.setText(String.valueOf(propertyDto.getZipcode()));
        address.setText(propertyDto.getAddress());
        rooms.setText(String.valueOf(propertyDto.getRooms()));
    }
    public void loadItems() {
        title = findViewById(R.id.title_edit);
        description = findViewById(R.id.description_edit);
        price = findViewById(R.id.price_edit);
        size = findViewById(R.id.size_edit);
        zipcode = findViewById(R.id.zipcode_edit);
        address = findViewById(R.id.address_edit);

        tvRegion = (TextView) findViewById(R.id.tvRegion);
        tvProvincia = (TextView) findViewById(R.id.tvProvincia);
        tvMunicipio = (TextView) findViewById(R.id.tvMunicipio);
        rooms= findViewById(R.id.rooms_edit);
        btProbar = findViewById(R.id.btProbar);
        btProbar.setOnClickListener(this);
        categories = findViewById(R.id.spinner_category);
        loadAllCategories();

        btnEdit = findViewById(R.id.edit_property);
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
                            new ArrayAdapter<>(EditPropertyActivity.this, android.R.layout.simple_spinner_dropdown_item, listCategories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categories.setAdapter(adapter);
                    categories.setSelection(listCategories.size() - 1);
                } else {
                    Toast.makeText(EditPropertyActivity.this, "Error loading categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<CategoryResponse>> call, Throwable t) {
                Toast.makeText(EditPropertyActivity.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getLoc(String fullAddress) throws IOException {
        String loc = Geocode.getLatLong(EditPropertyActivity.this, fullAddress);
        return loc;
    }

    @Override
    public void onGeographySelected(Map<String, String> hm) {
        tvRegion.setText(hm.get(GeographySpain.REGION));
        tvProvincia.setText(hm.get(GeographySpain.PROVINCIA));
        tvMunicipio.setText(hm.get(GeographySpain.MUNICIPIO));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btProbar) {
            GeographySelector gs = new GeographySelector(EditPropertyActivity.this);
            gs.setOnGeograpySelectedListener(EditPropertyActivity.this);
            FragmentManager fm = getSupportFragmentManager();
            gs.show(fm, "geographySelector");
        }

    }
}
