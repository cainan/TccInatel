package br.com.tcc.view;

import java.text.NumberFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
                Intent itt = new Intent(getApplicationContext(), EditDateActivity.class);
                itt.putExtra(EditBillActivity.BILL_PARAM, mBill);
                startActivityForResult(itt, 0);
            }

        });
        
        Button btnSms = (Button) findViewById(R.id.send_sms);
        btnSms.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
            	if(smsBody()!= null){           		
            		Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            		sendIntent.putExtra("sms_body", smsBody()); 
            		sendIntent.setType("vnd.android-dir/mms-sms");
            		startActivity(sendIntent);
            		finish();
            	}
            	else
            		finish();
            }

        });
        
        /*
        Button shareBtn = (Button) findViewById(R.id.share);
        shareBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String text = "código: " + mBill.getCodigoBarra();

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(sharingIntent, "Compartilhar com:"));

                // String number = "12346556"; // The number on which you want to send SMS
                // startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", number,
                // null)));

                // Uri uri = Uri.parse("smsto:");
                // Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                // it.putExtra("sms_body", "Here you can set the SMS text to be sent");
                // startActivity(it);

            }

        });
        */

        TextView billField = (TextView) findViewById(R.id.bill);
        if (billField != null && mBill.getNome() != null) {
            billField.setText(mBill.getNome());
        }

        TextView valueField = (TextView) findViewById(R.id.value);
        if (valueField != null && mBill.getValor() != null) {
        	float valor = Float.parseFloat(mBill.getValor());
        	NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            valueField.setText(nf.format(valor));
        }

        TextView payField = (TextView) findViewById(R.id.pay);
        if (payField != null && mBill.getVencimento() != null) {
            payField.setText(mBill.getVencimento());
        }

        View barcodeBtnLayout = (View) findViewById(R.id.barcode_btn_layout);
        View barcodeLayout = (View) findViewById(R.id.barcode_layout);
        barcodeBtnLayout.setVisibility(View.VISIBLE);
        barcodeLayout.setVisibility(View.VISIBLE);
        /*
        if (mBill.getCodigoBarra() != null) {
            if (barcodeLayout != null) {
                barcodeLayout.setVisibility(View.VISIBLE);
                TextView barcode = (TextView) findViewById(R.id.code);
                if (barcode != null) {
                    barcode.setText(mBill.getCodigoBarra());
                }
            }

            if (barcodeBtnLayout != null) {
                barcodeBtnLayout.setVisibility(View.VISIBLE);
            }
        }
        */
    }

    /**
     * Set the bill's notification field to 15 minutes later
     */
    private void remindMeLater() {
        GregorianCalendar deadLine = new GregorianCalendar();
        deadLine.add(GregorianCalendar.MINUTE, 15);

        int hour = deadLine.getTime().getHours();
        int minute = deadLine.getTime().getMinutes();
        mBill.setNotificar(hour + ":" + minute);

        DatabaseDelegate.getInstance(getApplicationContext()).editBillById(mBill);

        finish();

    }
    
    private String smsBody(){
    	String msg = "Pagamento de Conta";
   	
    	if (mBill.getNome() != null) {
            msg = msg + "\n\nConta: "+ mBill.getNome();
        }
    	if (mBill.getValor() != null) {
    		float valor = Float.parseFloat(mBill.getValor());
    		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    		msg = msg + "\nValor: "+ nf.format(valor);
        }
    	if (mBill.getVencimento() != null) {
    		msg = msg + "\nVencimento: "+ mBill.getVencimento();
        }
    	if (mBill.getCodigoBarra() != null) {
    		msg = msg + "\nCódigo de barra: "+ mBill.getCodigoBarra();
        }
    	return msg;
    }

    /**
     * Receive new date if the user changed it
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            mBill = (Conta) data.getSerializableExtra(EditDateActivity.RESULT_PARAM);
            initView();
        }
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
