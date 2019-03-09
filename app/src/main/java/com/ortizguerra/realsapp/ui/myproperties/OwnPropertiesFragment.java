package com.ortizguerra.realsapp.ui.myproperties;

import android.content.Context;
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
import com.ortizguerra.realsapp.model.MyPropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.util.UtilToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OwnPropertiesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private MyPropertiesInteractionListener mListener;
    Context ctx = getContext();
    List<MyPropertyResponse> properties = new ArrayList<>();
    String jwt;
    PropertyService service;
    OwnPropertiesRecyclerViewAdapter adapter;

    public OwnPropertiesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static OwnPropertiesFragment newInstance(int columnCount) {
        OwnPropertiesFragment fragment = new OwnPropertiesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        jwt = UtilToken.getToken(getContext());

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myproperties_list, container, false);

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

            Call<ResponseContainer<MyPropertyResponse>> call = service.getMine();
            call.enqueue(new Callback<ResponseContainer<MyPropertyResponse>>() {
                @Override
                public void onResponse(Call<ResponseContainer<MyPropertyResponse>> call, Response<ResponseContainer<MyPropertyResponse>> response) {
                    if (response.code() != 200) {
                        Toast.makeText(getActivity(), "Error in request", Toast.LENGTH_SHORT).show();
                    } else {
                        properties = response.body().getRows();

                        adapter = new OwnPropertiesRecyclerViewAdapter(context, properties, mListener);
                        recyclerView.setAdapter(adapter);

                    }
                }

                @Override
                public void onFailure(Call<ResponseContainer<MyPropertyResponse>> call, Throwable t) {
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
