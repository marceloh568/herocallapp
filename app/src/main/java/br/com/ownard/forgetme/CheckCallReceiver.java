package br.com.ownard.forgetme;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


import java.lang.reflect.Method;

import controllers.CallController;


/**
 * Created by marcelo.cunha on 27/12/2017.
 */

public class CheckCallReceiver extends BroadcastReceiver {

    private CallController callController;
    public static final String TAG = "PHONE STATE";
    private static String mLastState;

    @Override
    public void onReceive(final Context context, final Intent intent) {


        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            mLastState = state;

            if (callController == null) {
                callController = new CallController(context);
            }

            TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            try {

                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                if (callController.checkPhone(number)) {
                    TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    try {
                        Class c = Class.forName(tm.getClass().getName());
                        Method m = c.getDeclaredMethod("getITelephony");
                        m.setAccessible(true);
                        Object telephonyService = m.invoke(tm);

                        c = Class.forName(telephonyService.getClass().getName());
                        m = c.getDeclaredMethod("endCall");
                        m.setAccessible(true);
                        m.invoke(telephonyService);

                        Toast.makeText(context, "EasyBlockCall - " + number, Toast.LENGTH_SHORT).show();
                        callController.addBlockCount(number);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void displayScreen(Intent intent, final Context context){

        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Intent intentPhoneCall = new Intent("android.intent.action.ANSWER");
                        intentPhoneCall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentPhoneCall);
                    }
                }, 100);
            }
        }
    }

}
