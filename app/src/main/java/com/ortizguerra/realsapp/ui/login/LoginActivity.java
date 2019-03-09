package com.ortizguerra.realsapp.ui.login;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ortizguerra.realsapp.R;

public class LoginActivity extends AppCompatActivity{
    private FragmentTransaction fragmentChanger;
    private Fragment loginFragment, registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_login);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Cabecera)));
        registerFragment=new RegisterFragment();
        loginFragment=new LoginFragment();

        fragmentChanger = getSupportFragmentManager().beginTransaction().replace(R.id.containerLogin, loginFragment);
        fragmentChanger.commit();
        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


}
