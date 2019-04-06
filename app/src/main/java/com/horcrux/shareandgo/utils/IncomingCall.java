package com.horcrux.shareandgo.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.horcrux.shareandgo.models.MessageData;
import com.horcrux.shareandgo.models.PhoneCall;

public class IncomingCall extends BroadcastReceiver {

    Context context;

    DatabaseReference reference;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        if(intent.getAction().equals("android.intent.action.PHONE_STATE")) {

            reference = FirebaseDatabase.getInstance().getReference("" + FirebaseAuth.getInstance().getUid()).child("PhoneState");

            try {
                // TELEPHONY MANAGER class object to register one listner
                TelephonyManager tmgr = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);

                //Create Listner
                MyPhoneStateListener phoneListener = new MyPhoneStateListener();

                // Register listener for LISTEN_CALL_STATE
                assert tmgr != null;
                tmgr.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);

            } catch (Exception e) {
                Log.e("Phone Receive Error", " " + e);
            }

//            context.startService(new Intent(context,IncomingServices.class));
        }


        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {

            reference = FirebaseDatabase.getInstance().getReference("" + FirebaseAuth.getInstance().getUid()).child("Message");

            final SmsManager sms = SmsManager.getDefault();

            final Bundle bundle = intent.getExtras();

            try {

                if (bundle != null) {

                    final Object[] pdusObj = (Object[]) bundle.get("pdus");

                    for (int i = 0; i < pdusObj.length; i++) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String senderNum = phoneNumber;
                        String message = currentMessage.getDisplayMessageBody();

                        MessageData data = new MessageData(senderNum, message);
                        reference.setValue(data);

                    } // end for loop
                } // bundle is null

            } catch (Exception e) {
                Log.e("SmsReceiver", "Exception smsReceiver" +e);

            }


//            context.startService(new Intent(context,IncomingServices.class));
        }

    }

    private class MyPhoneStateListener extends PhoneStateListener {

        public void onCallStateChanged(int state, String incomingNumber) {

            Toast.makeText(context, state + " incoming no: " + incomingNumber, Toast.LENGTH_SHORT).show();
            Log.d("MyPhoneListener",state + "   incoming no:" + incomingNumber);

            PhoneCall call = new PhoneCall(incomingNumber, state + "");
            reference.setValue(call);

        }
    }
}
