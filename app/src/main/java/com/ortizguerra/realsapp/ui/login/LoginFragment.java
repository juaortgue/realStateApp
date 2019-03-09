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
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.LoginService;
import com.ortizguerra.realsapp.ui.common.DashboardActivity;
import com.ortizguerra.realsapp.util.UtilToken;
import com.ortizguerra.realsapp.util.Validator;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText editTextemail;
    private EditText editTextpassword;
    private Button btn_register;
    private Button btn_login;
    private Context ctx;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LoginInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
        ctx=getContext();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        ctx=getContext();
        loadItems(v);

        return v;
    }


    public void doLogin() {
        String email = editTextemail.getText().toString();
        String password= editTextpassword.getText().toString();
        if (validate()){
            String credentials = Credentials.basic(email, password);
            LoginService service = ServiceGenerator.createService(LoginService.class);
            Call<LoginRegisterResponse> login = service.doLogin(credentials);
            login.enqueue(new Callback<LoginRegisterResponse>() {
                @Override
                public void onResponse(Call<LoginRegisterResponse> call, Response<LoginRegisterResponse> response) {
                    if (response.code() != 201) {
                        // error
                        Log.e("RequestError", response.message());
                        Toast.makeText(ctx, "Error while trying to login", Toast.LENGTH_SHORT).show();
                    } else {
                        // exito
                        UtilToken.setToken(ctx, response.body().getToken());
                        UtilToken.setId(ctx, response.body().getUser().get_id());

                        startActivity(new Intent(ctx, DashboardActivity.class));
                    }
                }

                @Override
                public void onFailure(Call<LoginRegisterResponse> call, Throwable t) {
                    Log.e("NetworkFailure", t.getMessage());
                    Toast.makeText(ctx, "Error. Can't connect to server", Toast.LENGTH_SHORT).show();
                }
            });
        }



    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof LoginInteractionListener) {
            mListener = (LoginInteractionListener) context;
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
        editTextemail=v.findViewById(R.id.editTextEmail);
        editTextpassword=v.findViewById(R.id.editTextPassword);
        btn_login=v.findViewById(R.id.buttonLogin);
        btn_register=v.findViewById(R.id.buttonRegister);
        btn_login.setOnClickListener(v1 -> doLogin());
        btn_register.setOnClickListener(v2 -> {
            if(getActivity() != null) {
                RegisterFragment f = new RegisterFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.containerLogin, f)
                        .commit();
            }
        });
    }
    public boolean validate(){
        int passMinSize=6, passMaxSize=15;
        Validator.clearError(editTextemail);
        Validator.clearError(editTextpassword);
        String incorrectEmail, incorrectPassword;
        incorrectEmail = getString(R.string.incorrect_email);
        incorrectPassword = getString(R.string.password_size);
        boolean isValid=true;
        if (!Validator.isNotEmpty(editTextemail) || !Validator.checkEmail(editTextemail)){
            isValid=false;
            Validator.setError(editTextemail, incorrectEmail);
        }

        if (Validator.isLessThan(editTextpassword, passMinSize) || Validator.isGreaterThan(editTextpassword, passMaxSize)){
            isValid=false;

            Validator.setError(editTextpassword, incorrectPassword);
        }


        return isValid;

    }
}
