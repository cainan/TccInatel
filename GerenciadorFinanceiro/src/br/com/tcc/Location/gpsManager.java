package br.com.tcc.Location;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

public class GpsManager {
    
    
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
    
    
    
    public boolean chkConnectionStatus(Context ctx){
        
        ConnectivityManager connMgr = (ConnectivityManager)
        ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(wifi.isAvailable()){
            Toast.makeText(ctx, "Wifi Connection" , Toast.LENGTH_LONG).show();
            return true;
        }
        else if( mobile.isAvailable() ){
            Toast.makeText(ctx, "Mobile 3G Connection" , Toast.LENGTH_LONG).show();
            return true;
        }
        else{
            Toast.makeText(ctx, "No Network Connection" , Toast.LENGTH_LONG).show();
            return false;
            }
    }
}
