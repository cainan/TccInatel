package br.com.tcc.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.tcc.R;
import br.com.tcc.model.Conta;

public class GalleryAdapter extends BaseAdapter {

    /** Hold the Layout inflater */
    private LayoutInflater mInflater = null;

    /** Hold an array of Bills to be shown in gallery */
    private ArrayList<Conta> mBills;

    public GalleryAdapter(Context c, ArrayList<Conta> bills) {
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mBills = bills;
    }

    public int getCount() {
        return mBills.size();
    }

    public Object getItem(int position) {
        return mBills.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout rowLayout = (RelativeLayout) mInflater.inflate(R.layout.gallery_item,
                parent, false);

        TextView name = (TextView) rowLayout.findViewById(R.id.name);
        TextView date = (TextView) rowLayout.findViewById(R.id.day_date);
        TextView value = (TextView) rowLayout.findViewById(R.id.value);

        if (name != null) {
            name.setText(mBills.get(position).getNome());
        }

        if (date != null) {
            date.setText(mBills.get(position).getVencimento());
        }
        
        if (value != null) {
            value.setText("R$ " + mBills.get(position).getValor());
        }

        return rowLayout;
    }
}
