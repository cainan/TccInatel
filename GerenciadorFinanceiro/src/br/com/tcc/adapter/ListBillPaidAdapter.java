package br.com.tcc.adapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import br.com.tcc.R;
import br.com.tcc.model.Conta;

public class ListBillPaidAdapter extends BaseAdapter {

    /** Hold the Application Context */
    private Context mContext;

    /** Hold an array of Bills to be shown in listView */
    private ArrayList<Conta> mBillsPaid;
    
    private Conta mBillSelected;
    
    private List<Conta> listBillSelected = new ArrayList<Conta>();
	

    /**
     * The class' constructor
     * 
     * @param applicationContext
     * @param bills
     */
    public ListBillPaidAdapter(Context applicationContext, ArrayList<Conta> bills) {
        mContext = applicationContext;
        mBillsPaid = bills;
    }

    //@Override
    public int getCount() {
        return mBillsPaid.size();
    }

    //@Override
    public Object getItem(int position) {
        return mBillsPaid.get(position);
    }

    //@Override
    public long getItemId(int position) {
        return position;
    }

   //@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        RelativeLayout rowLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
                R.layout.list_bill_paid_item, parent, false);

        if (mBillsPaid != null && mBillsPaid.get(position) != null) {

            // Set list item according to product item data
            TextView billName = (TextView) rowLayout.findViewById(R.id.bill_name_paid);
            if (billName != null) {
                billName.setText(mBillsPaid.get(position).getNome());
            }

            TextView billDeadline = (TextView) rowLayout.findViewById(R.id.bill_deadline_paid);
            if (billDeadline != null) {
                billDeadline.setText(mBillsPaid.get(position).getVencimento());
            }

            TextView billValue = (TextView) rowLayout.findViewById(R.id.bill_value_paid);
            if (billValue != null) {
            	float valor = Float.parseFloat(mBillsPaid.get(position).getValor());
            	NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                billValue.setText(nf.format(valor));
            }
           
           
            final CheckBox check = (CheckBox) rowLayout.findViewById(R.id.checkbox_paid);
            
            
            if (check != null && mBillsPaid.get(position) != null) {
                check.setChecked(false);
            }
            
            final int pos = position;
            
            check.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					mBillSelected = mBillsPaid.get(pos);
					if (check.isChecked()) {
						listBillSelected.add(mBillSelected);
						setListBillSelected(listBillSelected);
					}
					else {
						listBillSelected.remove(listBillSelected.indexOf(mBillSelected));
						setListBillSelected(listBillSelected);
					}
				}
			});
        }

        return rowLayout;
    }

    /**
     * Update Bills instace to update the listView
     * 
     * @param bills
     */
    public void updateAdapter(ArrayList<Conta> bills) {
        if (mBillsPaid != null && bills != null) {
            mBillsPaid.clear();
            mBillsPaid = bills;
        }
        notifyDataSetChanged();
    }

	public List<Conta> getListBillSelected() {
		return listBillSelected;
	}

	public void setListBillSelected(List<Conta> listBillSelected) {
		this.listBillSelected = listBillSelected;
	}
    
	
}
