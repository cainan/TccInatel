package br.com.tcc.view;

import java.util.GregorianCalendar;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.tcc.R;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;

public class NotificationActivity extends BaseActivity {

    /** Hold the Bundle parameter of bill */
    public static final String BILL_PARAMETER = "billParameter";

    /** Hold the Notification Id */
    private int mBillId;

    /** Hold the Bill received */
    private Conta mBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decision_view);

        // Get the intent sent from notification
        Intent itt = getIntent();
        if (itt != null) {
            mBill = (Conta) itt.getSerializableExtra(BILL_PARAMETER);
            mBillId = mBill.getId();
        }

        initView();
    }

    /**
     * Initializing the view
     */
    private void initView() {
        Button searchBankBtn = (Button) findViewById(R.id.search_bank);
        searchBankBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                searchBanksGmaps();
            }

        });

        Button markAsPayedBtn = (Button) findViewById(R.id.mark_as_payed);
        markAsPayedBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                DatabaseDelegate.getInstance(getApplicationContext()).markAsPayed(mBillId);
                finish();
            }

        });

        Button remindLaterBtn = (Button) findViewById(R.id.remind_later);
        remindLaterBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                remindMeLater();
            }

        });

        Button remarkBtn = (Button) findViewById(R.id.remark);
        remarkBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Remarcar a data da conta
            }

        });
    }

    /**
     * Set the bill's notification field to 15 minutes later
     */
    private void remindMeLater() {
        GregorianCalendar deadLine = new GregorianCalendar();
        Log.d("log", "deadline: " + deadLine.getTime());

        deadLine.roll(GregorianCalendar.MINUTE, 15);
        Log.d("log", "deadline: " + deadLine.getTime());

        // TODO Quando a hora for, por exemplo, 10:50, se aumentarmos 15 min, a hora ficará 10:05.
        // Precisa-se de uma lógica para incrementar esses 15 minutos da forma certa.

        finish();

    }

    /**
     * Cancel the notification at Notification bar when the activity is destroyed
     */
    @Override
    protected void onDestroy() {

        // Cleaning the notification bar
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(mBillId);

        super.onDestroy();
    }

}
