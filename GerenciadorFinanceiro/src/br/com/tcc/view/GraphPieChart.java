package br.com.tcc.view;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;
import br.com.tcc.utils.AbstractDemoChart;

public class GraphPieChart extends AbstractDemoChart{

	public String getName() {
		return "Gráfico Contas Mensais";
	}

	public String getDesc() {
		return "Gráfico com as contas pagas e não pagas do mês";
	}

	public Intent execute(Context context) {
		//double[] values = new double[] { 12, 14, 11, 10, 19 };
	    double value1 = getBillToPay(context);
	    double value2 = getBillPaid(context);
	    double[] values = new double[] { value1, value2 };
		//int[] colors = new int[] { Color.BLUE, Color.GREEN, Color.MAGENTA, Color.YELLOW, Color.CYAN };
	    int[] colors = new int[] { Color.BLUE, Color.GREEN };
	    DefaultRenderer renderer = buildCategoryRenderer(colors);
	    renderer.setZoomButtonsVisible(true);
	    renderer.setZoomEnabled(true);
	    renderer.setChartTitleTextSize(20);
	    return ChartFactory.getPieChartIntent(context, buildCategoryDataset("Contas Mensais", values),
	        renderer, "Contas Mensais");
	}
	
	protected CategorySeries buildCategoryDataset(String title, double[] values) {
	      CategorySeries series = new CategorySeries(title);
	      int k = 0;
	      for (double value : values) {
	    	  NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
	    	  if (k == 0) {
	    		  series.add("A Pagar: " + nf.format(value), value);
	    		  k++;
	    	  }
	    	  else {
	    		  series.add("Pagas: " + nf.format(value), value); 
	    	  }
	        
	      }

	      return series;
	}
	
	private double getBillToPay(Context context) {
		DatabaseDelegate mDatabase = DatabaseDelegate.getInstance(context);
		ArrayList<Conta> mBillsToPay = mDatabase.readBillToPay();
		double totalBillsToPay = 0;
		for (Conta c : mBillsToPay) {
			totalBillsToPay += Double.parseDouble(c.getValor());
		}
		return totalBillsToPay;
	}
	
	private double getBillPaid(Context context) {
		DatabaseDelegate mDatabase = DatabaseDelegate.getInstance(context);
		ArrayList<Conta> mBillsPaid = mDatabase.readBillPaid();
		double totalBillsPaid = 0;
		for (Conta c : mBillsPaid) {
			totalBillsPaid += Double.parseDouble(c.getValor());
		}
		return totalBillsPaid;
	}

}
