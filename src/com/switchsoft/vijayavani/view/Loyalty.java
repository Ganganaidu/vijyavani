package com.switchsoft.vijayavani.view;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.switchsoft.vijayavani.R;
import com.switchsoft.vijayavani.sql.SQLHelper;
import com.switchsoft.vijayavani.util.Animations;
import com.switchsoft.vijayavani.util.Constants;
import com.switchsoft.vijayavani.util.GPSClass;
import com.switchsoft.vijayavani.util.GPSClass.LocationResult;

public class Loyalty extends Fragment implements OnClickListener{

	GPSClass gpsClass;
	LocationManager locationManager;

	//Declaring the view
	RelativeLayout mLoyalty_Main_Relativelayout;
	ProgressBar mLoyalty_Progressbar;
	ScrollView mLoyalty_Scrollview;

	//Sign up layout
	RelativeLayout mLogin_layout;
	RelativeLayout mSignup_Relativelayout;
	Button mSend_Signup_Button,mLogin_button,mSignup_button;
	EditText mName_Loyalty_Edittext, mEmail_Loyalty_Edittext, mPassword_Loyalty_Edittext,
	mPhoneno_Loyalty_Edittext, mStrt_Address_Loyalty_Edittext, mArea_Loyalty_Edittext,
	mCity_Loyalty_Edittext, mState_Loyalty_Edittext,mUsername_editText,mPassword_editText;


	//Rewards layout
	RelativeLayout mRewards_Relativelayout;
	Button mRewards_Checkin_Button;
	TextView mRewards_Earned_Textview;

	Animation growfromMiddle, shrinktoMiddle;

	String login_details = "";
	String user_details_url = "";
	String insertloyalty_details_url = "";
	String getloyalty_details_url = "";

	private Calendar ci;    
	String myCode = null;

	private boolean isLoginSucess = false;
	private boolean registration_task = false;
	private boolean myloyalty_task = false;
	private boolean insertpoints = false;
	private static boolean switch_GPS= false;
	private static boolean checkin_pressed = false;

	//variable for saving the usename
	String username = "";

	//result after inserting the registration
	String registration_info = "";
	String login_info = "";
	String inserted_loyalty_state = "";
	String myloyalty_details = "";

	SQLHelper sqlhelper;
	Constants constant;
	Handler mHandler;
	Animations animation_util;

	//get the loyalty id based on the store id which got checkin
	String loyalty_id;
	int storeid;

	String loyalty_checkin = "12";
	String loyalty_signup = "11";

	HashMap<String, Object> hm;	
	ArrayList<HashMap<String, Object>> storelocation_details;
	ArrayList<Integer> storelocation_distance;

	//Initialize only variables and objects do not initialize any views here
	//as this method will call first
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}
	//second this view called
	// Initialize all views here
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.loyalty, container, false);

		//Initializing the layout views
		mLoyalty_Main_Relativelayout = (RelativeLayout)view.findViewById(R.id.loyalty_main_relativelayout);
		mLoyalty_Progressbar = (ProgressBar)view.findViewById(R.id.loyalty_progressbar);
		mLoyalty_Scrollview = (ScrollView)view.findViewById(R.id.loyalty_scrollview);

		//sign up layout
		mSignup_Relativelayout = (RelativeLayout)view.findViewById(R.id.signup_relativelayout);
		mName_Loyalty_Edittext = (EditText)view.findViewById(R.id.name_loyalty_edittext);
		mEmail_Loyalty_Edittext = (EditText)view.findViewById(R.id.email_loyalty_edittext);
		mPassword_Loyalty_Edittext = (EditText)view.findViewById(R.id.password_loyalty_edittext);
		mPhoneno_Loyalty_Edittext = (EditText)view.findViewById(R.id.phoneno_loyalty_edittext);
		mStrt_Address_Loyalty_Edittext = (EditText)view.findViewById(R.id.strt_address_loyalty_edittext);
		mArea_Loyalty_Edittext = (EditText)view.findViewById(R.id.area_loyalty_edittext);
		mCity_Loyalty_Edittext = (EditText)view.findViewById(R.id.city_loyalty_edittext);
		mState_Loyalty_Edittext = (EditText)view.findViewById(R.id.state_loyalty_edittext);
		mSend_Signup_Button = (Button)view.findViewById(R.id.send_signup_button);

		// login layout
		mLogin_layout = (RelativeLayout)view.findViewById(R.id.login_layout);
		mPassword_editText =  (EditText)view.findViewById(R.id.password_editText);
		mUsername_editText =  (EditText)view.findViewById(R.id.username_editText);				
		mLogin_button = (Button)view.findViewById(R.id.login_button);
		mSignup_button = (Button)view.findViewById(R.id.signup_button);

		//rewards layout
		mRewards_Relativelayout = (RelativeLayout)view.findViewById(R.id.rewards_relativelayout);
		mRewards_Checkin_Button = (Button)view.findViewById(R.id.rewards_checkin_button);
		mRewards_Earned_Textview = (TextView)view.findViewById(R.id.rewards_earned_textview);


		//closing all the views depending on the data the views will be made visible

		return view;
	}

	// after onCreateview this method will call..so put all listeners in this class
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		sqlhelper = new SQLHelper(getActivity());
		constant = new Constants();
		mHandler = new Handler();
		animation_util = new Animations();

		ci = Calendar.getInstance();      // using calender for getting Time and Date

		mUsername_editText.setHint("User Name");
		mPassword_editText.setHint("Password");

		//Animation
		growfromMiddle = AnimationUtils.loadAnimation(getActivity(), R.anim.grow_from_middle);

		//on click listener for the buttons
		mSend_Signup_Button.setOnClickListener(this);
		mRewards_Checkin_Button.setOnClickListener(this);

		mLogin_button.setOnClickListener(this);
		mSignup_button.setOnClickListener(this);

		getStore_Details();

		if(getClass_UserID() == 0 || getLoginResponce() == null){

			mSignup_Relativelayout.setVisibility(View.GONE);
			mLogin_layout.setVisibility(View.VISIBLE);

		} else {

			mLoyalty_Scrollview.setVisibility(View.VISIBLE);

			mLogin_layout.setVisibility(View.GONE);
			mSignup_Relativelayout.setVisibility(View.GONE);
			mRewards_Relativelayout.setVisibility(View.VISIBLE);
			mRewards_Earned_Textview.setText(getClass_savePoints());
		}



	}


	@Override
	public void onStart(){
		super.onStart();
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

	}

	//Your created method
	public void onBackPressed()
	{
		//Handle any cleanup you don't always want done in the normal lifecycle
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send_signup_button:

			close_VirtualKeyboards();

			String name = mName_Loyalty_Edittext.getText().toString();
			String email_id = mEmail_Loyalty_Edittext.getText().toString();
			String password_register = mPassword_Loyalty_Edittext.getText().toString();
			String mobile = mPhoneno_Loyalty_Edittext.getText().toString();
			String strt_address = mStrt_Address_Loyalty_Edittext.getText().toString();
			String area = mArea_Loyalty_Edittext.getText().toString();
			String city = mCity_Loyalty_Edittext.getText().toString();
			String state = mState_Loyalty_Edittext.getText().toString();

			//checking for the name
			if(!name.equals("")){
				//checking for the email verification
				if(isEmailValid(email_id) == true){
					//password checking
					if(!password_register.equals("")){
						//mobile details
						if(!mobile.equals("")){
							//area details checking
							if(!area.equals("")){

								user_details_url = "/CustomerReg?Customer_Name="+name+"&Customer_Email="+email_id+"&Customer_Phone="+mobile+"&User_Id="+Constants.catalodid
										+"&Customer_Pwd="+password_register+"&Customer_Address="+strt_address+"&Customer_Area="+area+"&Customer_City="+city
										+"&Customer_State="+state;


								if(isOnline()){
									registration_task = true;
									SignUpAsynchronousTask testCursor = new SignUpAsynchronousTask();
									testCursor.execute();
									username = email_id;
								} else {
									Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
								}

							} else {
								mArea_Loyalty_Edittext.setError(getString(R.string.enter_area));
								Toast.makeText(getActivity(), getString(R.string.enter_area), Toast.LENGTH_SHORT).show();
							}
						} else {
							mPhoneno_Loyalty_Edittext.setError(getString(R.string.enter_phone));
							Toast.makeText(getActivity(), getString(R.string.enter_phone), Toast.LENGTH_SHORT).show();
						}
					} else {
						mPassword_Loyalty_Edittext.setError(getString(R.string.enter_valid_password));
						Toast.makeText(getActivity(), getString(R.string.enter_valid_password), Toast.LENGTH_SHORT).show();
					}

				} else {
					mEmail_Loyalty_Edittext.setError(getString(R.string.enter_valid_email));
					Toast.makeText(getActivity(), getString(R.string.enter_valid_email), Toast.LENGTH_SHORT).show();
				}
			} else {
				mName_Loyalty_Edittext.setError(getString(R.string.enter_name));
				Toast.makeText(getActivity(), getString(R.string.enter_name), Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.rewards_checkin_button:

			long oldTime  = getLoyalTime() ;
			long time =  ci.getTimeInMillis()/(60*1000);

			// checking time diff 
			int time_diff = (int) Math.abs(oldTime - time);
			//			Log.d(TAG,"time_diff() ::"+time_diff);

			// if time diff is less than 24 hours not allowing users into checkin
			if(time_diff == 0 || time_diff >= 24){

				saveTime(time);

				checkin_pressed = true;
				mRewards_Checkin_Button.setEnabled(false);

				if(getClass_UserID() != 0){
					checkgpsandsearch();	
				} else {
					Toast.makeText(getActivity(), getString(R.string.not_logedin), Toast.LENGTH_SHORT).show();
				}
			}else{
				loyalityLimit("Oops! You can only Check-in once every 24 hours..");
			}
			break;

		case R.id.login_button:

			mLogin_layout.setVisibility(View.VISIBLE);

			close_VirtualKeyboards();

			String username = mUsername_editText.getText().toString();
			String password = mPassword_editText.getText().toString();

			if( !username.equals("") && !password.equals("") ){ 

				login_details = Constants.BASE_URL+"/LoyalityLoginCheck?Customer_Email="+username+"&Customer_Pwd="+password+"&User_Id="+Constants.catalodid;
				logIn(login_details);

			}else{

				mUsername_editText.setError(getString(R.string.error_username));
				mPassword_editText.setError(getString(R.string.error_password));
				Toast.makeText(getActivity(), getString(R.string.enter_valid_password), Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.signup_button:

			mLoyalty_Scrollview.setVisibility(View.VISIBLE);
			mSignup_Relativelayout.setVisibility(View.VISIBLE);
			mLogin_layout.setVisibility(View.GONE);

			break;

		default:
			break;
		}
	}

	private void checkgpsandsearch(){

		WifiManager wifi = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()){
			//wifi is enabled
			startLocating_user();
		} else {
			locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

			if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
				AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
				dialog.setTitle("Loyalty");
				dialog.setMessage("GPS is OFF, to confirm loyalty your location is required. Do you want to turn on GPS.");
				dialog.setButton("Yes",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						switch_GPS = true;
						Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
						startActivity(myIntent);
					}
				});
				dialog.setButton2("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
					}
				});

				dialog.show();
			} else {
				startLocating_user();
			}
		}


	}

	private void startLocating_user(){

		mLoyalty_Progressbar.setVisibility(View.VISIBLE);

		gpsClass = new GPSClass();

		LocationResult locationResult = new LocationResult(){
			@Override
			public void gotLocation(Location location){
				//Got the location!
				mLoyalty_Progressbar.setVisibility(View.GONE);

				calculate(location.getLatitude(), location.getLongitude());
			}
		};

		gpsClass.getLocation(getActivity(), locationResult);


	}

	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}

	/** Checking user login credentials and activating check in details 
	 * @param url url of the user login*/
	private void logIn(String url){

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				mLoyalty_Progressbar.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(String response) {
				// Successfully got a response
				if(response != null){

					try {
						JSONObject jsonObject = new JSONObject(response);
						String loginResponce = jsonObject.getString("getLoyalityLoginResult"); 

						if(loginResponce.equalsIgnoreCase("Invalid User")){
							Toast.makeText(getActivity(), "Invalid User", Toast.LENGTH_SHORT).show();
							mLoyalty_Progressbar.setVisibility(View.GONE);
							isLoginSucess = false;
						}else{
							if(loginResponce.equalsIgnoreCase("Error occured While Login")){
								isLoginSucess = false;
							}else{

								isLoginSucess = true;
								saveLoginResponce(loginResponce);
								setClass_UserID(Integer.parseInt(loginResponce));
							}
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Throwable e, String response) {
				// Response failed :(
				Log.e("errorr","eroor:::;"+response+"e::"+e);
			}
			@Override
			public void onFinish() {

				if(!isLoginSucess){
					mPassword_editText.setError("Please enter correct username");
					mUsername_editText.setError("Please enter correct Password");
				}else{
					mLogin_layout.setVisibility(View.GONE);

					mLoyalty_Scrollview.setVisibility(View.VISIBLE);
					mRewards_Relativelayout.setVisibility(View.VISIBLE);

					mSignup_Relativelayout.setVisibility(View.GONE);
					getloyalty_details_url = "/GetmyPoints?Customer_Id="+getClass_UserID();

					getLoyalPoints(Constants.BASE_URL+getloyalty_details_url);
				}
			}
		});
	}

	/** getting previous check in(loyality points)
	 * @param url of the laoyality points*/
	private void getLoyalPoints(String url){

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(String response) {
				// Successfully got a response
				if(response != null){

					JSONObject jsonObject;
					try {

						jsonObject = new JSONObject(response);
						String myLoayalPoints   = jsonObject.getString("Loyality_GetmyPointsResult");	
						//						Log.e("errorr","myLoayalPoints::::"+myLoayalPoints);
						setClass_savePoints(myLoayalPoints);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFinish() {

				mLoyalty_Progressbar.setVisibility(View.GONE);
				mRewards_Earned_Textview.setText(getClass_savePoints());
			}
		});
	}


	private void register_user(){

		String jArray_main = "" ;
		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.BASE_URL+user_details_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getString("Insert_NEWICatelog_Loyality_CustomerRegResult");			

				registration_info = jArray_main;

			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}


	private void insert_loyalty(){

		String jArray_main = "" ;
		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.BASE_URL+insertloyalty_details_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getString("Insert_NEWICatelog_Loyality_Loyality_CustomerPointsResult");			

				inserted_loyalty_state = jArray_main;
			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}


	}

	private void getMy_loyalty(){

		String jArray_main = "" ;
		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.BASE_URL+getloyalty_details_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getString("Loyality_GetmyPointsResult");			

				myloyalty_details = jArray_main;
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}


	/**
	 * Returns the network state
	 */
	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**Using Asyn_task for displaying the data  */
	private class SignUpAsynchronousTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>{

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(Void... arg0) {
			ArrayList<HashMap<String, Object>> events = new ArrayList<HashMap<String, Object>>();

			if(registration_task == true){
				register_user();
			} else if(insertpoints == true){
				insert_loyalty();
			} else if(myloyalty_task == true){
				getMy_loyalty();
			}

			return events;
		}

		@Override
		protected void onPreExecute() {
			// display progress here
			mLoyalty_Progressbar.setVisibility(View.VISIBLE);
		}

		//Can use UI thread here
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> results) {
			mLoyalty_Progressbar.setVisibility(View.GONE);

			if(registration_task == true){
				registration_task = false;

				if(registration_info.equalsIgnoreCase("Email Already Exists")){
					Toast.makeText(getActivity(), "Email Already Exists", Toast.LENGTH_SHORT).show();
					if(mSignup_Relativelayout.getVisibility() == View.VISIBLE){
						mSignup_Relativelayout.setVisibility(View.GONE);
						mLogin_layout.setVisibility(View.VISIBLE);
					}

				}else{

					if(!registration_info.equals("Registration Faild")){
						Toast.makeText(getActivity(), getString(R.string.registerd), Toast.LENGTH_SHORT).show();
						setClass_UserID(Integer.parseInt(registration_info));
						//after getting registered user will be given some free points
						if(isOnline()){
							insertpoints = true;						
							getLoyalty_id(loyalty_signup);
							storeid = (Integer) storelocation_details.get(0).get("store_id");
							insertloyalty_details_url = "/InsertCustomerPoints?Customer_Id="+getClass_UserID()+"&User_Id="+Constants.catalodid+"&Store_Id="+storeid+"&Loyality_Id="+loyalty_id+"&Date="+getDate()+"&Time="+getTime();
							SignUpAsynchronousTask testCursor = new SignUpAsynchronousTask();
							testCursor.execute();
						} else {
							Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
						}

					} else {
						Toast.makeText(getActivity(), getString(R.string.failed_registration), Toast.LENGTH_SHORT).show();
					}
				}
			} else if(insertpoints == true){
				insertpoints = false;
				if(inserted_loyalty_state.equals("INSERTED")){
					Toast.makeText(getActivity(), getString(R.string.loyalty_added), Toast.LENGTH_SHORT).show();

					if(isOnline()){
						myloyalty_task = true;
						getloyalty_details_url = "/GetmyPoints?Customer_Id="+getClass_UserID();
						SignUpAsynchronousTask testCursor = new SignUpAsynchronousTask();
						testCursor.execute();
					} else {
						Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(getActivity(), getString(R.string.loyalty_add_failed), Toast.LENGTH_SHORT).show();
				}
			} else if(myloyalty_task == true){
				myloyalty_task = false;
				if(mSignup_Relativelayout.getVisibility() == View.VISIBLE){
					mSignup_Relativelayout.setVisibility(View.GONE);
					mRewards_Relativelayout.setVisibility(View.VISIBLE);
				}

				mRewards_Earned_Textview.setText(myloyalty_details);
				setClass_savePoints(myloyalty_details);

			}

		}
	}

	/** Shared preference for checking weather initial data was download or not*/
	private void setClass_UserID(int userid){
		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("userid", userid);
		editor.commit();
	}
	private int getClass_UserID(){
		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		Integer status = preferences.getInt("userid", 0);
		return status;
	}

	/** Shared preference for checking weather log in sucess or not*/
	private void saveLoginResponce(String responce){
		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("login", responce);
		editor.commit();
	}
	/** Getting lgoin succes data*/
	private String getLoginResponce(){
		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		String status = preferences.getString("login", "");
		return status;
	}

	/** Shared preference for checking weather initial data was download or not*/
	private void setClass_savePoints(String loyalpoints){
		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("LOYAL", loyalpoints);
		editor.commit();
	}
	private String getClass_savePoints(){
		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		String status = preferences.getString("LOYAL", "");
		return status;
	}

	//returns the current date  // for both time and date use yyyyMMdd_HHmmss in simple date format
	private String getDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String currentDate = sdf.format(new Date());
		return currentDate;
	}

	//returns the current time
	private String getTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String currentTime = sdf.format(new Date());
		return currentTime;
	} 
	/**saving time for loyality check */
	private void saveTime(long time){

		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong("time", time);
		editor.commit();
	}

	/** getting time for laoyality check*/
	private long getLoyalTime(){

		SharedPreferences preferences = getActivity().getPreferences(getActivity().MODE_PRIVATE);
		long time = preferences.getLong("time", 0);
		return time;
	}


	public void checkin_loyalty(){ 

		if(isOnline()){
			insertpoints = true;			
			getLoyalty_id(loyalty_checkin);
			insertloyalty_details_url = "/InsertCustomerPoints?Customer_Id="+getClass_UserID()+"&User_Id="+Constants.catalodid+"&Store_Id="+storeid+"&Loyality_Id="+loyalty_id+"&Date="+getDate()+"&Time="+getTime();
			SignUpAsynchronousTask testCursor = new SignUpAsynchronousTask();
			testCursor.execute();

			mRewards_Checkin_Button.setEnabled(true);	
		} else {
			Toast.makeText(getActivity(), getString(R.string.no_network), Toast.LENGTH_SHORT).show();
		}

	}

	/**This method will get the details based on table
	 * With the sequence as the criteria 
	 * Returns the data in ascending order of sequence*/
	private Cursor getEvents(String table) {
		SQLiteDatabase db = (sqlhelper).getReadableDatabase();
		Cursor cursor = db.query(table, null, null, null, null, null, null);

		return cursor;
	}

	/**This method will get the details from database based on Query */
	private Cursor getRawEvents(String sql) {
		SQLiteDatabase db = (sqlhelper).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		return cursor;
	}

	private void getStore_Details(){

		storelocation_details = new ArrayList<HashMap<String,Object>>();

		Cursor cursor = getEvents("STORELOCATOR");
		if(cursor != null){
			//while loop for fetching all the data
			while (cursor.moveToNext()) {
				hm = new HashMap<String, Object>();

				hm.put("latitude", cursor.getString(cursor.getColumnIndex("Latitude")));	
				hm.put("longitude", cursor.getString(cursor.getColumnIndex("Longitude")));
				hm.put("store_id", cursor.getInt(cursor.getColumnIndex("Store_id")));

				storelocation_details.add(hm);
			}
		}
	}

	private void getLoyalty_id(String loyal_typeid){

		//Query represents the position from where the data has to be fetched
		String 	qurey = "select * from "+"LOYALTY_TYPES"+" where "+
				"LOYALTY_TYPES"+"."+"LOYALTY_ID"+" = \"" + loyal_typeid + "\"";

		Cursor cursor = getRawEvents(qurey);

		try {
			if(cursor != null){
				while (cursor.moveToNext()) {

					loyalty_id = cursor.getString(cursor.getColumnIndex("LOYALTY_ID"));
				}
			}

		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	/** displaying alert box if loyality limit is below 24 hours*/
	private void loyalityLimit(String  message){
		try {
			AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
			dialog.setTitle("Loyalty");
			dialog.setMessage(message);
			dialog.setButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();

				}
			});

			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void close_VirtualKeyboards(){

		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mName_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mEmail_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mPassword_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mPhoneno_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mStrt_Address_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mArea_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mCity_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mState_Loyalty_Edittext.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mUsername_editText.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(mPassword_editText.getWindowToken(), 0);

	}

	private void calculate(double latitude, double longitude){

		//for saving the distance values
		storelocation_distance = new ArrayList<Integer>();

		//iterating all the latitude values and getting the distance of all
		for(int i = 0 ; i <= storelocation_details.size()-1;i++ ){
			//finding the distance between two places
			double store_distance = distance(latitude, longitude, Double.valueOf((String) storelocation_details.get(i).get("latitude")), 
					Double.valueOf((String) storelocation_details.get(i).get("longitude")));

			//converting the value into meters
			double store_in_meters = store_distance * 1000;

			//converting to integer, the double values
			int store_convertedTOint = (int) store_in_meters;

			//saving the converted distance value
			storelocation_distance.add(store_convertedTOint);
		}

		//getting the ieast distance value from the saved list
		int lest_distance = Collections.min(storelocation_distance);

		if(lest_distance <= 50){

			//finding the position of the value so that the store id of that can be found and loyalty for that particular store is assigned
			int store_position = 0;
			for (int i = 0; i < storelocation_distance.size() - 1; i++) {

				if (String.valueOf(lest_distance).equals(String.valueOf(storelocation_distance.get(i)))) {
					store_position = i;
					break;
				}
			}

			//gets the store id based on the above position calculation
			storeid = (Integer) storelocation_details.get(store_position).get("store_id");

			//display the alert dialog
			earned_loyalty(lest_distance);	
		} else {

			//display the alert dialog
			no_loyalty(lest_distance);	
		}

	}

	private void earned_loyalty(Integer message){
		try {
			AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
			dialog.setTitle("Loyalty");
			dialog.setMessage("Thank you for visiting our store.");
			dialog.setButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();

				}
			});

			dialog.show();

			checkin_loyalty();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void no_loyalty(Integer message){
		try {
			AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
			dialog.setTitle("Loyalty");
			dialog.setMessage("Your current distance from our nearest store is "+message+" meters, you should atleast be 50 meters to earn loyalty points.");
			dialog.setButton("Ok",
					new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});

			dialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		return (dist);
	}

	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}

}
