package br.com.tcc.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;
import android.content.Context;
import android.content.ContextWrapper;

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
	
	public void turnGPSOn(Context ctx){
        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider"); 
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            ctx.sendBroadcast(poke);
        }
    }

    public void turnGPSOff(Context ctx){
        String provider = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3")); 
            ctx.sendBroadcast(poke);
        }
    }
    
    public void chkGpsStatus(Context ctx){
    	
    	LocationManager locationManager = (LocationManager) ctx.getSystemService(ctx.LOCATION_SERVICE);
		boolean isGPS = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
		
		if(isGPS)
			Toast.makeText(((ContextWrapper) ctx).getBaseContext(),"GPS is ON", Toast.LENGTH_SHORT).show();
		else			
			Toast.makeText(((ContextWrapper) ctx).getBaseContext(),"GPS is OFF", Toast.LENGTH_SHORT).show();	 	
    }


}
