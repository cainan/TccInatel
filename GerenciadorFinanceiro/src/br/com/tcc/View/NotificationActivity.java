package br.com.tcc.View;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import br.com.tcc.R;
import br.com.tcc.Model.Conta;

public class NotificationActivity extends Activity {

    /** Hold the Bundle parameter of bill */
    public static final String BILL_PARAMETER = "billParameter";

    /** Hold the Notification Id */
    private int mNotificationId;

    /** Hold the Bill received */
    private Conta bill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_bill);

        // Get the intent sent from notification
        Intent itt = getIntent();
        if (itt != null) {
            bill = (Conta) itt.getSerializableExtra(BILL_PARAMETER);
            mNotificationId = bill.getId();
        }
    }

    @Override
    protected void onDestroy() {

        // Cleaning the notification bar
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(mNotificationId);

        super.onDestroy();
    }

}
