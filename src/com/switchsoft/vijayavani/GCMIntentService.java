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
package com.switchsoft.vijayavani;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.switchsoft.vijayavani.push.ServerUtilities;
import com.switchsoft.vijayavani.util.Constants;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	Bundle extras = null;
	String  alertMsg = null;
	private final static int NOTIFY_OFFERS = 1;
	private static int NOTIFY_TYPE_ID = 0;

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	@Override
	protected void onMessage(Context context, Intent intent) {

		Log.e(TAG, "Received message--"+intent.getExtras());
		extras = intent.getExtras();
		String message = extras.getString("message");
		Log.e(TAG, "Received message--"+message);

		// notifies user
		displayNotification(context, message);
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification"+total);
		// notifies user
		displayNotification(context, "Received deleted messages notification"+total);
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void displayNotification(Context context, String message) {

		int icon = R.drawable.app_icon;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager)
				context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification notification = new Notification(icon, message, when);
		String title = context.getString(R.string.app_name);

		RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
		contentView.setImageViewResource(R.id.image, icon);
		contentView.setTextViewText(R.id.title, title);
		contentView.setTextViewText(R.id.text, message);
		notification.contentView = contentView;

		Intent notificationIntent = new Intent(context, SplashScreenActivity.class);

		NOTIFY_TYPE_ID = NOTIFY_OFFERS;

		notificationIntent.putExtra("FromNotification", true);
		// set intent so it does not start a new activity
		/*notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);*/
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent intent =
				PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.contentIntent = intent;

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.defaults |= Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		notificationManager.notify(NOTIFY_TYPE_ID, notification);

	}

}
