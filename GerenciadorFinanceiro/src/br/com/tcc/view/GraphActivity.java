package br.com.tcc.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;

import com.java4less.rchart.Chart;
import com.java4less.rchart.ChartLoader;
import com.java4less.rchart.android.ChartPanel;
import com.java4less.rchart.gc.android.ChartAndroidImage;

public class GraphActivity extends Activity {

    /** Hold the chart */
    private ChartLoader mChart = new ChartLoader();

    /** Hold the chart panel */
    private ChartPanel mChartPanel;

    /** Hold the payed bills to plot on chart */
    private ArrayList<Conta> mPayedBills;

    /** Hold the bills to pay to plot on chart */
    private ArrayList<Conta> mBillsToPay;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        getBillToPlot();

        mChart = new ChartLoader();
        // this is to be able to load the images
        ChartAndroidImage.assets = getAssets();

        String chartfile = this.getIntent().getStringExtra("com.java4less.rchart.samples.file");

        // setContentView(R.layout.main);
        mChartPanel = new ChartPanel(this);

        try {
            mChartPanel.setChart(loadFromAsset(chartfile));
        } catch (Exception e) {
            Log.e(ChartPanel.TAG, "Could not lod chart from file.", e);
        }

        setContentView(mChartPanel);
    }

    /**
     * Get the bills of the current month
     */
    private void getBillToPlot() {
        // Getting current date
        Date date = new Date();
        int month = date.getMonth() + 1;
        int year = date.getYear() + 1900;

        DatabaseDelegate db = DatabaseDelegate.getInstance(getApplicationContext());
        mBillsToPay = db.readMonthlyBills(month, year, false);
        mPayedBills = db.readMonthlyBills(month, year, true);

    }

    protected void onDestroy() {

        if (mChartPanel != null)
            if (mChartPanel.getChart() != null)
                mChartPanel.getChart().stopUpdater();

        super.onDestroy();
    }

    /**
     * load chart definition from file
     * 
     * @param name
     * @return
     */
    public Chart loadFromAsset(String name) throws IOException {

        InputStream is = getAssets().open(name);

        mChart.loadFromFile(is, true);

        Log.i(ChartPanel.TAG, "Building chart ...");

        Chart c = mChart.build(false, false);
        c.setWidth(850);
        c.setHeight(450);

        if (name.equals("barChart3D.txt")) {
            updateBarValues(c);
        } else {
            updatePieValues(c);
        }

        Log.i(ChartPanel.TAG, "Build ok");

        return c;
    }

    private void updatePieValues(Chart c) {

        if (mBillsToPay != null) {
            float totalToPay = 0;
            for (Conta bill : mBillsToPay) {
                totalToPay += Float.parseFloat(bill.getValor());
            }
            c.plotters[0].getSerie(0).replaceYValueAt(0, totalToPay);
        }

        if (mPayedBills != null) {
            float totalPayed = 0;
            for (Conta bill : mPayedBills) {
                totalPayed += Float.parseFloat(bill.getValor());
            }
            c.plotters[0].getSerie(0).replaceYValueAt(1, totalPayed);
        }

    }

    /**
     * Update the values of Bar Chart
     * 
     * @param c
     */
    private void updateBarValues(Chart c) {

        c.plotters[0].getSerie(0).replaceYValueAt(0, 0);

        // Using "serie0" to payed bills
        if (mPayedBills != null) {

            for (Conta bill : mPayedBills) {
                int day = Integer.parseInt(bill.getDia());
                float value = Float.parseFloat(bill.getValor());
                c.plotters[0].getSerie(0).replaceYValueAt(day, value);
            }

        }

        // Using "serie1" to bills to pay
        // if (mBillsToPay != null) {
        //
        // for (Conta bill : mBillsToPay) {
        // // c.plotters[0].getSerie(0).replaceYValueAt(1, 21);
        // }
        //
        // }
        // do not allow the chart to be rebuilt from the original parameters
        c.autoRebuild = false;
    }

}
