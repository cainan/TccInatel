package br.com.tcc.view;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import br.com.tcc.R;
import br.com.tcc.utils.GpsManager;

public class BaseActivity extends Activity {

    /** Hold an instance of GPS manager */
    public GpsManager mGpsManager = new GpsManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // put some animations when transition from an activity to another
        overridePendingTransition(R.anim.fade, R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // put some animations when transition from an activity to another
        overridePendingTransition(R.anim.fade, R.anim.fade_out);
    }

    /**
     * Turn the gps off
     */
    @Override
    protected void onResume() {
        this.mGpsManager.turnGPSOff(getApplicationContext());
        super.onResume();
    }

    /**
     * Open Google maps to search for banks
     */
    public void searchBanksGmaps() {
        if (mGpsManager.chkConnectionStatus(getApplicationContext())) {
            mGpsManager.turnGPSOn(getApplicationContext());
            String geoUriString = ("geo:0,0?q=Agencias bancarias");
            Uri geoUri = Uri.parse(geoUriString);
            Intent mapCall = new Intent(Intent.ACTION_VIEW, geoUri);
            startActivity(mapCall);
        } else
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG)
                    .show();
    }

}
