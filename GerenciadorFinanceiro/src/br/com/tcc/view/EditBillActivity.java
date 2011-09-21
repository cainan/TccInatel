package br.com.tcc.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import br.com.tcc.R;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;

public class EditBillActivity extends BillActivity {

    /** Bundle parameter to read the bill received */
    public static final String BILL_PARAM = "bill";

    /** Hold an instance of Bill */
    private Conta mBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent itt = getIntent();
        if (itt != null) {
            mBill = (Conta) itt.getExtras().get(BILL_PARAM);
        }

        fillFields();
    }

    /**
     * FulFill the fields with the bill to be edited
     */
    private void fillFields() {

        Button sendButton = (Button) findViewById(R.id.btn_enviar);
        if (sendButton != null) {
            sendButton.setText(getApplicationContext().getResources().getString(R.string.alterar));
        }

        if (mBill.getNotificar() != null) {
            mNotifyEdit.setText(mBill.getNotificar());
            String[] time = mBill.getNotificar().split(":");
            mHour = Integer.parseInt(time[0]);
            mMinute = Integer.parseInt(time[1]);
        }

        if (mBill.getCodigoBarra() != null) {
            mBarcodeEdit.setText(mBill.getCodigoBarra());
        }

        if (mBill.getValor() != null) {
            mValueEdit.setText(mBill.getValor());
        }

        if (mBill.getNome() != null) {
            mNameEdit.setText(mBill.getNome());
        }

        if (mBill.getVencimento() != null) {
            mPaymentDateEdit.setText(mBill.getVencimento());
            String[] date = mBill.getVencimento().split("/");
            mDay = Integer.parseInt(date[0]);
            mMonth = Integer.parseInt(date[1]) - 1;
            mYear = Integer.parseInt(date[2]);
        }

        if (mBill.isPago()) {
            mPayed.setChecked(true);
        } else {
            mPayed.setChecked(false);
        }

    }

    /**
     * Sending the edited bill to Database. Must add the id of the bill we received in this new
     * object to make the update.
     */
    @Override
    void sendToDB(Conta bill) {
        // putting the bill id in the new object to make the update
        bill.setId(mBill.getId());

        DatabaseDelegate db = DatabaseDelegate.getInstance(getApplicationContext());
        if (db.editBillById(bill) > 0) {
            Toast.makeText(this, "Dados gravados com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ocorreu um erro durante a gravação", Toast.LENGTH_SHORT).show();
        }
    }

}
