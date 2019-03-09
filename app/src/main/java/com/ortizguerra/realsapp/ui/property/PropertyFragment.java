package com.ortizguerra.realsapp.ui.property;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.util.UtilToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int NO_FAV_CODE = 0;

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private PropertyInteractionListener mListener;
    Context ctx;
    String token;
    MyPropertyRecyclerViewAdapter adapter;
    List<PropertyResponse> properties;
    private PropertyService propertyService;
    private final int FAV_CODE=0;
    Map<String, String> options = new HashMap<>();

    @SuppressLint("ValidFragment")
    public PropertyFragment(Map<String,String> options) {
        this.options = options;
    }
    public PropertyFragment() {
    }

    public static PropertyFragment newInstance(int columnCount) {
        PropertyFragment fragment = new PropertyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_property_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            //options = new HashMap<>();
            Context context = view.getContext();
            token=UtilToken.getToken(context);
            options.put("near", "-6.0071807999999995,37.3803677");
            options.put("max_distance","1000000000000");
            options.put("min_distance","1");

            final RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            properties = new ArrayList<>();

            if(token==null){
                if (options.size()<=3){
                    loadProperties(recyclerView);
                }else{
                    loadPropertiesFilter(recyclerView);

                }
                //

            }else{
                loadFavProperties(recyclerView, token);
            }



        }
        return view;
    }
    public void loadPropertiesFilter(RecyclerView recyclerView){

        propertyService = ServiceGenerator.createService(PropertyService.class);

        Call<ResponseContainer<PropertyResponse>> call = propertyService.listProperties(options);
        call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (response.code() != 200) {
                    Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    properties = response.body().getRows();

                    adapter = new MyPropertyRecyclerViewAdapter(getContext(), properties, mListener, NO_FAV_CODE);
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

    public void loadFavProperties(RecyclerView recyclerView, String token){
        propertyService= ServiceGenerator.createService(PropertyService.class,token, AuthType.JWT);
        Call<ResponseContainer<PropertyResponse>> call = propertyService.listFavsProperties();
        call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.e("error response", "code error");
                    Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("successful response", "code error");

                    properties = response.body().getRows();

                    adapter = new MyPropertyRecyclerViewAdapter(
                            getContext(),
                            properties,
                            mListener,
                            FAV_CODE);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                Log.e("failure", "failure in petition");

            }
        });

    }
    public void loadProperties(RecyclerView recyclerView){
        propertyService= ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainer<PropertyResponse>> call = propertyService.listProperties();
        call.enqueue(new Callback<ResponseContainer<PropertyResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyResponse>> call, Response<ResponseContainer<PropertyResponse>> response) {
                if (!response.isSuccessful()) {
                    Log.e("error response", "code error");
                    Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("successful response", "code error");

                    properties = response.body().getRows();

                    adapter = new MyPropertyRecyclerViewAdapter(
                            getContext(),

                            properties,
                            mListener, NO_FAV_CODE);
                    recyclerView.setAdapter(adapter);
                    options = new HashMap<>();
                    options.put("near", "-6.0071807999999995,37.3803677");
                    options.put("max_distance","1000000000000");
                    options.put("min_distance","1");

                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyResponse>> call, Throwable t) {
                Log.e("failure", "failure in petition");
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PropertyInteractionListener) {
            mListener = (PropertyInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }


}
