package com.ortizguerra.realsapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.model.LoginRegisterResponse;
import com.ortizguerra.realsapp.model.UserResponse;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.LoginService;
import com.ortizguerra.realsapp.ui.common.DashboardActivity;
import com.ortizguerra.realsapp.util.UtilToken;
import com.ortizguerra.realsapp.util.Validator;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LoginInteractionListener mListener;
    private EditText editTextemailRegister;
    private EditText editTextpasswordRegister;
    private EditText editTextpasswordTwoRegister;
    private Button button_register, button_cancel;
    private Context ctx;
    public RegisterFragment() {
        // Required empty public constructor
    }

    
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ctx=getContext();
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_register, container, false);
        loadItems(v);
        return v;
    }

    

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadItems(View v) {
        editTextemailRegister=v.findViewById(R.id.editTextEmailRegister);
        editTextpasswordRegister=v.findViewById(R.id.editTextPasswordOneRegister);
        editTextpasswordTwoRegister=v.findViewById(R.id.editTextPasswordTwoRegister);
        button_register=v.findViewById(R.id.buttonRegisterOnRegisterFragment);
        button_cancel=v.findViewById(R.id.button_cancel);
        button_register.setOnClickListener(v1 -> doRegister());
        button_cancel.setOnClickListener(v2 -> {
            if(getActivity() != null) {
                LoginFragment f = new LoginFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerLogin, f)
                        .commit();
            }
        });
    }

    private void doRegister() {
        String email = editTextemailRegister.getText().toString();
        String password= editTextpasswordRegister.getText().toString();
        String passwordTwo = editTextpasswordTwoRegister.getText().toString();
        if (validate()){
            UserResponse register = new UserResponse(email, password);
            LoginService service = ServiceGenerator.createService(LoginService.class);
            Call<LoginRegisterResponse> loginReponseCall = service.doRegister(register);

            loginReponseCall.enqueue(new retrofit2.Callback<LoginRegisterResponse>() {
                @Override
                public void onResponse(Call<LoginRegisterResponse> call, Response<LoginRegisterResponse> response) {
                    if (response.code() == 201) {
                        // success
                        UtilToken.setToken(ctx, response.body().getToken());
                        UtilToken.setId(ctx, response.body().getUser().get_id());
                        UtilToken.setToken(ctx, response.body().getToken());
                        startActivity(new Intent(ctx, DashboardActivity.class));
                    } else {
                        // error
                        Toast.makeText(ctx, "Error while signing up.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginRegisterResponse> call, Throwable t) {
                    Log.e("NetworkFailure", t.getMessage());
                    Toast.makeText(ctx, "Network Connection Failure", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public boolean validate(){
        int passMinSize=6, passMaxSize=15;
        Validator.clearError(editTextemailRegister);
        Validator.clearError(editTextpasswordRegister);
        Validator.clearError(editTextpasswordTwoRegister);
        String incorrectEmail, incorrectPassword, samePassword;
        incorrectEmail = getString(R.string.incorrect_email);
        incorrectPassword = getString(R.string.password_size);
        samePassword=getString(R.string.repeatPassword);
        boolean isValid=true;
        if (!Validator.isNotEmpty(editTextemailRegister) || !Validator.checkEmail(editTextemailRegister)){
            isValid=false;
            Validator.setError(editTextemailRegister, incorrectEmail);
        }

        if (Validator.isLessThan(editTextpasswordRegister, passMinSize) || Validator.isGreaterThan(editTextpasswordRegister, passMaxSize)){
            isValid=false;

            Validator.setError(editTextpasswordRegister, incorrectPassword);
        }
        if (!Validator.isSamePassword(editTextpasswordRegister, editTextpasswordTwoRegister)){
            isValid=false;
            Validator.setError(editTextpasswordTwoRegister, samePassword);
        }

        return isValid;

    }
}
