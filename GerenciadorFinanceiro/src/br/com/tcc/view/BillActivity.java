package br.com.tcc.view;

import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.TimePicker;
import br.com.tcc.R;
import br.com.tcc.model.Conta;

public abstract class BillActivity extends BaseActivity {

    /** Bundle fields that save is the labels are selected */
    private static final String NAME_LABEL = "nameLabel";
    private static final String PAYMENT_DATE_LABEL = "paymentDateLabel";
    private static final String VALUE_LABEL = "valueLabel";
    private static final String NOTIFY_LABEL = "notifyLabel";

    /** The minimum length in a field */
    protected static final int MINIMUM_LENGTH = 2;

    /** Dialogs ID */
    private static final int DATE_DIALOG_ID = 0;
    private static final int TIME_DIALOG_ID = 1;
    private static final int INCOMPLETE_FIELD_DIALOG_ID = 2;
    private static final int BARCODE_SCANNER_UNAVAILABLE = 3;

    /** Barcode scanner informations */
    private static final String BARCODE_SCANNER_LAUNCHER = "com.google.zxing.client.android.SCAN";
    private static final String BARCODE_SCANNER_PACKAGE = "com.google.zxing.client.android";

    /** Hold the hour */
    protected int mHour;

    /** Hold the minute */
    protected int mMinute;

    /** Hold the year */
    protected int mYear;

    /** Hold the month */
    protected int mMonth;

    /** Hold the day */
    protected int mDay;

    /** Hold the notify field */
    protected EditText mNotifyEdit;

    /** Hold the barcode field */
    protected EditText mBarcodeEdit;

    /** Hold the Bill's value field */
    protected EditText mValueEdit;

    /** Hold the Bill's name field */
    protected EditText mNameEdit;

    /** Hold the payment date field */
    protected EditText mPaymentDateEdit;

    /** Hold the Notify label */
    private TextView mNotifyLabel;

    /** Hold the Bill's name label */
    private TextView mNameLabel;

    /** Hold the payment date label */
    private TextView mPaymentDateLabel;

    /** Hold the Bill's value label */
    private TextView mValueLabel;

    /** Hold the check box value */
    protected CheckBox mPayed;

    /** Hold an instance of calendar */
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setContentView(R.layout.register_bill);

        initView();

        calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

    }

    /**
     * Initializing the view
     */
    private void initView() {

        mNameLabel = (TextView) findViewById(R.id.name_label);
        mPaymentDateLabel = (TextView) findViewById(R.id.vencimento_label);
        mValueLabel = (TextView) findViewById(R.id.valor_label);
        mPayed = (CheckBox) findViewById(R.id.checkbox);
        mNotifyLabel = (TextView) findViewById(R.id.notificar_label);

        mBarcodeEdit = (EditText) findViewById(R.id.cod_barra_edit);
        mNameEdit = (EditText) findViewById(R.id.name_edit);
        mValueEdit = (EditText) findViewById(R.id.valor_edit);

        mNotifyEdit = (EditText) findViewById(R.id.notificar_edit);
        mNotifyEdit.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                showDialog(TIME_DIALOG_ID);
            }

        });

        mPaymentDateEdit = (EditText) findViewById(R.id.vencimento_edit);
        mPaymentDateEdit.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                showDialog(DATE_DIALOG_ID);
            }

        });

        Button cancelBtn = (Button) findViewById(R.id.btn_cancel);
        cancelBtn.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                finish();
            }

        });

        Button barCodeBtn = (Button) findViewById(R.id.btn_cod_barra);
        barCodeBtn.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View arg0) {
                callBarcodeScanner();
            }

        });

        Button submitBtn = (Button) findViewById(R.id.btn_enviar);
        submitBtn.setOnClickListener(new OnClickListener() {

            //@Override
            public void onClick(View v) {
                submitAction();
            }

        });
    }

    /**
     * Action made after a submit button be pressed
     */
    protected void submitAction() {
        if (validateFields()) {
            sendToDB(makeBillObject());
        } else {
            showDialog(INCOMPLETE_FIELD_DIALOG_ID);
        }
    }

    /**
     * Test if Barcode scanner is already installed in the phone, if it is, launch it. If not, ask
     * the user to install it.
     */
    private void callBarcodeScanner() {
        boolean isInstalled = false;

        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> packages = packageManager
                .getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(BARCODE_SCANNER_PACKAGE)) {
                isInstalled = true;
                break;
            }

        }

        if (isInstalled) {
            Intent intent = new Intent(BARCODE_SCANNER_LAUNCHER);
            startActivityForResult(intent, 0);
        } else {
            showDialog(BARCODE_SCANNER_UNAVAILABLE);
        }

    }

    /**
     * Make a Bill object from the fields at screen
     */
    private Conta makeBillObject() {
        Conta conta = new Conta();
        conta.setNome(mNameEdit.getText().toString());
        conta.setValor(mValueEdit.getText().toString());
        conta.setVencimento(mPaymentDateEdit.getText().toString());
        conta.setNotificar(mNotifyEdit.getText().toString());

        if (mBarcodeEdit.getText().toString().length() > MINIMUM_LENGTH) {
            conta.setCodigoBarra(mBarcodeEdit.getText().toString());
        }

        conta.setPago(mPayed.isChecked());

        return conta;
    }

    /**
     * Send data to DB.
     */
    abstract void sendToDB(Conta bill);

    /**
     * This method is responsible to do the fields validation
     * 
     * @return true if is valid, false otherwise.
     */
    protected boolean validateFields() {

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

        mNotifyLabel.setSelected(false);
        if (mNotifyEdit.getText().toString().length() < MINIMUM_LENGTH) {
            valid = false;
            mNotifyLabel.setSelected(true);
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
        mNotifyLabel.setSelected(savedInstanceState.getBoolean(NOTIFY_LABEL));

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
        outState.putBoolean(NOTIFY_LABEL, mNotifyLabel.isSelected());

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

        case TIME_DIALOG_ID:
            return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);

        case INCOMPLETE_FIELD_DIALOG_ID:
            builder.setTitle(R.string.app_name).setMessage(R.string.preencha_campos);
            builder.setNeutralButton(R.string.ok_txt, new DialogInterface.OnClickListener() {

                //@Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }

            });
            dialog = builder.create();
            break;
        case BARCODE_SCANNER_UNAVAILABLE:
            builder.setTitle(R.string.app_name).setMessage(R.string.barcode_scanner_unavailable);
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {

                //@Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
            builder.setPositiveButton(R.string.instalar, new DialogInterface.OnClickListener() {

                //@Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW).setData(Uri
                            .parse("market://details?id=com.google.zxing.client.android"));
                    startActivity(goToMarket);
                }

            });

            dialog = builder.create();
            break;
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
        case TIME_DIALOG_ID:
            ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
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

            // Month + 1 because GregorianCalendar goes from 0 to 11
            int realMonth = mMonth + 1;

            mPaymentDateEdit.setText(String.format("%02d", mDay) + "/"
                    + String.format("%02d", realMonth) + "/" + String.format("%04d", mYear));
        }
    };

    /**
     * TimePicker Listener to update the Payment notify field
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        //@Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
            mNotifyEdit
                    .setText(String.format("%02d", mHour) + ":" + String.format("%02d", mMinute));
        }

    };

    /**
     * Wait for bar code scanner answer
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            String contents = intent.getStringExtra("SCAN_RESULT");
            mBarcodeEdit.setText(contents);
        }

    }
}
