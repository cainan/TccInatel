package br.com.tcc.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.tcc.R;
import br.com.tcc.Alert.Alerts;
import br.com.tcc.Service.ScheduleService;

public class GerenciadorFinanceiro extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        startService(new Intent(this, ScheduleService.class));

        initView();

    }

    private void initView() {
        Button registerBill = (Button) findViewById(R.id.btn_register_bill);
        if (registerBill != null) {
            registerBill.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    startActivity(new Intent(getApplicationContext(), RegisterBill.class));
                }

            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        String title;
        String message;
        Alerts al = new Alerts();
        switch (item.getItemId()) {
        case R.id.exit:
            this.finish();
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