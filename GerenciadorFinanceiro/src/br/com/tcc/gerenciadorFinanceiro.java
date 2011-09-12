package br.com.tcc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class gerenciadorFinanceiro extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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