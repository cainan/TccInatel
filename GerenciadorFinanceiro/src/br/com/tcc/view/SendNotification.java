package br.com.tcc.view;

import java.text.NumberFormat;
import java.util.Locale;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import br.com.tcc.R;

public class SendNotification extends BaseActivity {
	
	public static final String SUBJECT = "Notificação de contas";
	
	private String message;
	
	private String emailsTo;
	
	private String phonesTo;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);
        
        Button btn_send_email = (Button) findViewById(R.id.button_send_email);
        Button btn_send_sms = (Button) findViewById(R.id.button_send_sms);
        
        Intent i = getIntent();
        message = parserMessage(i.getStringExtra("bills"), i.getStringExtra("names"));
        emailsTo = i.getStringExtra("emails");
        phonesTo = i.getStringExtra("phones");
        
        btn_send_email.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{emailsTo});
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, SUBJECT);
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

				startActivity(Intent.createChooser(emailIntent, "Enviar e-mail..."));				
			}
		});

        btn_send_sms.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Uri u = Uri.fromParts("sms", phonesTo, null);
				Intent sendIntent = new Intent(Intent.ACTION_VIEW, u);
				sendIntent.putExtra("sms_body", message); 		          
				startActivity(sendIntent); 					
			}
		});
    }
    
    private String parserMessage(String message, String names) {
    	NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    	StringBuffer sb = new StringBuffer();
    	String [] divMessage = message.split(";");
    	String [] divNames = names.split(",");
    	int posTotal = divMessage.length - 1;
    	float total = Float.parseFloat(divMessage[posTotal]);
    	sb.append("Notificação sobre contas\n\n");
    	
    	for (int i=0; i<posTotal; i++) {
    		sb.append(divMessage[i] + "\n");
    	}
    	
    	sb.append("\nTotal: " + nf.format(total) + "\n\n");
    	sb.append("Total de pessoas que irão pagar: " + divNames.length + " (");
    	
    	for (int i=0; i<divNames.length; i++) {
    		if (i == divNames.length - 1) {
    			sb.append(divNames[i]);
    		}
    		else {
    			sb.append(divNames[i] + ", ");
    		}    		
    	}
    	
    	sb.append(").\n\n");
    	sb.append("Portanto, " + nf.format(total) + "/" + divNames.length + " = " + nf.format(total/divNames.length) + " para cada um.");
    	sb.append("\n\n\nObrigado!!!");
    	
    	return sb.toString();
    }
   
}
