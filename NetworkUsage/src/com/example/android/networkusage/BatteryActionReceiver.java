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
	import android.content.BroadcastReceiver;
	import android.content.Context;
	import android.content.Intent;
	import android.net.wifi.WifiManager;
	import android.util.Log;
	import android.widget.Toast;

public class BatteryActionReceiver extends BroadcastReceiver {

		/**
		 * Changes the WiFi state
		 * 
		 * @param context
		 * @param on
		 *            true to turn WiFi on, false to turn it off
		 */
		private static void changeWiFi(Context context, boolean on) {
			try {
				((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).setWifiEnabled(on);
			} catch (Exception e) {
				Toast.makeText(context, "Can not change WiFi state: " + e.getClass().getName(), Toast.LENGTH_LONG).show();
			}
		}

		@SuppressLint("InlinedApi")
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (LowBatteryAction.BATTERY_LOW_ACTION.equals(action)) {
				changeWiFi(context, false);
				Log.e(TAG, "Switching wifi off");
			} else if (Intent.ACTION_BATTERY_OKAY.equals(action)) {
				changeWiFi(context, true);
				Log.e(TAG, "Switching wifi on");
			} else if (intent.getAction().equals(Intent.ACTION_POWER_CONNECTED)) {
				changeWiFi(context, true);
			}
		}
		
		private static final String TAG = NetworkActivity.class.getSimpleName();

	}

