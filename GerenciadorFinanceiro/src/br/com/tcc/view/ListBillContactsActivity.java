package br.com.tcc.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
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

public class ListBillContactsActivity extends BaseActivity {

    /** Hold the ListView */
    private ListView mListViewContacts;

    /** Hold the ListAdapter */
    public ListBillContactsAdapter mListContactAdapter;

    /** Hold the Contacts List */
    public List<Contacts> mContacts = new ArrayList<Contacts>();

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

    /** Hold the Cancel Button */
    private Button mCancelBtn;

    /** Hold the Progress Dialog */
    private ProgressDialog mDialog;

    /** Hold the AsyncTask */
    private ContactsList mAsyncTask;

    /** Hold the option dialog id */
    private static final int OPTION_DIALOG = 1;

    /** Hold the email id */
    private static final int EMAIL = 0;

    /** Hold the sms id */
    private static final int SMS = 1;

    /** Hold the message that will be sent */
    private String mMessage;

    public static final String SUBJECT = "Notifica��o de contas";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_contacts_bill);

        mListViewContacts = (ListView) findViewById(R.id.listview_contact);
        mEmptyListContacts = (TextView) findViewById(R.id.empty_list_contact);
        mCancelBtn = (Button) findViewById(R.id.button_back);
        mButton = (Button) findViewById(R.id.button_contact);

        Intent i = getIntent();
        billsSelected = i.getStringExtra("bills");

        mCancelBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }

        });

        mButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (mListContactAdapter.getListContactSelected() != null
                        && mListContactAdapter.getListContactSelected().size() > 0) {
                    emailContactsSelected = getEmails(mListContactAdapter.getListContactSelected());
                    phoneContactsSelected = getPhoneNumbers(mListContactAdapter
                            .getListContactSelected());
                    nameContactsSelected = getNames(mListContactAdapter.getListContactSelected());

                    // Intent i = new Intent();
                    // i.putExtra("bills", billsSelected);
                    // i.putExtra("names", nameContactsSelected);
                    // i.putExtra("emails", emailContactsSelected);
                    // i.putExtra("phones", phoneContactsSelected);
                    // i.setClass(getApplicationContext(), SendNotification.class);
                    // startActivity(i);

                    showDialog(OPTION_DIALOG);
                }
            }
        });

    }

    /**
     * Starts the AsyncTask
     */
    private void launchTask() {
        mAsyncTask = new ContactsList();
        mAsyncTask.execute();
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

        if (mCancelBtn != null) {
            mCancelBtn.setVisibility(View.GONE);
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

        if (mCancelBtn != null) {
            mCancelBtn.setVisibility(View.VISIBLE);
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
     * Call clear list
     */
    @Override
    protected void onResume() {
        launchTask();

        if (mListContactAdapter != null) {
            if (mListContactAdapter.getListContactSelected() != null
                    && mListContactAdapter.getListContactSelected().size() > 0) {
                mListContactAdapter.getListContactSelected().clear();
            }
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (mDialog.isShowing()) {
            mDialog.cancel();
        }
        super.onPause();
    }

    /**
     * Return list of contacts
     */
    private List<Contacts> getListContacts() {

        List<Contacts> listContacts = new ArrayList<Contacts>();

        // Find contact based on name.
        ContentResolver cr = getContentResolver();
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
        Cursor cursor = cr
                .query(ContactsContract.Contacts.CONTENT_URI, null, selection, null, null);

        while (cursor.moveToNext()) {

            Contacts contact = new Contacts();
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // seta id
            contact.setId(contactId);

            String contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // seta o nome do contato
            contact.setName(contactName);

            // Get all phone numbers.
            Cursor phones = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = " + contactId,
                    null, null);

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

            // Get all email addresses.
            Cursor emails = cr.query(Email.CONTENT_URI, null, Email.CONTACT_ID + " = " + contactId,
                    null, null);

            if (emails != null && emails.getCount() > 0) {
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
        } else {
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
        } else {
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
        } else {
            return "";
        }
    }

    private class ContactsList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mDialog = new ProgressDialog(ListBillContactsActivity.this);
            if (mDialog != null) {
                mDialog.setCancelable(false);
                mDialog.setMessage("Carregando contatos...");
                mDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!isCancelled()) {
                mContacts = getListContacts();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
            if (mContacts.size() > 0) {
                initView();
            } else {
                emptyList();
            }

        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case (OPTION_DIALOG):
            return new AlertDialog.Builder(ListBillContactsActivity.this).setTitle(
                    R.string.enviar_por).setItems(R.array.enviar_por_item,

            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sendInfo(which);
                }

            }).create();

        }

        return null;
    }

    private void sendInfo(int which) {

        mMessage = parserMessage(billsSelected, nameContactsSelected);

        switch (which) {
        case EMAIL:
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[] { emailContactsSelected });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT);
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, mMessage);

            startActivity(Intent.createChooser(emailIntent, "Enviar e-mail usando:"));
            break;

        case SMS:
            Uri u = Uri.fromParts("sms", phoneContactsSelected, null);
            Intent sendIntent = new Intent(Intent.ACTION_VIEW, u);
            sendIntent.putExtra("sms_body", mMessage);
            startActivity(sendIntent);
            break;

        default:
            break;
        }
    }

    private String parserMessage(String message, String names) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        StringBuffer sb = new StringBuffer();
        String[] divMessage = message.split(";");
        String[] divNames = names.split(",");
        int posTotal = divMessage.length - 1;
        float total = Float.parseFloat(divMessage[posTotal]);
        sb.append("Notifica��o sobre contas\n\n");

        for (int i = 0; i < posTotal; i++) {
            sb.append(divMessage[i] + "\n");
        }

        sb.append("\nTotal: " + nf.format(total) + "\n\n");
        sb.append("Total de pessoas que ir�o pagar: " + divNames.length + " (");

        for (int i = 0; i < divNames.length; i++) {
            if (i == divNames.length - 1) {
                sb.append(divNames[i]);
            } else {
                sb.append(divNames[i] + ", ");
            }
        }

        sb.append(").\n\n");
        sb.append("Portanto, " + nf.format(total) + "/" + divNames.length + " = "
                + nf.format(total / divNames.length) + " para cada um.");
        sb.append("\n\n\nObrigado!!!");

        return sb.toString();
    }

}
