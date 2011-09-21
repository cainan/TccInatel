package br.com.tcc.service;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import br.com.tcc.R;
import br.com.tcc.model.Conta;
import br.com.tcc.model.database.DatabaseDelegate;
import br.com.tcc.view.NotificationActivity;

public class ScheduleService extends Service {

    /** Message used when a notification appears in notification bar */
    private static final String NOTIFICATION_BAR_TEXT = "Uma conta foi agendada para pagamento";

    /** Message used in notification title */
    private static final String NOTIFICATION_TITLE = "Conta a pagar:";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("log", "Starting Service");

        // Adding filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);

        // Registering Broadcast Receiver
        registerReceiver(mBroadcast, filter);

        searchBillsToPay();

        super.onCreate();
    }

    /**
     * Broadcast receiver that receive an intent every time the time changes
     */
    BroadcastReceiver mBroadcast = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
                Log.d("log", "Time Changed");
                searchBillsToPay();
            }

        }

    };

    /**
     * Check if there is an bill to be payed in the moment that the method is called
     */
    private void searchBillsToPay() {
        DatabaseDelegate db = DatabaseDelegate.getInstance(getApplicationContext());
        ArrayList<Conta> arrayConta = db.ReadBillToPay();

        GregorianCalendar today = new GregorianCalendar();
        String[] date;
        String[] time;
        int year;
        int month;
        int day;
        int hour;
        int minute;

        for (int i = 0; i < arrayConta.size(); i++) {

            date = arrayConta.get(i).getVencimento().toString().split("/");
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]) - 1;
            year = Integer.parseInt(date[2]);

            time = arrayConta.get(i).getNotificar().toString().split(":");
            hour = Integer.parseInt(time[0]);
            minute = Integer.parseInt(time[1]);

            GregorianCalendar billToPay = new GregorianCalendar(year, month, day, hour, minute);

            if (billToPay.getTime().toString().equals(today.getTime().toString())
                    || billToPay.getTime().before(today.getTime())) {

                String message = arrayConta.get(i).getNome();
                createNotification(this, NOTIFICATION_BAR_TEXT, NOTIFICATION_TITLE, message,
                        NotificationActivity.class, arrayConta.get(i));
            }

        }
    }

    /**
     * Create a notification
     * 
     * @param context
     * @param notificationBarText
     * @param notificationTitle
     * @param message
     * @param class1
     * @param conta
     */
    private void createNotification(Context context, String notificationBarText,
            String notificationTitle, String message, Class<NotificationActivity> class1,
            Conta conta) {

        // Get notification service
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Notification notification = new Notification(R.drawable.icon, notificationBarText, System
                .currentTimeMillis());

        // Create an intent to start NotificationActivity
        Intent itt = new Intent(context, class1);

        // Put the bill to pay in extra
        itt.putExtra(NotificationActivity.BILL_PARAMETER, conta);

        // Configure PendingIntent
        PendingIntent pendingItt = PendingIntent.getActivity(context, 0, itt,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Set notification event
        notification.setLatestEventInfo(context, notificationTitle, message, pendingItt);

        // notification.vibrate = new long[] { 100, 250, 100, 300 };
        notification.defaults |= Notification.DEFAULT_LIGHTS;

        int id = conta.getId();
        nm.notify(id, notification);

    }
}
