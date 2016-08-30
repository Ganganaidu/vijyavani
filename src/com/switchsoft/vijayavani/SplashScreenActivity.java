package com.switchsoft.vijayavani;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.switchsoft.vijayavani.asynctask.GetNewDataOnPush;
import com.switchsoft.vijayavani.asynctask.ItemsAsyncTask;
import com.switchsoft.vijayavani.asynctask.ItemsLevel4AsyncTask;
import com.switchsoft.vijayavani.asynctask.ItemsLevel5AsyncTask;
import com.switchsoft.vijayavani.asynctask.LoyaltyTypesAsyncTask;
import com.switchsoft.vijayavani.asynctask.MainCategoryAsyncTask;
import com.switchsoft.vijayavani.asynctask.OffersAsyncTask;
import com.switchsoft.vijayavani.asynctask.StoreLocatorAsyncTask;
import com.switchsoft.vijayavani.asynctask.SubCategoryAsyncTask;
import com.switchsoft.vijayavani.asynctask.TimeStampAsyncTask;
import com.switchsoft.vijayavani.push.CommonUtilities;
import com.switchsoft.vijayavani.push.ServerUtilities;
import com.switchsoft.vijayavani.sql.SQLHelper;

/**Flash screen which displays the flash image
 * */
public class SplashScreenActivity extends FragmentActivity {

	public static SplashScreenActivity splashscreen = null;

	//Declaring the views
	RelativeLayout mLoading_Relativelayout;
	TextView mLoading_Textview;
	TextView mDot1_Textview, mDot2_Textview, mPercentageComplete_Textview;

	//Declaring the SQL
	public static SQLHelper sqlhelper;

	//Stores value weather to get those data or not
	public static boolean download_offers = false;
	public static boolean download_main_category = false;
	public static boolean download_sub_category = false;
	public static boolean download_items = false;
	public static boolean download_storelocator = false;

	//GCM Push notification details
	AsyncTask<Void, Void, Void> mRegisterTask;
	//Allows the screen to be always switched on
	PowerManager.WakeLock wakelock;

	TranslateAnimation movetoright_1,movetoright_2,movetoright_3, 
	EnterFrom_left_1, EnterFrom_left_2, EnterFrom_left_3, 
	EnterText_FromLeft;


	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//removes the top status bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
		.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);

		setContentView(R.layout.splashscreen);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		splashscreen = this;

		mLoading_Relativelayout = (RelativeLayout)findViewById(R.id.loading_relativelayout);
		mLoading_Textview = (TextView)findViewById(R.id.loading_textview);
		mDot1_Textview = (TextView)findViewById(R.id.dot1_textview);
		mDot2_Textview = (TextView)findViewById(R.id.dot2_textview);
		mPercentageComplete_Textview = (TextView)findViewById(R.id.percentage_complete_textview);

		//Initializing the sql
		sqlhelper = new SQLHelper(getApplicationContext());

		//keeps the screen on without going into dim state
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		wakelock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "My Tag");
		wakelock.acquire();

		inititialize_animation();

		if(isOnline()){
			Bundle extras = getIntent().getExtras(); 
			if(extras !=null)
			{				
				new GetNewDataOnPush().execute();
				setPushType(0);

			}
			else{
				if(getPushRegid().equals("")){
					registerPushDetails();	
				} 
				new TimeStampAsyncTask().execute();
			}
			
			mLoading_Relativelayout.setVisibility(View.VISIBLE);
			mDot2_Textview.startAnimation(movetoright_1);
			update_percentage("2");
		} else {
			openMainActivity();
		}
	}

	private void inititialize_animation(){
		//for moving the text to right
		movetoright_1 = new TranslateAnimation(0,450,0,0);
		movetoright_1.setDuration(600);
		movetoright_1.setFillAfter(true);

		movetoright_2 = new TranslateAnimation(0,450,0,0);
		movetoright_2.setDuration(600);
		movetoright_2.setFillAfter(true);

		movetoright_3 = new TranslateAnimation(0,450,0,0);
		movetoright_3.setDuration(700);
		movetoright_3.setFillAfter(true);

		//for moving the text from left to right
		EnterFrom_left_1 = new TranslateAnimation(-450,0,0,0);
		EnterFrom_left_1.setDuration(600);
		EnterFrom_left_1.setFillAfter(true);

		EnterFrom_left_2 = new TranslateAnimation(-450,0,0,0);
		EnterFrom_left_2.setDuration(600);
		EnterFrom_left_2.setFillAfter(true);

		EnterFrom_left_3 = new TranslateAnimation(-450,0,0,0);
		EnterFrom_left_3.setDuration(700);
		EnterFrom_left_3.setFillAfter(true);

		EnterText_FromLeft = new TranslateAnimation(-450, 0, 0, 0);
		EnterText_FromLeft.setDuration(600);
		EnterText_FromLeft.setFillAfter(true);


		movetoright_1.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mDot2_Textview.setVisibility(View.GONE);
				mDot1_Textview.startAnimation(movetoright_2);

			}
		});

		movetoright_2.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mDot1_Textview.setVisibility(View.GONE);
				mLoading_Textview.startAnimation(movetoright_3);
				loading_timer(200,100);

			}
		});

		movetoright_3.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mLoading_Textview.setVisibility(View.GONE);				
			}
		});

		EnterFrom_left_1.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mDot1_Textview.setVisibility(View.VISIBLE);
				mDot1_Textview.startAnimation(EnterFrom_left_2);
			}
		});   

		EnterFrom_left_2.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mLoading_Textview.setVisibility(View.VISIBLE);
				mLoading_Textview.startAnimation(EnterFrom_left_3);
			}
		}); 

		EnterFrom_left_3.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				mDot2_Textview.startAnimation(movetoright_1);
			}
		});
	}
	
	public void update_percentage(String percentage){
		mPercentageComplete_Textview.setVisibility(View.VISIBLE);
		mPercentageComplete_Textview.setText(percentage + " %");
		mPercentageComplete_Textview.startAnimation(EnterText_FromLeft);
	}

	//Count down timer for showing the next screen after specific time
	CountDownTimer loading_timer;
	/**
	 * Using Timer for animating the loading text
	 */
	private void loading_timer(long millis,long countdown){

		loading_timer = new CountDownTimer(millis, countdown) {
			public void onTick(long millisUntilFinished) {

			}
			//when count down completes
			public void onFinish() {
				//Opens the tab layout view
				mDot2_Textview.setVisibility(View.VISIBLE);
				mDot2_Textview.startAnimation(EnterFrom_left_1);
			}
		};
		loading_timer.start();
	}

	/**
	 * Returns the network state
	 */
	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**Database retriever
	 * */
	/**This method will get the details from database based on Query */
	public Cursor getRawEvents(String sql) {
		SQLiteDatabase db = (sqlhelper).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		return cursor;
	}

	public void setPushType(int type){
		SharedPreferences preferences = getSharedPreferences("PUSH_TYPE",MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("PushType", type);
		editor.commit();
	}
	public int getPushType(){
		SharedPreferences preferences = getSharedPreferences("PUSH_TYPE",MODE_PRIVATE);
		int type = preferences.getInt("PushType", 0);
		return type;
	}


	public void setPushRegid(String regid){
		SharedPreferences preferences = getSharedPreferences("PUSH_REGID",MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("PushRedId", regid);
		editor.commit();
	}
	public String getPushRegid(){
		SharedPreferences preferences = getSharedPreferences("PUSH_REGID",MODE_PRIVATE);
		String regid = preferences.getString("PushRedId", "");
		return regid;
	}

	//////////////////////////////////////////////////////////Async task start
	/**On asynchronous task complete
	 * */
	//Time stamp complete
	public void onTimeStampComplete(){
		if(isOnline()){	
			if(download_main_category == true){
				new MainCategoryAsyncTask().execute();

			} else if(download_sub_category == true){
				new SubCategoryAsyncTask().execute();

			} else if(download_items == true){
				new ItemsAsyncTask().execute();

			} else if(download_offers == true){
				new OffersAsyncTask().execute();

			} else if(download_storelocator == true){
				new StoreLocatorAsyncTask().execute();

			} else {
				new LoyaltyTypesAsyncTask().execute();
			}

		} else {
			openMainActivity();
		}
	}

	//Main category complete
	public void onMainCategoryComplete(){
		if(isOnline()){	
			if(download_sub_category == true){
				new SubCategoryAsyncTask().execute();

			} else if(download_items == true){
				new ItemsAsyncTask().execute();

			} else if(download_offers == true){
				new OffersAsyncTask().execute();

			} else if(download_storelocator == true){
				new StoreLocatorAsyncTask().execute();

			} else {
				new LoyaltyTypesAsyncTask().execute();

			}
		}else {
			openMainActivity();
		}
	}

	//Sub category complete
	public void onSubCategoryComplete(){
		if(isOnline()){	
			if(download_items == true){
				new ItemsAsyncTask().execute();

			} else if(download_offers == true){
				new OffersAsyncTask().execute();

			} else if(download_storelocator == true){
				new StoreLocatorAsyncTask().execute();

			} else {
				new LoyaltyTypesAsyncTask().execute();

			}
		}else {
			openMainActivity();
		}
	}

	//Item complete
	public void onItemComplete(){
		new ItemsLevel4AsyncTask().execute();
	}

	//Item4 complete
	public void onItem4Complete(){
		new ItemsLevel5AsyncTask().execute();
	}

	//Item5 complete
	public void onItem5Complete(){
		if(isOnline()){	
			if(download_offers == true){
				new OffersAsyncTask().execute();

			} else if(download_storelocator == true){
				new StoreLocatorAsyncTask().execute();

			} else {
				new LoyaltyTypesAsyncTask().execute();

			}
		}else {
			openMainActivity();
		}
	}

	//Offer complete
	public void onOfferComplete(){
		if(isOnline()){	
			if(download_storelocator == true){
				new StoreLocatorAsyncTask().execute();

			} else {
				new LoyaltyTypesAsyncTask().execute();

			}
		}else {
			openMainActivity();
		}
	}

	//Store locator complete
	public void onStoreLocatorComplete(){
		if(isOnline()){	
			new LoyaltyTypesAsyncTask().execute();

		} else {
			openMainActivity();
		}
	}

	//Loyalty complete
	public void onLoyaltyTypesComplete(){
		openMainActivity();
	}

	/**GCm Push Registration*/
	private void registerPushDetails(){

		String TAG = "Activity";

		final String regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, CommonUtilities.SENDER_ID);
			Log.d(TAG,"regId--nulll"+getPushRegid());
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
				//mDisplay.append(getString(R.string.already_registered) + "\n");
				Log.d(TAG,""+getString(R.string.already_registered) + "\n"); 
				Log.d(TAG,"regId--else "+getPushRegid());
			} else {

				Log.d(TAG,"regId--async "+getPushRegid());
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						boolean registered = ServerUtilities.register(context, regId);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}
				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}
	//////////////////////////////////////////////////////////END Async task start

	public void openMainActivity(){
		//removes the screen on state
		if(wakelock!=null && wakelock.isHeld()==true){
			wakelock.release();
			wakelock = null;
		}
		finish();
		startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//Closing the database if it is still open
		if(sqlhelper != null){
			sqlhelper.close();
		}

		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
	}

	/**When back key is pressed this method is called*/
	@Override
	public void onBackPressed() {		
		//removes the screen on state
		if(wakelock!=null && wakelock.isHeld()==true){
			wakelock.release();
			wakelock = null;
		}

		finish();
	}


}

