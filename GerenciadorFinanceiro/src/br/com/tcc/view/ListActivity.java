package br.com.tcc.view;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import br.com.tcc.R;
import br.com.tcc.adapter.ListBillAdapter;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;

public class ListActivity extends BaseActivity {

    /** Hold dialog id */
    private static final int OPTIONS_DIALOG = 1;
    private static final int REMOVE_BILL_DIALOG = 2;

    /** Hold the Bills */
    protected ArrayList<Conta> mBills;

    /** Hold The selected Bill */
    private Conta mSelectedBill;

    /** Hold the ListAdapter */
    private ListBillAdapter mListAdapter;

    /** Hold the ListView */
    private ListView mListView;

    /** Hold the Empty message */
    private TextView mEmptyList;

    /** Hold the Spinner */
    private Spinner mSpinner;

    /** Hold the Database Instance */
    private DatabaseDelegate mDatabase;

    /** Hold the position of the spinner selected item */
    private int mPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_bill);

        // getting a database instance
        mDatabase = DatabaseDelegate.getInstance(getApplicationContext());
        mBills = mDatabase.readAll();

        // getting some fields
        mListView = (ListView) findViewById(R.id.listview);
        mEmptyList = (TextView) findViewById(R.id.empty_list);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        if (mBills.size() > 0) {
            initView();
        } else {
            emptyList();
        }
    }

    /**
     * Show a message telling that there is not bills registered
     */
    private void emptyList() {
        mListView.setVisibility(View.GONE);
        mEmptyList.setVisibility(View.VISIBLE);
        mSpinner.setVisibility(View.GONE);
    }

    /**
     * Initializing the view
     */
    private void initView() {

        mListView.setVisibility(View.VISIBLE);
        mEmptyList.setVisibility(View.GONE);
        mSpinner.setVisibility(View.VISIBLE);

        mListAdapter = new ListBillAdapter(getApplicationContext(), mBills);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedBill = mBills.get(position);
                if (mSelectedBill != null) {
                    showDialog(OPTIONS_DIALOG);
                }
            }

        });

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item,
                android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setPosition(position);
                updateList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }

        });

        setListAnimation();
    }

    /**
     * Creating dialogs
     */
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        Dialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
        case OPTIONS_DIALOG:
            builder.setTitle(R.string.app_name).setMessage(R.string.option_alert);
            builder.setNeutralButton(R.string.remove_txt, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showDialog(REMOVE_BILL_DIALOG);
                }

            });

            builder.setPositiveButton(R.string.alterar, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent itt = new Intent(getApplicationContext(), EditBillActivity.class);
                    itt.putExtra(EditBillActivity.BILL_PARAM, mSelectedBill);

                    startActivity(itt);
                }

            });

            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });
            break;

        case REMOVE_BILL_DIALOG:

            builder.setTitle(R.string.app_name).setMessage(R.string.remove_bill_alert);
            builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }

            });

            builder.setPositiveButton(R.string.ok_txt, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseDelegate db = DatabaseDelegate.getInstance(getApplicationContext());
                    db.deleteBill(mSelectedBill);
                    updateList();

                }

            });

            break;
        }

        dialog = builder.create();
        return dialog;
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
        mListView.setLayoutAnimation(controller);

    }

    /**
     * Read database to update the listView
     */
    protected void updateList() {
        updateData();
        mListAdapter.updateAdapter(mBills);
    }

    /**
     * Read the database to get the updated info
     */
    private void updateData() {
        switch (getPosition()) {
        case 0:
            mBills = mDatabase.readAll();
            break;
        case 1:
            mBills = mDatabase.readBillToPay();
            break;
        }
    }

    /**
     * Call updateList
     */
    @Override
    protected void onResume() {
        if (mBills.size() > 0) {
            updateList();
        }
        super.onResume();
    }

    /**
     * Set a variable with the selected position of the spinner
     */
    private void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    /**
     * Get the position of the selected item of the spinner
     * 
     * @return position
     */
    private int getPosition() {
        return mPosition;
    }

}