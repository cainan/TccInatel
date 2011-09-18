package br.com.tcc.Database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.tcc.Model.Conta;

public class DatabaseDelegate {

    /** Hold the Database's name */
    private static final String DB_NAME = "Gerenciador_Financeiro";

    /** Hold the Database's version */
    private static final int DB_VERSION = 1;

    /** Hold the table's name */
    private static final String TABLE_NAME = "Contas";

    /** Hold the script to delete the database */
    private static final String SCRIPT_DB_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /** Hold the script to create the database */
    private static final String SCRIPT_DB_CREATE = "create table " + TABLE_NAME
            + "( _id integer primary key autoincrement," + "conta text not null,"
            + "valor text not null," + "vencimento text not null," + "notificar text not null,"
            + "codigo text," + "status integer not null);";

    /** Hold an instance of SQLiteHelper */
    private SQLiteHelper mDatabaseHelper;

    /** Hold the data base while open */
    private SQLiteDatabase mDataBase;

    /** Static instance to connect to database */
    private static DatabaseDelegate mDatabaseDelegate;

    /**
     * Get a instance of database
     * 
     * @param Context
     */
    public static DatabaseDelegate getInstance(Context applicationContext) {
        if (mDatabaseDelegate == null) {
            mDatabaseDelegate = new DatabaseDelegate(applicationContext);
        }
        return mDatabaseDelegate;
    }

    /**
     * Create a new database if it doesn't exist.
     * 
     * @param Context
     */
    public DatabaseDelegate(Context ctx) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new SQLiteHelper(ctx, DB_NAME, DB_VERSION, SCRIPT_DB_CREATE,
                    SCRIPT_DB_DELETE);

        }
    }

    /**
     * Close database
     */
    public void closeDb() {
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
        }
    }

    /**
     * Insert a new bill in database
     * 
     * @param Conta
     */
    public synchronized long inserir(Conta c) {

        Long success;

        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put("conta", c.getNome());
        cv.put("valor", c.getValor());
        cv.put("vencimento", c.getVencimento());
        cv.put("notificar", c.getNotificar());
        cv.put("codigo", c.getCodigoBarra());
        cv.put("status", c.isPago());
        cv.put("notificar", c.getNotificar());

        success = mDataBase.insert(TABLE_NAME, null, cv);

        // Close database
        closeDb();

        return success;
    }

    /**
     * Read bills to pay from database
     * 
     * @return Conta
     */
    public synchronized ArrayList<Conta> ReadBillToPay() {
        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ArrayList<Conta> arrayConta = new ArrayList<Conta>();
        Conta conta;

        String[] allColumns = { "_id", "conta", "valor", "vencimento", "notificar", "codigo",
                "status" };

        // SELECT * FROM CONTAS WHERE STATUS = 0
        Cursor cursor = mDataBase.query(TABLE_NAME, allColumns, "status = 0", null, null, null,
                null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                conta = new Conta();
                conta.setId(cursor.getInt(0));
                conta.setNome(cursor.getString(1));
                conta.setValor(cursor.getString(2));
                conta.setVencimento(cursor.getString(3));
                conta.setNotificar(cursor.getString(4));
                conta.setCodigoBarra(cursor.getString(5));
                if (cursor.getInt(6) == 0) {
                    conta.setPago(false);
                } else {
                    conta.setPago(false);
                }

                arrayConta.add(conta);
                cursor.moveToNext();
            }
        }

        // Open Database
        closeDb();

        return arrayConta;
    }
}
