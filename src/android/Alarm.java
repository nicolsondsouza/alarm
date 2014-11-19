package com.trinisoft.alarm;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trinisoft.alarm.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

/**
 * This class echoes a string called from JavaScript.
 */
public class Alarm extends CordovaPlugin {
	private PendingIntent pendingIntent1,pendingIntent2;
	private AlarmManager manager;
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    	Context context = this.cordova.getActivity().getApplicationContext();
    	if (action.equals("setAlarm")) {
    		long date = args.getLong(0);
			
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.cordova.getActivity());
			SharedPreferences.Editor editor = settings.edit();
            editor.putLong("AlarmPlugin.AlarmDate", date); //$NON-NLS-1$
            editor.commit();
			
			AlarmManager alarmMgr = (AlarmManager)(this.cordova.getActivity().getSystemService(Context.ALARM_SERVICE));
			
			PendingIntent alarmIntent;     
			Intent intent = new Intent(this.cordova.getActivity(), AlarmReceiver.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			alarmIntent = PendingIntent.getBroadcast(this.cordova.getActivity(), 0, intent, 0);
			
			alarmMgr.cancel(alarmIntent);
			alarmMgr.set(AlarmManager.RTC_WAKEUP,  date, alarmIntent);
			
			callbackContext.success("Alarm set at: " +date);
			//Toast.makeText(context, "Alarm set at: " +date, Toast.LENGTH_SHORT).show();
        	return true;
//        	Intent alarmIntent = new Intent(context, AlarmReceiver.class);
//        	alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//     		pendingIntent1 = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//			pendingIntent2 = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
//			
//			
//     		
//     		manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
//     		
//			Calendar cal1 = Calendar.getInstance();
//			cal1.setTimeInMillis(System.currentTimeMillis());
//			cal1.set(Calendar.HOUR_OF_DAY, 16);
//			cal1.set(Calendar.MINUTE, 21);
//			cal1.set(Calendar.SECOND, 0);
//			
//			Calendar cal2 = Calendar.getInstance();
//			cal2.set(Calendar.HOUR_OF_DAY, args.getInt(0));//8pm for example
//			cal2.set(Calendar.MINUTE, args.getInt(1));
//			cal2.set(Calendar.SECOND, args.getInt(2));
//			
//			manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, args.getLong(0), AlarmManager.INTERVAL_DAY, pendingIntent1);
			//manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal2.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent2);
			
			
     		//String message = args.getString(0);
//            this.echo("Done!", callbackContext);
//            return true;
        }
        return false;
    }

    private void echo(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
