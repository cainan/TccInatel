package br.com.tcc.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import br.com.tcc.adapter.ListBillPaidAdapter;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;


public class ListBillPaidActivity extends BaseActivity {

	/** Hold the Bills */
	protected ArrayList<Conta> mBillsPaid;

	/** Hold the ListAdapter */
	private ListBillPaidAdapter mListBillAdapterPaid;

	/** Hold the ListView */
	private ListView mListViewPaid;

	/** Hold the Empty message */
	private TextView mEmptyListPaid;

	/** Hold the Button */
	private Button mButton;

	/** Hold the Bills Selected */
	private String billsSelected;

	/** Hold the Database Instance */
	private DatabaseDelegate mDatabase;

	/** Hold the ProgressDialog Instance */
	ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_bill_paid);

		// getting a database instance
		mDatabase = DatabaseDelegate.getInstance(getApplicationContext());

		dialog = new ProgressDialog(this);
		//mBillsPaid = mDatabase.readBillToPay();

		// getting some fields
		mListViewPaid = (ListView) findViewById(R.id.listview_paid);
		mEmptyListPaid = (TextView) findViewById(R.id.empty_list_paid);
		mButton = (Button) findViewById(R.id.button_paid);


		mButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (mListBillAdapterPaid.getListBillSelected() != null && mListBillAdapterPaid.getListBillSelected().size() > 0) {
					billsSelected = getBills(mListBillAdapterPaid.getListBillSelected());
					Intent i = new Intent();
					i.putExtra("bills", billsSelected);
					i.setClass(getApplicationContext(), ListBillContactsActivity.class);
					startActivity(i);
				}
			}
		});

	}

	/**
	 * Show a message telling that there is not bills registered
	 */
	private void emptyList() {
		if (mListViewPaid != null) {
			mListViewPaid.setVisibility(View.GONE);
		}

		if (mEmptyListPaid != null) {
			mEmptyListPaid.setVisibility(View.VISIBLE);
		}

		if (mButton != null) {
			mButton.setVisibility(View.GONE);
		}
	}

	/**
	 * Initializing the view
	 */
	private void initView() {

		if (mListViewPaid != null) {
			mListViewPaid.setVisibility(View.VISIBLE);
		}

		if (mEmptyListPaid != null) {
			mEmptyListPaid.setVisibility(View.GONE);
		}

		if (mButton != null) {
			mButton.setVisibility(View.VISIBLE);
		}

		mListBillAdapterPaid = new ListBillPaidAdapter(getApplicationContext(), mBillsPaid);
		mListViewPaid.setAdapter(mListBillAdapterPaid);
		updateList();        

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
		mListViewPaid.setLayoutAnimation(controller);

	}

	/**
	 * Read database to update the listView
	 */
	protected void updateList() {
		updateData();
		mListBillAdapterPaid.updateAdapter(mBillsPaid);
	}

	/**
	 * Read the database to get the updated info
	 */
	private void updateData() {
		mBillsPaid = mDatabase.readBillToPay();
	}

	/**
	 * Call updateList
	 */
	@Override
	protected void onResume() {
		/*
    	if (mBillsPaid.size() > 0) {
            updateList();
        }
        if (mListBillAdapterPaid != null){
        	if (mListBillAdapterPaid.getListBillSelected() != null && mListBillAdapterPaid.getListBillSelected().size() > 0) {
            	mListBillAdapterPaid.getListBillSelected().clear();
            }
        }
		 */
		new BillsOperation().execute();
		super.onResume();
	}

	/**
	 * Return list of bills selected
	 */
	private String getBills(List<Conta> billsSelected) {
		StringBuffer sb = new StringBuffer();
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
		String billsResult = "";
		float total = 0;
		if (billsSelected != null && billsSelected.size() > 0) {
			for (Conta c : billsSelected) {
				sb.append("Conta: " + c.getNome() + "\n" + "Valor: " + nf.format(Float.parseFloat(c.getValor())) + "\n"
						+ "Vencimento: " + c.getVencimento() + "\n" + ";");
				total += Float.parseFloat(c.getValor());
			}
		}
		sb.append(total);
		billsResult = sb.toString();
		return billsResult;
	}

	private class BillsOperation extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			dialog.setMessage("Carregando contas...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			mBillsPaid = mDatabase.readBillToPay();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dialog.dismiss(); 
			if (mBillsPaid.size() > 0) {
				initView();
			} else {
				emptyList();
			}
		}
	}

}


