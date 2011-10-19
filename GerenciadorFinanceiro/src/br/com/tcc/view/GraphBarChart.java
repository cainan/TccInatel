package br.com.tcc.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;
import br.com.tcc.utils.AbstractDemoChart;

public class GraphBarChart extends AbstractDemoChart{

	private int year = 0;
	
	public GraphBarChart() {
		Calendar cal = Calendar.getInstance();
		this.year = cal.get(Calendar.YEAR); 
	}
	
	/**
	   * Returns the chart name.
	   * 
	   * @return the chart name
	   */
	  public String getName() {
	    return "Contas mensais";
	  }

	  /**
	   * Returns the chart description.
	   * 
	   * @return the chart description
	   */
	  public String getDesc() {
	    return "Contas mensais de todo o ano";
	  }

	  /**
	   * Executes the chart demo.
	   * 
	   * @param context the context
	   * @return the built intent
	   */
	  public Intent execute(Context context) {
	    String[] titles = new String[] { "Contas mensais de " + year };
	    List<double[]> values = new ArrayList<double[]>();
	    double [] totalBillsMonths = getTotalBillsMonths(context); 
	    values.add(totalBillsMonths);
	    int[] colors = new int[] { Color.GREEN };
	    XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);
	    setChartSettings(renderer, "Contas Mensais", "Mêses", "Valores (R$)", 0.5,
	        12.5, 0, 2000, Color.GRAY, Color.LTGRAY);
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    //renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
	    renderer.setXLabels(12);
	    renderer.setYLabels(10);
	    renderer.setXLabelsAlign(Align.LEFT);
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(true, false);
	    // renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);
	    return ChartFactory.getBarChartIntent(context, buildBarDataset(titles, values), renderer,
	        Type.STACKED);
	  }
	  
	  private double[] getTotalBillsMonths(Context context) {
		  DatabaseDelegate mDatabase = DatabaseDelegate.getInstance(context);
		  double totalBillsMonth = 0;
		  double [] totals = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		  for (int i=0; i<12; i++) {
			  ArrayList<Conta> mBillsMonth = mDatabase.readMonthlyBills(i + 1, year, true);
			  if ((mBillsMonth != null) && (mBillsMonth.size() > 0)) {
				  for (Conta c : mBillsMonth) {
					  totalBillsMonth += Double.parseDouble(c.getValor());
				  }
				  totals[i] = totalBillsMonth;
				  totalBillsMonth = 0;
			  }
			  else {
				  totals[i] = 0;
			  }
			  
		  }
		 return totals;
	  }

}
