package com.ortizguerra.realsapp.ui.property;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
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
import com.ortizguerra.realsapp.model.AddFavResponse;
import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.retrofit.generator.AuthType;
import com.ortizguerra.realsapp.retrofit.generator.ServiceGenerator;
import com.ortizguerra.realsapp.retrofit.services.PropertyService;
import com.ortizguerra.realsapp.ui.favproperty.FavPropertyFragment;
import com.ortizguerra.realsapp.util.UtilToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MyPropertyRecyclerViewAdapter extends RecyclerView.Adapter<MyPropertyRecyclerViewAdapter.ViewHolder> {

    private final List<PropertyResponse> mValues;
    private final PropertyInteractionListener mListener;
    private FragmentTransaction fragmentChanger;

    private final Context context;
    private PropertyService propertyService;
    String jwt;
    private  int favouriteCode=0;
    public MyPropertyRecyclerViewAdapter(Context ctx, List<PropertyResponse> items, PropertyInteractionListener listener, int favCode) {
        mValues = items;
        context=ctx;
        mListener = listener;
        favouriteCode=favCode;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_property, parent, false);
        return new ViewHolder(view);
    }
    /*public void loadPictures(String id){
        //subir imagen
        List<String> photos = new ArrayList<>();
        propertyService = ServiceGenerator.createService(PropertyService.class);
        Call<ResponseContainer<PropertyOneResponse>> call = propertyService.getOne(id);
        call.enqueue(new Callback<ResponseContainer<PropertyOneResponse>>() {
            @Override
            public void onResponse(Call<ResponseContainer<PropertyOneResponse>> call, Response<ResponseContainer<PropertyOneResponse>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error in request get one", Toast.LENGTH_SHORT).show();
                } else {
                    //photos=response.body().getRows();
                    Toast.makeText(context, "Get one correctly done", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseContainer<PropertyOneResponse>> call, Throwable t) {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();

            }
        });        //subir imagen

    }*/
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        jwt = UtilToken.getToken(context);

        holder.mItem = mValues.get(position);


        //FALTA GLIDE CON LA IMAGEN

        if (holder.mItem.getPhotos().size()!=0){
            Glide
                    .with(context)
                    .load(holder.mItem.getPhotos().get(0))
                    .centerCrop()

                    .into(holder.imageViewcover);
        }else{
            Glide
                    .with(context)
                    .load("https://www.riverside.org.uk/wp-content/themes/rd-riverside/grunticon/png/no-images-placeholder.png")
                    .centerCrop()

                    .into(holder.imageViewcover);
        }

        holder.textViewAddress.setText(holder.mItem.getAddress());
        String eurosymbol = context.getResources().getString(R.string.euro);
        String price = String.valueOf(holder.mItem.getPrice())+eurosymbol ;
        String title = holder.mItem.getTitle()+", "+price;
        holder.textViewTitle.setText(title);
        String rooms = String.valueOf(holder.mItem.getRooms());
        holder.textViewRooms.setText(rooms);
        holder.textViewSize.setText(String.valueOf(holder.mItem.getSize()));
        //holder.textViewPrice.setText(price);
        //holder.textViewIdProperty.setText(holder.mItem.getId());
        toogleStar(holder);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent detailsActivity = new Intent(context ,DetailsActivity.class);
                detailsActivity.putExtra("property", holder.mItem.getId().toString());

                context.startActivity(detailsActivity);
            }
        });
        checkOptions(holder);





        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    //mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }
    public void goToLocation(ViewHolder holder){

    }
    public void toogleStar(ViewHolder holder) {
        if (holder.mItem.isFav() ){
            holder.imageViewFavourite.setImageResource(R.drawable.ic_star_filled_24dp);

        }else{
            holder.imageViewFavourite.setImageResource(R.drawable.ic_star_border_black_24dp);

        }
    }
    public void addFavourite(ViewHolder holder){
        propertyService = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        PropertyResponse p =holder.mItem;
        Call<AddFavResponse> call = propertyService.addFav(holder.mItem.getId());
        call.enqueue(new Callback<AddFavResponse>() {
            @Override
            public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Property added to favourites", Toast.LENGTH_LONG).show();
                    holder.mItem.setFav(true);
                    toogleStar(holder);
                    if (favouriteCode==1){
                        refreshList(holder);

                    }


                }
            }

            @Override
            public void onFailure(Call<AddFavResponse> call, Throwable t) {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void refreshList(ViewHolder holder){
        AppCompatActivity activity = (AppCompatActivity) context;
        Fragment myFragment = new FavPropertyFragment();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.contenedor, myFragment).addToBackStack(null).commit();
    }
    public void deleteFavourite(ViewHolder holder){
        propertyService = ServiceGenerator.createService(PropertyService.class, jwt, AuthType.JWT);
        PropertyResponse p =holder.mItem;
        Call<AddFavResponse> call = propertyService.deleteFav(holder.mItem.getId());
        call.enqueue(new Callback<AddFavResponse>() {
            @Override
            public void onResponse(Call<AddFavResponse> call, Response<AddFavResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, "Error in request", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Property deleted from favourites", Toast.LENGTH_LONG).show();
                    holder.mItem.setFav(false);
                    toogleStar(holder);
                    if (favouriteCode==1){
                        refreshList(holder);

                    }
                }
            }

            @Override
            public void onFailure(Call<AddFavResponse> call, Throwable t) {
                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void checkOptions(ViewHolder holder){
        String token = UtilToken.getToken(context);
        if (token==null){
            holder.imageViewLocation.setImageDrawable(null);
            holder.imageViewFavourite.setImageDrawable(null);
        }else{
            holder.imageViewLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToLocation(holder);

                }
            });

            holder.imageViewFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        if (holder.mItem.isFav()){
                            System.out.println("Es favorito");
                            deleteFavourite(holder);
                        }else{
                            System.out.println("NO es favorito");
                            addFavourite(holder);
                        }
                        //deleteFavourite(holder);
                        //addFavourite(holder);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageViewcover, imageViewLocation, imageViewFavourite;
        //public final TextView textViewCategory;
        //public final TextView textViewPrice;
        //public final TextView textViewRoom;
        public final TextView textViewAddress;
        public final TextView textViewTitle, textViewRooms, textViewSize;
        //private final TextView textViewIdProperty;
        private final ConstraintLayout constraintLayout;



        public PropertyResponse mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewcover = view.findViewById(R.id.imageViewCover);
            //textViewCategory = view.findViewById(R.id.textViewCategoryDetail);
            //textViewPrice = view.findViewById(R.id.textViewPriceDetail);
            //textViewRoom = view.findViewById(R.id.textViewRoom);
            textViewTitle = view.findViewById(R.id.textViewTitleDetail);
            textViewAddress = view.findViewById(R.id.textViewTitleDetail);
            imageViewLocation= view.findViewById(R.id.imageViewLocation);
            imageViewFavourite = view.findViewById(R.id.imageViewEdit);
            //textViewIdProperty = view.findViewById(R.id.textViewIdProperty);
            constraintLayout=view.findViewById(R.id.constraintProperty);
            textViewRooms=view.findViewById(R.id.textViewRooms);
            textViewSize=view.findViewById(R.id.textViewSize);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewAddress.getText() + "'";
        }
    }
}
