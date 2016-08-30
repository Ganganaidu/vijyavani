/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.switchsoft.vijayavani.push;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.switchsoft.vijayavani.util.Constants;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {

	private static final String TAG = "ServerUtilities";
//	private static final int MAX_ATTEMPTS = 5;
	private static final int MAX_ATTEMPTS = 0;
	private static final int BACKOFF_MILLI_SECONDS = 2000;
	private static final Random random = new Random();
	private static String retunType ="Insert_NEWICatelogdb_push_DeviceIdResult";
	private static String deviceType= "android";
	/**
	 * Register this account/device pair within the server.
	 *
	 * @return whether the registration succeeded or not.
	 */
	public static boolean register(final Context context, final String regId) {

		Log.i(TAG, "registering device (regId = " + regId + ")");

		boolean result = false;
		String serverUrl = Constants.GetServerUrl(regId,deviceType,Constants.catalodid);

		long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
		// Once GCM returns a registration id, we need to register it in the
		// demo server. As the server might be down, we will retry it a couple
		// times.
		for (int i = 1; i <= MAX_ATTEMPTS; i++) {
			Log.d(TAG, "Attempt #" + i + " to register");
			result = registerId(serverUrl);
			if(result){
				GCMRegistrar.setRegisteredOnServer(context, true);
				return true;
			}else{
				// Here we are simplifying and retrying on any error; in a real
				// application, it should retry only on unrecoverable errors
				// (like HTTP error code 503).
				Log.e(TAG, "Failed to register on attempt " + i);
				if (i == MAX_ATTEMPTS) {
					break;
				}
				try {
					Log.d(TAG, "Sleeping for " + backoff + " ms before retry");
					Thread.sleep(backoff);
				} catch (InterruptedException e1) {
					// Activity finished before we complete - exit.
					Log.d(TAG, "Thread interrupted: abort remaining retries!");
					Thread.currentThread().interrupt();
					return false;
				}
				// increase backoff exponentially
				backoff *= 2;
			}
		}
		return false;
	}

	/**
	 * Unregister this account/device pair within the server.
	 */
	public static void unregister(final Context context, final String regId) {
		
		Log.i(TAG, "unregistering device (regId = " + regId + ")");
		
		GCMRegistrar.setRegisteredOnServer(context, false);
	}

	
	public static boolean registerId(String serverUrl)
	{
		JSONObject jsonObject;
		try
		{
			URL connect = new URL(serverUrl);
			URLConnection connection = connect.openConnection(); 
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(10000);
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while((current = bis.read()) != -1)
			{
				baf.append((byte)current);
			}
			String myString = new String(baf.toByteArray());
			jsonObject = new JSONObject(myString);
			Log.d(TAG,"The Response is:"+jsonObject);

			String responseString = jsonObject.getString(retunType);

			Log.d(TAG,"String -- "+responseString);
			if(responseString.equals("INSERTED")){
				return true;
			}else{
				return false;
			}
		}
		catch (SocketTimeoutException e) 
		{
			e.printStackTrace();
			return false;

		}catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			return false;

		}
	}
}
