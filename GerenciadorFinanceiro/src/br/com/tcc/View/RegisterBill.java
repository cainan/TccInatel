package br.com.tcc.View;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import br.com.tcc.R;
import br.com.tcc.Database.DatabaseDelegate;
import br.com.tcc.Model.Conta;

public class RegisterBill extends Activity {

    /** Bundle fields that save is the labels are selected */
    private static final String NAME_LABEL = "nameLabel";
    private static final String PAYMENT_DATE_LABEL = "paymentDateLabel";
    private static final String VALUE_LABEL = "valueLabel";

    /** The minimum length in a field */
    private static final int MINIMUM_LENGTH = 2;

    /** Dialogs ID */
    private static final int DATE_DIALOG_ID = 0;
    private static final int INCOMPLETE_FIELD_DIALOG_ID = 1;

    /** Hold the year */
    private int mYear;

    /** Hold the month */
    private int mMonth;

    /** Hold the day */
    private int mDay;

    /** Hold the barcode field */
    private TextView mBarcodeEdit;

    /** Hold the Bill's value field */
    private TextView mValueEdit;

    /** Hold the Bill's name field */
    private TextView mNameEdit;

    /** Hold the payment date field */
    private EditText mPaymentDateEdit;

    /** Hold the Bill's name label */
    private TextView mNameLabel;

    /** Hold the payment date label */
    private TextView mPaymentDateLabel;

    /** Hold the Bill's value label */
    private TextView mValueLabel;

    /** Hold the checkbox value */
    private CheckBox mPayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.register_bill);

        initView();

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

    }

    /**
     * Initializing the view
     */
    private void initView() {

        mNameLabel = (TextView) findViewById(R.id.name_label);
        mPaymentDateLabel = (TextView) findViewById(R.id.vencimento_label);
        mValueLabel = (TextView) findViewById(R.id.valor_label);
        mPayed = (CheckBox) findViewById(R.id.checkbox);

        mBarcodeEdit = (EditText) findViewById(R.id.cod_barra_edit);
        mNameEdit = (EditText) findViewById(R.id.name_edit);
        mValueEdit = (EditText) findViewById(R.id.valor_edit);
        mPaymentDateEdit = (EditText) findViewById(R.id.vencimento_edit);
        mPaymentDateEdit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG_ID);
            }

        });

        Button cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }

        });

        Button barCodeBtn = (Button) findViewById(R.id.btn_cod_barra);
        barCodeBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO call bar code scanner.
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        		startActivityForResult(intent, 0);
            }

        });

        Button submitBtn = (Button) findViewById(R.id.btn_enviar);
        submitBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    sendToDB();
                } else {
                    showDialog(INCOMPLETE_FIELD_DIALOG_ID);
                }
            }

        });
    }

    /**
     * Send data to DB.
     */
    private void sendToDB() {
        Conta conta = new Conta();
        conta.setNome(mNameEdit.getText().toString());
        conta.setValor(mValueEdit.getText().toString());
        conta.setVencimento(mPaymentDateEdit.getText().toString());

        if (mBarcodeEdit.getText().toString().length() > MINIMUM_LENGTH) {
            conta.setCodigoBarra(mBarcodeEdit.getText().toString());
        }

        conta.setPago(mPayed.isChecked());

        DatabaseDelegate db = DatabaseDelegate.getInstance(getApplicationContext());
        if (db.inserir(conta) > 0) {
            Toast.makeText(this, "Dados gravados com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ocorreu um erro durante a gravação", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This method is responsible to do the fields validation
     * 
     * @return true if is valid, false otherwise.
     */
    private boolean validateFields() {

        boolean valid = true;

        mNameLabel.setSelected(false);
        if (mNameEdit.getText().toString().length() < MINIMUM_LENGTH) {
            valid = false;
            mNameLabel.setSelected(true);
        }

        mPaymentDateLabel.setSelected(false);
        if (mPaymentDateEdit.getText().toString().length() < MINIMUM_LENGTH) {
            valid = false;
            mPaymentDateLabel.setSelected(true);
        }

        mValueLabel.setSelected(false);
        if (mValueEdit.getText().toString().length() < MINIMUM_LENGTH) {
            valid = false;
            mValueLabel.setSelected(true);
        }

        return valid;
    }

    /**
     * Restore some Instances after screen orientation changed
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("log", "onRestoreInstanceState");

        mNameLabel.setSelected(savedInstanceState.getBoolean(NAME_LABEL));
        mPaymentDateLabel.setSelected(savedInstanceState.getBoolean(PAYMENT_DATE_LABEL));
        mValueLabel.setSelected(savedInstanceState.getBoolean(VALUE_LABEL));

        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Save some Instances when screen orientation changes
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("log", "onSaveInstanceState");

        outState.putBoolean(NAME_LABEL, mNameLabel.isSelected());
        outState.putBoolean(PAYMENT_DATE_LABEL, mPaymentDateLabel.isSelected());
        outState.putBoolean(VALUE_LABEL, mValueLabel.isSelected());

        super.onSaveInstanceState(outState);
    }

    /**
     * Creating Dialogs
     */
    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);

        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);

        case INCOMPLETE_FIELD_DIALOG_ID:
            builder.setTitle(R.string.app_name).setMessage(R.string.preencha_campos);
            builder.setNeutralButton(R.string.ok_txt, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

            });
            dialog = builder.create();
        }
        return dialog;
    }

    /**
     * Preparing Dialogs
     */
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
        case DATE_DIALOG_ID:
            ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
            break;
        }
    }

    /**
     * DatePicker Listener to update the Payment date field
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            mPaymentDateEdit.setText(mDay + "/" + mMonth + "/" + mYear);
        }
    };
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		
    	if (requestCode == 0) {
    		if (resultCode == RESULT_OK) {
    			String contents = intent.getStringExtra("SCAN_RESULT");
    			String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
    			
    			mBarcodeEdit.setText(contents);   			
    		}
    	}
    }

}
