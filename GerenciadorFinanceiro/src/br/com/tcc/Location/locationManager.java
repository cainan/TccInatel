package br.com.tcc.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class locationManager {
	
	private LocationManager locationManager;
	private Location location;
	

	
	public String getNameLocation(Context ctx){
		
		locationManager = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
        String bestProvider = locationManager.getBestProvider(new Criteria(),true);
        location = locationManager.getLastKnownLocation(bestProvider);
		
		Geocoder geoCoder = new Geocoder(ctx, Locale.getDefault());
		String end = null;
		
        try {
            List<Address> addresses = geoCoder.getFromLocation(location.getLatitude(),location.getLongitude(), 5);
            if (addresses.size() > 0) {
                end = addresses.get(0).getFeatureName();    	                  	                
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
        return end;
	}


}
