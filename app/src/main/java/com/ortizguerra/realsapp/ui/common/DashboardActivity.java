package com.ortizguerra.realsapp.ui.common;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.EditText;

import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.ui.createproperty.CreatePropertyActivity;
import com.ortizguerra.realsapp.ui.favproperty.FavPropertyFragment;
import com.ortizguerra.realsapp.ui.login.LoginActivity;
import com.ortizguerra.realsapp.ui.map.MapsActivity;
import com.ortizguerra.realsapp.ui.myproperties.OwnPropertiesFragment;
import com.ortizguerra.realsapp.ui.property.PropertyFragment;
import com.ortizguerra.realsapp.ui.property.PropertyInteractionListener;
import com.ortizguerra.realsapp.util.UtilToken;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PropertyInteractionListener {

    MenuItem optionMenuFavourites, optionMenuMyOwnProperties, optionMenuLogin, optionMenuLogout;
    private FragmentTransaction fragmentChanger;
    private Fragment favourite, myProperty, property;
    private FloatingActionButton fab;
    Map<String, String> options = new HashMap<>();

    EditText rooms, city, province, address, zipcode, min_price, max_price, min_size, max_size;
    int frag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        /*Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent addProperty = new Intent(getApplicationContext(), CreatePropertyActivity.class);
                startActivity(addProperty);
            }
        });
        fab.hide();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        loadItems(navigationView);
        changeMenuVisibility();
        favourite = new FavPropertyFragment();
        myProperty = new OwnPropertiesFragment();
        property = new PropertyFragment(options);
        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, property);
        fragmentChanger.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menu.add(R.menu.menu_items);
        //MenuInflater inflater = getMenuInflater();

        getMenuInflater().inflate(R.menu.menu_items, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id==R.id.action_filter){
            searchOptions();
            return true;
        }
        if (id==R.id.action_map){
            Intent mapActivity = new Intent(getApplicationContext(), MapsActivity.class);
            mapActivity.putExtra("options", (Serializable) options);
            startActivity(mapActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment f = null;

        if (id == R.id.nav_every_property) {
            fab.hide();
            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, property);
            fragmentChanger.commit();
        } else if (id == R.id.nav_my_property) {
            fab.show();
            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, myProperty);
            fragmentChanger.commit();
        } else if (id == R.id.nav_favourite_property) {
            fab.hide();
            fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, favourite);
            fragmentChanger.commit();
        } else if (id == R.id.nav_login) {
            Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(iLogin);
        } else if (id == R.id.nav_logout) {
            createAndShowLogoutDialog();
        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void changeMenuVisibility(){
        String token = UtilToken.getToken(this);
        if(token == null) {
            optionMenuMyOwnProperties.setVisible(false);
            optionMenuFavourites.setVisible(false);
            optionMenuLogout.setVisible(false);
            optionMenuLogin.setVisible(true);

        }else{
            optionMenuMyOwnProperties.setVisible(true);
            optionMenuFavourites.setVisible(true);
            optionMenuLogout.setVisible(true);
            optionMenuLogin.setVisible(false);

        }
    }
    public void loadItems(NavigationView navigationView){
        optionMenuLogin = navigationView.getMenu().findItem(R.id.nav_login);
        optionMenuFavourites= navigationView.getMenu().findItem(R.id.nav_favourite_property);
        optionMenuMyOwnProperties=navigationView.getMenu().findItem(R.id.nav_my_property);
        optionMenuLogout=navigationView.getMenu().findItem(R.id.nav_logout);


    }
    public void createAndShowLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.logoutDialog)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                logout();
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
    public  void logout(){
        UtilToken.clearAll(this);
        changeMenuVisibility();
        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, new PropertyFragment(options));
        fragmentChanger.commit();
    }
    public void searchOptions () {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ResourceType")
        View dialogLayout = inflater.inflate(R.layout.activity_search, null);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setView(dialogLayout);

        rooms = dialogLayout.findViewById(R.id.search_rooms);
        city = dialogLayout.findViewById(R.id.search_city);
        province = dialogLayout.findViewById(R.id.search_province);
        zipcode = dialogLayout.findViewById(R.id.search_zipcode);
        address = dialogLayout.findViewById(R.id.search_address);
        min_price = dialogLayout.findViewById(R.id.search_min_price);
        max_price = dialogLayout.findViewById(R.id.search_max_price);
        min_size = dialogLayout.findViewById(R.id.search_min_size);
        max_size = dialogLayout.findViewById(R.id.search_max_size);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            if(frag == 0){

                if(!rooms.getText().toString().equals("")){
                    options.put("rooms", rooms.getText().toString());
                } if(!city.getText().toString().equals("")){
                    options.put("city", city.getText().toString());
                } if (!province.getText().toString().equals("")) {
                    options.put("province", province.getText().toString());
                } if (!zipcode.getText().toString().equals("")){
                    options.put("zipcode", zipcode.getText().toString());
                } if (!address.getText().toString().equals("")){
                    options.put("address", address.getText().toString());
                } if (!min_price.getText().toString().equals("")) {
                    options.put("min_price", min_price.getText().toString());
                } if (!max_price.getText().toString().equals("")){
                    options.put("max_price", max_price.getText().toString());
                } if(!min_size.getText().toString().equals("")) {
                    options.put("min_size", min_size.getText().toString());
                } if(!max_size.getText().toString().equals("")){
                    options.put("max_size", max_size.getText().toString());
                }
                System.out.println(options.values());
                property = new PropertyFragment(options);
                fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, property);
                fragmentChanger.commit();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            Log.d("Back", "Going back");
        });
        android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

    }
}
