package br.com.tcc.view;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import br.com.tcc.R;
import br.com.tcc.adapter.GalleryAdapter;
import br.com.tcc.model.database.DatabaseDelegate;
import br.com.tcc.service.ScheduleService;
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
                    startActivity(new Intent(getApplicationContext(), ListActivity.class));
                }

            });
        }

        Button barGraphBtn = (Button) findViewById(R.id.btn_bar_graphs);
        if (barGraphBtn != null) {
            barGraphBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent result = new Intent();
                    result.setClass(getApplicationContext(), GraphActivity.class);
                    result.putExtra("com.java4less.rchart.samples.file", "barChart3D.txt");
                    startActivity(result);
                }

            });
        }
        
        /*
        Button pieGraphBtn = (Button) findViewById(R.id.btn_pie_graphs);
        if (pieGraphBtn != null) {
            pieGraphBtn.setOnClickListener(new OnClickListener() {

                //@Override
                public void onClick(View arg0) {
                    Intent result = new Intent();
                    result.setClass(getApplicationContext(), GraphActivity.class);
                    result.putExtra("com.java4less.rchart.samples.file", "pieChart3D.txt");
                    startActivity(result);
                }

            });
        }
        */
        
        Button pieGraphBtnBar = (Button) findViewById(R.id.btn_pie_graphs_bar);
        if (pieGraphBtnBar != null) {
            pieGraphBtnBar.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                	Intent intent = new GraphBarChart().execute(getApplicationContext());
                	startActivity(intent);
                }

            });
        }
        
        Button pieGraphBtn2 = (Button) findViewById(R.id.btn_pie_graphs_2);
        if (pieGraphBtn2 != null) {
            pieGraphBtn2.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {                	                	
                	Intent intent = new GraphPieChart().execute(getApplicationContext());
                	startActivity(intent);
                }

            });
        }

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; 
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Gallery gallery = (Gallery) findViewById(R.id.gallery);
        gallery.setAdapter(new GalleryAdapter(this, DatabaseDelegate.getInstance(this).readDailyBills(day, month, year)));
        

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