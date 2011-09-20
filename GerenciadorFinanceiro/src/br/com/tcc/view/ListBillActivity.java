package br.com.tcc.view;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import br.com.tcc.R;
import br.com.tcc.adapter.ListBillAdapter;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;

public class ListBillActivity extends BaseActivity {

    /** Hold the Bills */
    ArrayList<Conta> mBills;

    /** Hold the ListAdapter */
    ListBillAdapter mListAdapter;

    /** Hold the ListView */
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_bill);

        mBills = DatabaseDelegate.getInstance(getApplicationContext()).ReadAll();

        initView();
    }

    /**
     * Initializing the view
     */
    private void initView() {

        mListAdapter = new ListBillAdapter(getApplicationContext(), mBills);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Abrir uma activity para o usuario editar a conta selecionada.

            }

        });

        setListAnimation();
    }

    /**
     * Create a cascade animation when showing the list
     */
    private void setListAnimation() {

        AnimationSet set = new AnimationSet(true);

        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(50);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(100);
        set.addAnimation(animation);

        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        mListView.setLayoutAnimation(controller);

    }

}
