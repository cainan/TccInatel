package br.com.tcc.adapter;

import java.util.ArrayList;
import java.util.List;

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
import br.com.tcc.model.Contacts;

public class ListBillContactsAdapter extends BaseAdapter {

	/** Hold the Application Context */
	private Context mContext;

	/** Hold an array of Contacts to be shown in listView */
	private List<Contacts> mContacts = new ArrayList<Contacts>();

	private Contacts mContactSelected;

	private List<Contacts> listContactSelected = new ArrayList<Contacts>();

	/**
	 * The class' constructor
	 * 
	 * @param applicationContext
	 * @param contacts
	 */
	public ListBillContactsAdapter(Context applicationContext, List<Contacts> contacts) {
		mContext = applicationContext;
		mContacts = contacts;
	}

	//@Override
	public int getCount() {
		return mContacts.size();
	}

	//@Override
	public Object getItem(int position) {
		return mContacts.get(position);
	}

	//@Override
	public long getItemId(int position) {
		return position;
	}

	public List<Contacts> getListContactSelected() {
		return listContactSelected;
	}

	public void setListContactSelected(List<Contacts> listContactSelected) {
		this.listContactSelected = listContactSelected;
	}

	//@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RelativeLayout rowLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				R.layout.list_contacts_bill_item, parent, false);

		if (mContacts != null && mContacts.get(position) != null) {

			// Set list item according to product item data
			TextView contactName = (TextView) rowLayout.findViewById(R.id.bill_name_contact);
			if (contactName != null) {
				contactName.setText(mContacts.get(position).getName());
			}          

			final CheckBox check = (CheckBox) rowLayout.findViewById(R.id.checkbox_contact);

			if (check != null && mContacts.get(position) != null) {
				check.setChecked(false);
			}

			final int pos = position;

			check.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					mContactSelected = mContacts.get(pos);
					if (check.isChecked()) {
						listContactSelected.add(mContactSelected);
						setListContactSelected(listContactSelected);
					}
					else {
						listContactSelected.remove(listContactSelected.indexOf(mContactSelected));
						setListContactSelected(listContactSelected);
					}
				}
			});
		}

		return rowLayout;
	}
	
	/**
     * Update Contacts instace to update the listView
     * 
     * @param bills
     */
    public void updateAdapter(List<Contacts> contacts) {
        if (mContacts != null && contacts != null) {
            mContacts.clear();
            mContacts = contacts;
        }
        notifyDataSetChanged();
    }
}

