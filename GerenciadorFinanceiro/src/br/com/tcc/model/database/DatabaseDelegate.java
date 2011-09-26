package br.com.tcc.model.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import br.com.tcc.model.Conta;

public class DatabaseDelegate {

    /** Hold the Database's name */
    private static final String DB_NAME = "Gerenciador_Financeiro";

    /** Hold the Database's version */
    private static final int DB_VERSION = 3;

    /** Hold the table's name */
    private static final String TABLE_NAME = "Contas";

    /** Hold the script to delete the database */
    private static final String SCRIPT_DB_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /** Hold the script to create the database */
    private static final String SCRIPT_DB_CREATE = "create table " + TABLE_NAME
            + "( _id integer primary key autoincrement," + "conta text not null,"
            + "valor text not null," + "vencimento text not null," + "notificar text not null,"
            + "codigo text," + "status integer not null," + "dia integer not null,"
            + "mes integer not null," + "ano integer not null" + ");";

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
    private DatabaseDelegate(Context ctx) {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = new SQLiteHelper(ctx, DB_NAME, DB_VERSION, SCRIPT_DB_CREATE,
                    SCRIPT_DB_DELETE);

        }
    }

    /**
     * Close database
     */
    private void closeDb() {
        if (mDatabaseHelper != null) {
            mDatabaseHelper.close();
        }
    }

    /**
     * Insert a new bill in database
     * 
     * @param Conta
     */
    public synchronized long addBill(Conta bill) {

        Long success;

        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ContentValues cv = createContentValues(bill);

        success = mDataBase.insert(TABLE_NAME, null, cv);

        // Close database
        closeDb();

        return success;
    }

    /**
     * Delete a bill from Database
     * 
     * @param bill
     * @return
     */
    public synchronized int deleteBill(Conta bill) {

        int success;

        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        success = mDataBase.delete(TABLE_NAME, "_id =" + bill.getId(), null);

        // Close database
        closeDb();

        return success;
    }

    /**
     * Read bills to pay from database
     * 
     * @return Conta
     */
    public synchronized ArrayList<Conta> readBillToPay() {
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

    /**
     * Read the bills the current month
     * 
     * @param month
     * @param year
     */
    public synchronized ArrayList<Conta> readMonthlyBills(int month, int year, boolean status) {
        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ArrayList<Conta> arrayConta = new ArrayList<Conta>();
        Conta conta;

        String[] allColumns = { "_id", "conta", "valor", "vencimento", "notificar", "codigo",
                "status", "dia", "mes", "ano" };

        String where;
        if (status) {
            where = "mes=" + month + " and ano=" + year + " and status=" + "1";
        } else {
            where = "mes=" + month + " and ano=" + year + " and status=" + "0";
        }

        // SELECT * FROM CONTAS WHERE MES = ? AND ANO = ?
        Cursor cursor = mDataBase.query(TABLE_NAME, allColumns, where, null, null, null, "dia");

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
                conta.setDia(cursor.getString(7));
                conta.setMes(cursor.getString(8));
                conta.setAno(cursor.getString(9));

                arrayConta.add(conta);
                cursor.moveToNext();
            }
        }

        // Open Database
        closeDb();

        return arrayConta;

    }

    /**
     * Read the bills the current month
     * 
     * @param month
     * @param year
     */
    public synchronized ArrayList<Conta> readMonthlyBills(int week, boolean status) {
        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ArrayList<Conta> arrayConta = new ArrayList<Conta>();
        Conta conta;

        String[] allColumns = { "_id", "conta", "valor", "vencimento", "notificar", "codigo",
                "status", "dia", "mes", "ano" };

        String where;
        if (status) {
            where = "semana=" + week + " and status=" + "1";
        } else {
            where = "semana=" + week + " and status=" + "0";
        }

        // SELECT * FROM CONTAS WHERE MES = ? AND ANO = ?
        Cursor cursor = mDataBase.query(TABLE_NAME, allColumns, where, null, null, null, "dia");

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
                conta.setDia(cursor.getString(7));
                conta.setMes(cursor.getString(8));
                conta.setAno(cursor.getString(9));

                arrayConta.add(conta);
                cursor.moveToNext();
            }
        }

        // Open Database
        closeDb();

        return arrayConta;

    }

    /**
     * Read all the bills registered
     * 
     * @return Conta
     */
    public synchronized ArrayList<Conta> readAll() {
        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ArrayList<Conta> arrayConta = new ArrayList<Conta>();
        Conta conta;

        String[] allColumns = { "_id", "conta", "valor", "vencimento", "notificar", "codigo",
                "status" };

        // SELECT * FROM CONTAS
        Cursor cursor = mDataBase.query(TABLE_NAME, allColumns, null, null, null, null, null);

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
                    conta.setPago(true);
                }

                arrayConta.add(conta);
                cursor.moveToNext();
            }
        }

        // Open Database
        closeDb();

        return arrayConta;
    }

    /**
     * Mark the bill as payed
     * 
     * @param id
     */
    public synchronized void markAsPayed(int id) {
        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("status", 1);

        mDataBase.update(TABLE_NAME, cv, "_id =" + id, null);

        // Open Database
        closeDb();
    }

    /**
     * Edit a bill from data base using the id parameter
     * 
     * @param bill
     */
    public synchronized int editBillById(Conta bill) {
        // Open Database
        mDataBase = mDatabaseHelper.getWritableDatabase();

        int rowAffected;
        int id = bill.getId();

        ContentValues cv = createContentValues(bill);

        rowAffected = mDataBase.update(TABLE_NAME, cv, "_id =" + id, null);

        // Open Database
        closeDb();

        return rowAffected;
    }

    /**
     * Encapsulate a bill in ContentValues to send to database
     * 
     * @param bill
     * @return contentValues
     */
    private ContentValues createContentValues(Conta bill) {
        ContentValues cv = new ContentValues();

        cv.put("conta", bill.getNome());
        cv.put("valor", bill.getValor());
        cv.put("vencimento", bill.getVencimento());
        cv.put("notificar", bill.getNotificar());
        cv.put("codigo", bill.getCodigoBarra());
        cv.put("status", bill.isPago());
        cv.put("notificar", bill.getNotificar());

        String data[] = bill.getVencimento().split("/");
        cv.put("dia", data[0]);
        cv.put("mes", data[1]);
        cv.put("ano", data[2]);

        // cv.put("semana", value)

        return cv;
    }
}
