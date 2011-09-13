package br.com.tcc.View;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import br.com.tcc.R;

public class RegisterBill extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.register_bill);

    }

}
