package com.ortizguerra.realsapp.util.geography;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

public class Geocode {

    public static String getLatLong(Context ctx,String address) throws IOException {
        Geocoder geocoder = new Geocoder(ctx);
        double latitude = 0;
        double longitude = 0;
        List<Address> addresses;
        addresses = geocoder.getFromLocationName(address, 1);
        if(addresses.size() > 0) {
            latitude= addresses.get(0).getLatitude();
            longitude= addresses.get(0).getLongitude();
        }

        String result = latitude +","+ longitude;
        return result;
    }
}
