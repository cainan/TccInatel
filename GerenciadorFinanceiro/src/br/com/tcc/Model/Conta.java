package br.com.tcc.Model;

import java.io.Serializable;

import android.util.Log;

public class Conta implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String mNome;
    private String mVencimento;
    private String mValor;
    private String mCodigoBarra;
    private boolean mPago;

    public String getNome() {
        return mNome;
    }

    public void setNome(String nome) {
        mNome = nome;
    }

    public String getVencimento() {
        return mVencimento;
    }

    public void setVencimento(String vencimento) {
        mVencimento = vencimento;
    }

    public String getValor() {
        return mValor;
    }

    public void setValor(String valor) {
        mValor = valor;
    }

    public String getCodigoBarra() {
        return mCodigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        mCodigoBarra = codigoBarra;
    }

    public void setPago(boolean mPago) {
        this.mPago = mPago;
    }

    public boolean isPago() {
        return mPago;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Conta)) {
            return false;
        }

        Conta other = (Conta) obj;

        if ((other.getNome().equals(this.getNome())) && (other.getValor().equals(this.getValor()))
                && (other.getVencimento().equals(this.getVencimento()))) {
            return true;
        }

        return false;

    }

    @Override
    public int hashCode() {
        int value = 0;
        try {
            int valor = Integer.parseInt(mValor);
            String data[] = mVencimento.split("/");
            int dia = Integer.parseInt(data[0]);
            int mes = Integer.parseInt(data[1]);
            int ano = Integer.parseInt(data[2]);
            Log.d("log", "data: " + dia + mes + ano);
            // int clientId = Integer.parseInt(mClientId);
            value = (valor * dia * mes) + ano;
        } catch (Exception e) {
        }
        return value;
    }

}
