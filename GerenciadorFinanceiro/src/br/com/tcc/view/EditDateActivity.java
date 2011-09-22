package br.com.tcc.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import br.com.tcc.R;

public class EditDateActivity extends EditBillActivity {

    /** Parameter used to send the result to the activity */
    public static final String RESULT_PARAM = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        blockFields();

    }

    /**
     * Need to return data to the this activity caller, so the method was overwritten
     */
    @Override
    protected void submitAction() {
        if (mPaymentDateEdit != null) {
            mBill.setVencimento(mPaymentDateEdit.getText().toString());
        }

        if (mNotifyEdit != null) {
            mBill.setNotificar(mNotifyEdit.getText().toString());
        }

        Intent itt = new Intent();
        itt.putExtra(RESULT_PARAM, mBill);
        setResult(0, itt);

        super.submitAction();
    }

    /**
     * When the user select the option "Remarcar Data", some fields are blocked to not be edited.
     */
    private void blockFields() {
        mNameEdit.setEnabled(false);
        mNameEdit.setFocusable(false);

        mValueEdit.setEnabled(false);
        mValueEdit.setFocusable(false);

        mBarcodeEdit.setEnabled(false);
        mBarcodeEdit.setFocusable(false);

        mPayed.setEnabled(false);
        Button barcodeBtn = (Button) findViewById(R.id.btn_cod_barra);
        if (barcodeBtn != null) {
            barcodeBtn.setEnabled(false);
        }
    }

}
