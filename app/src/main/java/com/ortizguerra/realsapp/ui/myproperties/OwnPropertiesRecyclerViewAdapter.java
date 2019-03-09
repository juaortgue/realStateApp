package com.ortizguerra.realsapp.ui.myproperties;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ortizguerra.realsapp.R;
import com.ortizguerra.realsapp.dto.EditPropertyDto;
import com.ortizguerra.realsapp.model.MyPropertyResponse;
import com.ortizguerra.realsapp.model.PropertyOneResponse;
import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainerOneRow;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.ui.editproperty.EditPropertyActivity;
import com.ortizguerra.realsapp.ui.favproperty.FavPropertyFragment;
import com.ortizguerra.realsapp.ui.myproperties.dummy.DummyContent.DummyItem;
import com.ortizguerra.realsapp.ui.property.DetailsActivity;
import com.ortizguerra.realsapp.ui.property.MyPropertyRecyclerViewAdapter;
import com.ortizguerra.realsapp.util.UtilToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnPropertiesRecyclerViewAdapter extends RecyclerView.Adapter<OwnPropertiesRecyclerViewAdapter.ViewHolder> {

    private final List<MyPropertyResponse> mValues;
    private final MyPropertiesInteractionListener mListener;
    Context contexto;
    String jwt;
    PropertyService service;



    public OwnPropertiesRecyclerViewAdapter(Context ctx, List<MyPropertyResponse> items, MyPropertiesInteractionListener listener) {

        mValues = items;
        mListener = listener;
        contexto = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_myproperties, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        jwt = UtilToken.getToken(contexto);
        setItems(holder, position);


    }
    public void setItems(ViewHolder holder, int position){
        holder.mItem = mValues.get(position);
        if (holder.mItem.getPhotos().size()!=0){
            Glide
                    .with(contexto)
                    .load(holder.mItem.getPhotos().get(0))
                    .centerCrop()
                    .into(holder.imageViewcover);
        }else{
            Glide
                    .with(contexto)
                    .load("https://www.riverside.org.uk/wp-content/themes/rd-riverside/grunticon/png/no-images-placeholder.png")
                    .centerCrop()
                    .into(holder.imageViewcover);
        }

        holder.textViewAddress.setText(holder.mItem.getAddress());

        String eurosymbol = contexto.getResources().getString(R.string.euro);
        String price = String.valueOf(holder.mItem.getPrice())+eurosymbol ;
        String title = holder.textViewTitle.getText().toString()+", "+price;
        holder.textViewTitle.setText(title);
        String rooms = String.valueOf(holder.mItem.getRooms());
        holder.textViewRooms.setText(rooms);
        holder.textViewSize.setText(String.valueOf(holder.mItem.getSize()));
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsActivity = new Intent(contexto , DetailsActivity.class);
                detailsActivity.putExtra("property", holder.mItem.getId());

                contexto.startActivity(detailsActivity);
            }
        });

        holder.imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEdited(holder);
            }
        });

        holder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createAndShowDeleteDialog(holder);

            }
        });


    }
    public void createAndShowDeleteDialog(ViewHolder holder){
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setTitle(R.string.deleteDialog)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteProperty(holder);
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
    public void createEdited(final ViewHolder holder) {
        EditPropertyDto editedDto = new EditPropertyDto();
        System.out.println(holder.mItem.getId());
        service = ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainerOneRow<PropertyOneResponse>> callOne = service.getOne(holder.mItem.getId());
        callOne.enqueue(new Callback<ResponseContainerOneRow<PropertyOneResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainerOneRow<PropertyOneResponse>> call, Response<ResponseContainerOneRow<PropertyOneResponse>> response) {
                PropertyOneResponse resp = response.body().getRows();
                //editedDto.setId(holder.mItem.getId());
                editedDto.setAddress(resp.getAddress());
                editedDto.setCategoryId(resp.getCategoryId().getId());
                editedDto.setCity(resp.getCity());
                editedDto.setDescription(resp.getDescription());
                editedDto.setLoc(resp.getLoc());
                //editedDto.setOwnerId(holder.mItem.getOwnerId());
                //editedDto.setPhotos(resp.getPhotos());
                long price = (new Double(resp.getPrice())).longValue();
                editedDto.setPrice(price);
                editedDto.setRooms(Long.valueOf(resp.getRooms()));
                editedDto.setProvince(resp.getProvince());
                editedDto.setZipcode(resp.getZipcode());
                editedDto.setSize((long)resp.getSize());
                editedDto.setTitle(resp.getTitle());

                Intent editActivity = new Intent(contexto, EditPropertyActivity.class);
                editActivity.putExtra("property", editedDto);
                editActivity.putExtra("idProperty", holder.mItem.getId());
                contexto.startActivity(editActivity);
            }

            @Override
            public void onFailure(Call<ResponseContainerOneRow<PropertyOneResponse>> call, Throwable t) {

            }
        });


    }
    public void refreshList(ViewHolder holder){
        AppCompatActivity activity = (AppCompatActivity) contexto;
        Fragment myFragment = new OwnPropertiesFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, myFragment).addToBackStack(null).commit();
    }
    public void deleteProperty(final ViewHolder holder) {
        String id = holder.mItem.getId();
        service = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        Call<MyPropertyResponse> callDelete = service.delete(id);
        callDelete.enqueue(new Callback<MyPropertyResponse>() {
            @Override
            public void onResponse(Call<MyPropertyResponse> call, Response<MyPropertyResponse> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(contexto, "Property deleted", Toast.LENGTH_SHORT).show();
                    refreshList(holder);
                } else {
                    Toast.makeText(contexto, "Error while deleting", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyPropertyResponse> call, Throwable t) {
                Toast.makeText(contexto, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageViewcover, imageViewEdit, imageViewDelete;
        public final TextView textViewAddress, textViewSize, textViewRooms;
        public MyPropertyResponse mItem;

        public final TextView textViewTitle;
        private final ConstraintLayout constraintLayout;




        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewcover = view.findViewById(R.id.imageViewCover);
            textViewTitle = view.findViewById(R.id.textViewTitleDetail);
            textViewAddress = view.findViewById(R.id.textViewTitleDetail);
            imageViewEdit= view.findViewById(R.id.imageViewEdit);
            imageViewDelete = view.findViewById(R.id.imageViewDelete);
            constraintLayout=view.findViewById(R.id.constraintProperty);
            textViewRooms=view.findViewById(R.id.textViewRooms);
            textViewSize=view.findViewById(R.id.textViewSize);
        }

        @Override
        public String toString() {
            return super.toString() + textViewTitle.getText().toString();
        }
    }
}
