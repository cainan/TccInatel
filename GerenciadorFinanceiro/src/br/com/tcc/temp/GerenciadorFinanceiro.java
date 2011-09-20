package br.com.tcc.temp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.tcc.R;
import br.com.tcc.utils.Alerts;

public class GerenciadorFinanceiro extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(this, ScheduleService.class));

        initView();
    }

    /**
     * Initialize the view
     */
    private void initView() {
        Button registerBill = (Button) findViewById(R.id.btn_register_bill);
        Button locationGps = (Button) findViewById(R.id.btn_location_gps);
        Button showBills = (Button) findViewById(R.id.btn_show_bills);

        if (registerBill != null) {
            registerBill.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    startActivity(new Intent(getApplicationContext(), RegisterBillActivity.class));
                    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                    // overridePendingTransition(R.anim.fade, R.anim.hold);
                }

            });
        }

        if (locationGps != null) {
            locationGps.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    searchBanksGmaps();
                }

            });
        }

        if (showBills != null) {
            showBills.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    startActivity(new Intent(getApplicationContext(), ListBillActivity.class));
                }

            });
        }
    }

    /**
     * Create the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    /**
     * Set actions of menu's options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        String title;
        String message;
        Alerts al = new Alerts();
        switch (item.getItemId()) {
        case R.id.exit:
            finish();
            return true;
        case R.id.sobre:
            title = getResources().getString(R.string.app_name);
            message = getResources().getString(R.string.about_txt);
            al.AlertAbout(this, title, message);
            return true;
        default:
            return super.onOptionsItemSelected(item);

        }
    }

}