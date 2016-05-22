package io.humanplz.humanplz;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.logging.Logger;

public class PhoneCallReceiver extends BroadcastReceiver
{
    private static final Logger log = Logger.getLogger( PhoneCallReceiver.class.getName() );

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(isOutgoingCall(intent)) {

            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String phoneNumber = getResultData();
            if (phoneNumber == null) {
                phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            }
            log.info("Receiving outgoing call to phonenumber: " + phoneNumber);

            // NOTE: this is temporary hack.. to be replaced with a real database
            while(!URLDataDownload.isFinishedDownloading());
            ArrayList<DataEntry>  dataEntries = URLDataDownload.getData();

            for(DataEntry dataEntry: dataEntries) {
                log.info("apa " + dataEntry.getPhoneNumber());
                if(dataEntry.getPhoneNumber().equals(phoneNumber)) {
                    log.info("found matching phonenumber in database");
                    phoneNumber += "," + dataEntry.getDtfmCode();
                    setResultData(phoneNumber);
                    log.info("Phonenumber is now " + phoneNumber);
                    break;
                }
            }
        }
    }

    private boolean isOutgoingCall(Intent intent) {
        return intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL");
    }


}
