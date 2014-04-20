/*
 * Copyright 2013 Thomas Hoffmann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.networkusage;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

public class LowBatteryAction extends Service {
	final static String BATTERY_LOW_ACTION = "BATTERY_LOW";

	private static BroadcastReceiver br;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressLint("NewApi")
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		if (br == null) {
			Log.e(TAG, "creating low battery receiver");
			br = new BatteryLowReceiver();
			IntentFilter intf = new IntentFilter();
			intf.addAction(Intent.ACTION_BATTERY_LOW);
			registerReceiver(br, intf);
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		if (Logger.LOG)
//			Logger.log("destroying screen off receiver");
		if (br != null) {
			try {
				unregisterReceiver(br);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		br = null;
	}

	private class BatteryLowReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_BATTERY_LOW.equals(intent.getAction())) {
				Log.e(TAG, "battery low action");
				sendBroadcast(new Intent(context, BatteryActionReceiver.class).setAction(BATTERY_LOW_ACTION));
			}
		}

	}
	private static final String TAG = NetworkActivity.class.getSimpleName();


}

///*
//* Calculate battery level
//*/
//private int calculateBatteryLevel(Context context) {
//	// LogFile.log("calculateBatteryLevel()");
//
//	Intent batteryIntent = context.getApplicationContext()
//			.registerReceiver(null,
//					new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
//
//	int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
//	int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
//	return level * 100 / scale;
//}
//
///*
//* Action when battery life is less
//*/
//private void actionOnBatteryLife(Context context) {
//	int batteryLevel = calculateBatteryLevel(context);
//	if (batteryLevel < 20) {
//		// turn off Wifi and GPS
//		enableDisableWifi(false);
//		// GPS?
//
//		if (batteryLevel < 20) {
//			// turn off network also
//		}
//	}
//}



