package com.jpos.desktopmode.ext.fw.floatdot;
import android.content.*;

public class CallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, XHFWService.class);
        context.startService(startServiceIntent);
    }
}
