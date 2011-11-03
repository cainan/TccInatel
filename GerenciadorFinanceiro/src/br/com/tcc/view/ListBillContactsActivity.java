package br.com.tcc.view;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import br.com.tcc.R;
import br.com.tcc.adapter.ListBillContactsAdapter;
import br.com.tcc.model.Contacts;
import br.com.tcc.model.database.DatabaseDelegate;

public class ListBillContactsActivity extends BaseActivity {

    /** Hold the ListView */
    private ListView mListViewContacts;
    
    /** Hold the ListAdapter */
    public ListBillContactsAdapter mListContactAdapter;
    
    /** Hold the Contacts List */
    private List<Contacts> mContacts = new ArrayList<Contacts>();

    /** Hold the Empty message */
    private TextView mEmptyListContacts;
    
    /** Hold the Email Contacts Selected */
    private String emailContactsSelected;
    
    /** Hold the Phone Number Contacts Selected */
    private String phoneContactsSelected;
    
    /** Hold the Names Contacts Selected */
    private String nameContactsSelected;
    
    /** Hold the Bills Selected */
    private String billsSelected;

    /** Hold the Button */
    private Button mButton;
    
    /** Hold the Database Instance */
    private DatabaseDelegate mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_contacts_bill);

        mListViewContacts = (ListView) findViewById(R.id.listview_contact);
        mEmptyListContacts = (TextView) findViewById(R.id.empty_list_contact);
        mButton = (Button) findViewById(R.id.button_contact);
        
        Intent i = getIntent();
        billsSelected = i.getStringExtra("bills");
        
        mContacts = getListContacts();
        
        mButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (mListContactAdapter.getListContactSelected() != null && mListContactAdapter.getListContactSelected().size() > 0) {
					emailContactsSelected = getEmails(mListContactAdapter.getListContactSelected());
					phoneContactsSelected = getPhoneNumbers(mListContactAdapter.getListContactSelected());
					nameContactsSelected = getNames(mListContactAdapter.getListContactSelected());
					
					Intent i = new Intent();
					i.putExtra("bills", billsSelected);
					i.putExtra("names", nameContactsSelected);
					i.putExtra("emails", emailContactsSelected);
					i.putExtra("phones", phoneContactsSelected);
					i.setClass(getApplicationContext(), SendNotification.class);
					startActivity(i);
				}
			}
		});
        

        if (mContacts.size() > 0) {
            initView();
        } else {
            emptyList();
        }
    }

    /**
     * Show a message telling that there is not bills registered
     */
    private void emptyList() {
        if (mListViewContacts != null) {
            mListViewContacts.setVisibility(View.GONE);
        }

        if (mEmptyListContacts != null) {
            mEmptyListContacts.setVisibility(View.VISIBLE);
        }

        if (mButton != null) {
            mButton.setVisibility(View.GONE);
        }
    }

    /**
     * Initializing the view
     */
    private void initView() {

        if (mListViewContacts != null) {
            mListViewContacts.setVisibility(View.VISIBLE);
        }

        if (mEmptyListContacts != null) {
            mEmptyListContacts.setVisibility(View.GONE);
        }
        
        if (mButton != null) {
            mButton.setVisibility(View.VISIBLE);
        }

        mListContactAdapter = new ListBillContactsAdapter(getApplicationContext(), mContacts);
		mListViewContacts.setAdapter(mListContactAdapter);       

        setListAnimation();
    }

    /**
     * Create a cascade animation when showing the list
     */
    private void setListAnimation() {

        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(110);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(160);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        mListViewContacts.setLayoutAnimation(controller);

    }  
    
    /**
     * Read to update the listView
     */
    protected void updateList() {
        updateData();
        mListContactAdapter.updateAdapter(mContacts);
    }

    /**
     * Read the database to get the updated info
     */
    private void updateData() {
        mContacts = getListContacts();
    }

    
    /**
     * Call clear list
     */
    @Override
    protected void onResume() {
    	if (mContacts.size() > 0) {
            updateList();
        }
    	if (mListContactAdapter.getListContactSelected() != null && mListContactAdapter.getListContactSelected().size() > 0) {
        	mListContactAdapter.getListContactSelected().clear();
        }
        super.onResume();
    }

    /**
     * Return list of contacts
     */
    private List<Contacts> getListContacts() {

		List<Contacts> listContacts = new ArrayList<Contacts>();

		//  Find contact based on name.
		ContentResolver cr = getContentResolver();
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
		Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, selection, null, null);

		while (cursor.moveToNext()) {

			Contacts contact = new Contacts();
			String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			//seta id
			contact.setId(contactId);

			String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//seta o nome do contato
			contact.setName(contactName);

			//  Get all phone numbers.
			Cursor phones = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId, null, null);

			if (phones != null && phones.getCount() > 0) {
				String number = "";
				
				while (phones.moveToNext()) {
					int type = phones.getInt(phones.getColumnIndex(Phone.TYPE));
					switch (type) {
					case Phone.TYPE_MOBILE:
						number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
						contact.setPhoneNumber(number);
						break;
					}
				}
				if (number == null || number.equals("")) {
					phones.moveToFirst();
					number = phones.getString(phones.getColumnIndex(Phone.NUMBER));
					contact.setPhoneNumber(number);
				}
			}
			phones.close();

			//  Get all email addresses.
			Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId, null, null);

			if(emails != null && emails.getCount() > 0) {
				emails.moveToFirst();
				String email = emails.getString(emails.getColumnIndex(Email.DATA));
				if (email != null && !email.equals("")) {
					contact.setEmail(email);
				}
			}
			emails.close();
			contact.setMarked(false);
			listContacts.add(contact);
		}
		cursor.close();
		return listContacts;
	}
    
    private String getEmails(List<Contacts> contacts) {
		StringBuffer sb = new StringBuffer();
		for (Contacts c : contacts) {
			if (c.getEmail() != null && !c.getEmail().trim().equals("")) {
				sb.append(c.getEmail() + ",");
			}
		}
		if (sb != null && sb.length() > 0) {
			String emails = sb.toString();
			return emails.substring(0, emails.length() - 1);
		}
		else {
			return "";
		}
	}
	
	private String getPhoneNumbers(List<Contacts> contacts) {
		StringBuffer sb = new StringBuffer();
		for (Contacts c : contacts) {
			if (c.getPhoneNumber() != null && !c.getPhoneNumber().trim().equals("")) {
				sb.append(c.getPhoneNumber() + ",");
			}
		}
		if (sb != null && sb.length() > 0) {
			String phones = sb.toString();
			return phones.substring(0, phones.length() - 1);
		}
		else {
			return "";
		}
	}
	
	private String getNames(List<Contacts> contacts) {
		StringBuffer sb = new StringBuffer();
		for (Contacts c : contacts) {
			if (c.getName() != null && !c.getName().trim().equals("")) {
				sb.append(c.getName() + ",");
			}
		}
		if (sb != null && sb.length() > 0) {
			String names = sb.toString();
			return names.substring(0, names.length() - 1);
		}
		else {
			return "";
		}
	}
}
