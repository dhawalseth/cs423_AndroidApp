package com.example.android.networkusage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

//import com.example.android.networkusage.SpeedInfo;

public class SpeedTest {
	
// @Override

static double testSpeed() {
	InputStream stream = null;
	try {
		int bytesIn = 0;
		String downloadSpeed1;
		String downloadFileUrl = "http://www.gregbugaj.com/wp-content/uploads/2009/03/dummy.txt";
		long startCon = System.currentTimeMillis();
		URL url = new URL(downloadFileUrl);
		URLConnection con = url.openConnection();
		con.setUseCaches(false);
		long connectionLatency = System.currentTimeMillis() - startCon;
		stream = con.getInputStream();
		// Message msgUpdateConnection=Message.obtain(mHandler,
		// MSG_UPDATE_CONNECTION_TIME);

		// msgUpdateConnection.arg1=(int) connectionLatency;
		// mHandler.sendMessage(msgUpdateConnection);

		long start = System.currentTimeMillis();
		int currentByte = 0;
		long updateStart = System.currentTimeMillis();
		long updateDelta = 0;
		int bytesInThreshold = 0;

		while ((currentByte = stream.read()) != -1) {
			bytesIn++;
			bytesInThreshold++;
			if (updateDelta >= UPDATE_THRESHOLD) {
				int progress = (int) ((bytesIn / (double) EXPECTED_SIZE_IN_BYTES) * 100);
				Log.e(TAG, "Progress");
				Log.e(TAG, new Integer(progress).toString());
				// Message msg=Message.obtain(mHandler, MSG_UPDATE_STATUS,
				// calculate(updateDelta, bytesInThreshold));
				// msg.arg1=progress;
				// msg.arg2=bytesIn;
				// mHandler.sendMessage(msg);
				// Reset
				updateStart = System.currentTimeMillis();
				bytesInThreshold = 0;
			}
			updateDelta = System.currentTimeMillis() - updateStart;
		}

		long downloadTime = (System.currentTimeMillis() - start);
		// Prevent AritchmeticException
		if (downloadTime == 0) {
			downloadTime = 1;
		}
		// Message msg=Message.obtain(mHandler, MSG_COMPLETE_STATUS,
		// calculate(downloadTime, bytesIn));
		// msg.arg1=bytesIn;
		// mHandler.sendMessage(msg);
		final SpeedInfo info1 = calculate(downloadTime, bytesIn);
		Log.e(TAG, "Megabits");
		Log.e(TAG, String.valueOf(info1.megabits));
		Log.e(TAG, "Kilobits");
		Log.e(TAG, String.valueOf(info1.kilobits));
		Log.e(TAG, "Download speed");
		Log.e(TAG, String.valueOf(info1.downspeed));
		
		
		downloadSpeed1=String.valueOf(info1.megabits);
		return info1.downspeed;
		
		/*
		setContentView(R.layout.main);
		TextView textview=(TextView) findViewById(R.id.textview);
		textview.setText("Download speed:");
		*/
		
	} catch (MalformedURLException e) {
		Log.e(TAG, e.getMessage());
	} catch (IOException e) {
		Log.e(TAG, e.getMessage());
	} finally {
		try {
			if (stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			// Suppressed
		}
	}
	return 0;

}

// };

private static SpeedInfo calculate(final long downloadTime, final long bytesIn) {
	SpeedInfo info = new SpeedInfo();
	// from mil to sec
	long bytespersecond = (bytesIn / downloadTime) * 1000;
	double kilobits = bytespersecond * BYTE_TO_KILOBIT;
	double megabits = kilobits * KILOBIT_TO_MEGABIT;
	info.downspeed = bytespersecond;
	info.kilobits = kilobits;
	info.megabits = megabits;

	return info;
}

private static class SpeedInfo {
	public double kilobits = 0;
	public double megabits = 0;
	public double downspeed = 0;
}

private final static int UPDATE_THRESHOLD = 300;
private static final int EXPECTED_SIZE_IN_BYTES = 1048576;// 1MB 1024*1024
private static final String TAG = NetworkActivity.class.getSimpleName();
private static final double EDGE_THRESHOLD = 176.0;
private static final double BYTE_TO_KILOBIT = 0.0078125;
private static final double KILOBIT_TO_MEGABIT = 0.0009765625;
}

