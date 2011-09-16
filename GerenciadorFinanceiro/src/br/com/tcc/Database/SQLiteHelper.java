package br.com.tcc.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteHelper extends SQLiteOpenHelper {
    private String scriptCreate;
    private String scriptDelete;

    public SQLiteHelper(Context ctx, String nomeBd, int versaoBanco, String scriptCreate,
            String scriptDelete) {

        super(ctx, nomeBd, null, versaoBanco);
        this.scriptCreate = scriptCreate;
        this.scriptDelete = scriptDelete;
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d("log", "creating DB");
        db.execSQL(scriptCreate);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("log", "upgrading DB");
        db.execSQL(scriptDelete);
        onCreate(db);
    }
}