package br.com.tcc.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import br.com.tcc.Model.Conta;

public class DatabaseDelegate {

    private SQLiteDatabase dbGerFinanceiro;

    private static final String DB_NAME = "Gerenciador_Financeiro";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Contas";

    private static final String SCRIPT_DB_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String SCRIPT_DB_CREATE = "create table " + TABLE_NAME
            + "( _id integer primary key autoincrement," + "conta text not null,"
            + "valor text not null," + "vencimento text not null," + "codigo text,"
            + "status integer not null);";

    private SQLiteHelper mDatabaseHelper;

    private static DatabaseDelegate mDatabaseDelegate;

    public static DatabaseDelegate getInstance(Context applicationContext) {
        // TODO Auto-generated method stub
        if (mDatabaseDelegate == null) {
            mDatabaseDelegate = new DatabaseDelegate(applicationContext);
        }

        return mDatabaseDelegate;
    }

    public DatabaseDelegate(Context ctx) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new SQLiteHelper(ctx, DB_NAME, DB_VERSION, SCRIPT_DB_CREATE,
                    SCRIPT_DB_DELETE);
            dbGerFinanceiro = mDatabaseHelper.getWritableDatabase();

        }
    }

    public void closeDb() {
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
        }
    }

    public synchronized long inserir(Conta c) {
        ContentValues cv = new ContentValues();

        cv.put("conta", c.getNome());
        cv.put("valor", c.getValor());
        cv.put("vencimento", c.getVencimento());
        cv.put("codigo", c.getCodigoBarra());
        cv.put("status", c.isPago());

        Long success;
        success = dbGerFinanceiro.insert(TABLE_NAME, null, cv);

        closeDb();
        return success;
    }

    // private long atualizar(Conta bill){
    //
    // }
    //  
    // public int excluir(int id){
    //          
    // }
}
