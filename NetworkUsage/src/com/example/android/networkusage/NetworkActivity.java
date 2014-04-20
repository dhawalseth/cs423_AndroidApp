/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.android.networkusage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.example.android.networkusage.R;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Main Activity for the sample application.
 * 
 * This activity does the following:
 * 
 * o Presents a WebView screen to users. This WebView has a list of HTML links
 * to the latest questions tagged 'android' on stackoverflow.com.
 * 
 * o Parses the StackOverflow XML feed using XMLPullParser.
 * 
 * o Uses AsyncTask to download and process the XML feed.
 * 
 * o Monitors preferences and the device's network connection to determine
 * whether to refresh the WebView content.
 */
public class NetworkActivity extends Activity {
	public static final String WIFI = "Wi-Fi";
	public static final String ANY = "Any";
	private static final String URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";

	
	// Whether there is a Wi-Fi connection.
	private static boolean wifiConnected = false;
	// Whether there is a mobile connection.
	private static boolean mobileConnected = false;
	// Whether the display should be refreshed.
	public static boolean refreshDisplay = true;
	
	
	private String downloadSpeed1;

	// The user's current network preference setting.
	public static String sPref = null;
	
	private Switch mySwitch;
	private TextView switchStatus;
	private CheckBox cb1;
	private CheckBox cb2;
	private CheckBox cb3;
	private String input;
	

	// The BroadcastReceiver that tracks network connectivity changes.
	//private NetworkReceiver receiver = new NetworkReceiver();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Register BroadcastReceiver to track connection changes.
		IntentFilter filter = new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION);
	//	receiver = new NetworkReceiver();
		//this.registerReceiver(receiver, filter);
		
		setContentView(R.layout.main);
		

		switchStatus = (TextView) findViewById(R.id.switchStatus);
		mySwitch = (Switch) findViewById(R.id.mySwitch);
		
		cb1=(CheckBox) findViewById(R.id.checkBox1);
		cb2=(CheckBox) findViewById(R.id.checkBox2);
		cb3=(CheckBox) findViewById(R.id.checkBox3);
		
		
		
		  mySwitch.setChecked(true);
		  cb1.setChecked(true);
		  cb2.setChecked(true);
		  cb3.setChecked(true);
		  
		  
		  //attach a listener to check for changes in state
		  mySwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		   
		  @Override
		   public void onCheckedChanged(CompoundButton buttonView,
		     boolean isChecked) {
		 
		    if(isChecked){
		     input="Global Switch is currently ON";
		     switchStatus.setText(input);
		    }else{
		    	input="Global Switch is currently OFF";	
		        switchStatus.setText(input);
		    }
		 
		   }
		  }); 
		  
		  if(mySwitch.isChecked())
			  input="Global Switch is currently ON";
			  //switchStatus.setText("Switch is currently ON");  
		  else
			  //switchStatus.setText("Switch is currently OFF");
			  input="Global Switch is currently OFF";
	}
	
	
	public void onCheckboxClicked(View view) {
	    // Is the view now checked?
	    boolean checked = ((CheckBox) view).isChecked();
	    
	    // Check which checkbox was clicked
	    switch(view.getId()) {
	        case R.id.checkBox1:
	            if (checked)
	                switchStatus.setText("box 1 is checked");
	            else
	            	switchStatus.setText("box 1 is unchecked");
	            break;
	        case R.id.checkBox2:
	            if (checked)
	                switchStatus.setText("box 2 is checked");
	            else
	            	switchStatus.setText("box 2 is unchecked");
	            break;
	        case R.id.checkBox3:
	        	if (checked)
	                switchStatus.setText("box 3 is checked");
	            else
	            	switchStatus.setText("box 3 is unchecked");
	            break;
	        // TODO: Veggie sandwich
	    }
	}
	
	

	// Refreshes the display if the network connection and the
	// pref settings allow it.
	@Override
	public void onStart() {
		super.onStart();
		
		switchStatus.setText(input);
		// Gets the user's network preference settings
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		// Retrieves a string value for the preferences. The second parameter
		// is the default value to use if a preference value is not found.
		sPref = sharedPrefs.getString("listPref", "Any");

		// updateConnectedFlags();
//actionOnBatteryLife(getApplicationContext());
		//initialTest();

	}
	
	
	
	@Override
	public void onResume() {
	super.onResume();
	Start.start(this);
	}
	@Override
	public void onPause() {
	super.onPause();
	Start.start(this);
	}
	
	
	
//	
//	/**
//	 * 
//	 */
//	private void initialTest() {
//
//		// Check wifi and mobile connectivity
//		updateConnectedFlags();
//		// if Wifi is Connected
//		if (wifiConnected) {
//			// test wifi speed
//			SpeedTest.testSpeed();
//		} else {
//			// turn on Wifi and check speed
//			enableDisableWifi(true);
//			// check if wifi is connected
//			updateConnectedFlags();
//			if (wifiConnected) {
//				// test wifi speed
//				SpeedTest.testSpeed();
//			}
//			// else?
//		}
//
//		// Next test mobile speed
//		if (mobileConnected) {
//			// test mobile connection speed
//		}
//		// turn on Mobile and check speed
//		else {
//			// 1. turn off Wifi
//			enableDisableWifi(false);
//			// 2. turn on Mobile
//		}
//	}
//
//	/**
//	 * 
//	 */
//	private void enableDisableWifi(boolean doEnable) {
//		Context context = this.getApplicationContext();
//		WifiManager wifiManager = (WifiManager) context
//				.getSystemService(Context.WIFI_SERVICE);
//		wifiManager.setWifiEnabled(doEnable);
//	}

	@Override
	public void onDestroy() {
		super.onDestroy();
//		if (receiver != null) {
//			this.unregisterReceiver(receiver);
//		}
	}

//	// Checks the network connection and sets the wifiConnected and
//	// mobileConnected
//	// variables accordingly.
//	private void updateConnectedFlags() {
//		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
//		if (activeInfo != null && activeInfo.isConnected()) {
//			wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
//			mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
//		} else {
//			wifiConnected = false;
//			mobileConnected = false;
//		}
//	}
//
//	

}
