package com.ortizguerra.realsapp.ui.favproperty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;

import com.ortizguerra.realsapp.ui.login.LoginActivity;
import com.ortizguerra.realsapp.ui.property.MyPropertyRecyclerViewAdapter;
import com.ortizguerra.realsapp.ui.property.PropertyInteractionListener;
import com.ortizguerra.realsapp.util.UtilToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavPropertyFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int FAV_CODE = 1;

    private int mColumnCount = 1;
    private PropertyInteractionListener mListener;
    Context ctx = getContext();
    List<PropertyResponse> properties = new ArrayList<>();
    String jwt;
    PropertyService service;
    MyPropertyRecyclerViewAdapter adapter;


    public FavPropertyFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavPropertyFragment newInstance(int columnCount) {
        FavPropertyFragment fragment = new FavPropertyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jwt = UtilToken.getToken(getContext());

        if (jwt == null) {
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
        }
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favproperty_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);

            Call<ResponseContainer<PropertyResponse>> call = service.getFavs();
            call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
                @Override
                public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                    } else {
                        properties = response.body().getRows();
                        for (PropertyResponse p : properties){
                            p.setFav(true);
                        }
                        adapter = new MyPropertyRecyclerViewAdapter(context, properties, mListener, FAV_CODE);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                    Log.e("NetworkFailure", t.getMessage());
                    Toast.makeText(getActivity(), "Error de conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
