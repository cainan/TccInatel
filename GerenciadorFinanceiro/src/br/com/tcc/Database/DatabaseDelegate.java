package br.com.tcc.Database;

import br.com.tcc.Model.Conta;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseDelegate {
	
	private SQLiteDatabase dbGerFinanceiro;  
	
	private static final String SCRIPT_DB_DELETE = "DROP TABLE IF EXISTS contas";  
    private static final String SCRIPT_DB_CREATE = "create table contas ( );";  
  
    public DatabaseDelegate (Context ctx){  
        SQLiteHelper dbHelper = new SQLiteHelper(ctx, "dbGerFinanceiro", 1, SCRIPT_DB_CREATE, SCRIPT_DB_DELETE);  
        dbGerFinanceiro = dbHelper.getWritableDatabase();  
    }  
  
	private long inserir(Conta c){  
    	ContentValues cv = new ContentValues();  
        cv.put("nome", c.getNome());  
        cv.put("valor", c.getValor());  
        cv.put("vencimento", c.getVencimento());
        cv.put("codigoBarras", c.getCodigoBarra());
        
        return dbGerFinanceiro.insert("contas", null, cv);
    }  
  
//    private long atualizar(Conta bill){  
//
//    }  
//  
//    public int excluir(int id){  
//          
//    }  
}
