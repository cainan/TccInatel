package br.com.tcc.view;

import android.widget.Toast;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;

public class RegisterBillActivity extends BillActivity {

    /**
     * Just send the bill to DataBase
     */
    @Override
    void sendToDB(Conta bill) {
        DatabaseDelegate db = DatabaseDelegate.getInstance(getApplicationContext());
        if (db.addBill(bill) > 0) {
            Toast.makeText(this, "Dados gravados com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ocorreu um erro durante a gravação", Toast.LENGTH_SHORT).show();
        }

    }
}
