package es.source.code.br;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ButtonBroadcastReceiver extends BroadcastReceiver {
    public final static String ACTION_BUTTON = "es.source.code.br.ButtonBroadcastReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("Tag1","清除");
        if(action.equals(ACTION_BUTTON)){
            NotificationManager notifyManager = (NotificationManager) context.getSystemService(Context
                    .NOTIFICATION_SERVICE);
            notifyManager.cancel(intent.getIntExtra("id", 0));
        }
    }
}
