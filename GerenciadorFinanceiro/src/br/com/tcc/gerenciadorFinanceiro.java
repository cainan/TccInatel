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
    	Alerts al = new Alerts();
    	switch (item.getItemId()) {    
    		case R.id.exit:
    			this.finish();    			
    			return true;
    		case R.id.sobre:
    			al.AlertAbout(this,"Gerenciador Financeiro","Inatel - TCC \n Tema: S.O. Android");
    			return true;
    		default:        
    			return super.onOptionsItemSelected(item); 
    			
    	}
    	
    }
}