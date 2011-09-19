package br.com.tcc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.tcc.R;
import br.com.tcc.model.Conta;

public class ListBillAdapter extends BaseAdapter {

    /** Hold the Application Context */
    private Context mContext;

    /** Hold an array of Bills to be shown in listView */
    private ArrayList<Conta> mBills;

    /**
     * The class' constructor
     * 
     * @param applicationContext
     * @param bills
     */
    public ListBillAdapter(Context applicationContext, ArrayList<Conta> bills) {
        mContext = applicationContext;
        mBills = bills;
    }

    @Override
    public int getCount() {
        return mBills.size();
    }

    @Override
    public Object getItem(int position) {
        return mBills.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LinearLayout rowLayout = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.list_bill_item, parent, false);

        if (mBills != null && mBills.get(position) != null) {

            // Set list item according to product item data
            TextView billName = (TextView) rowLayout.findViewById(R.id.bill_name);
            if (billName != null) {
                billName.setText(mBills.get(position).getNome());
            }

            TextView billDeadline = (TextView) rowLayout.findViewById(R.id.bill_deadline);
            if (billDeadline != null) {
                billDeadline.setText(mBills.get(position).getVencimento());
            }

            TextView billValue = (TextView) rowLayout.findViewById(R.id.bill_value);
            if (billValue != null) {
                billValue.setText(mBills.get(position).getValor());
            }

        }

        return rowLayout;
    }

}
