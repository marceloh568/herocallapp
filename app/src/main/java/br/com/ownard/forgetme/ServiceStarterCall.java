package br.com.ownard.forgetme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by marcelo.cunha on 28/02/2018.
 */

public class ServiceStarterCall extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent("br.com.ownard.forgetme.CheckCallReceiver");
        i.setClass(context, CheckCallReceiver.class);
        context.startService(i);
    }
}
