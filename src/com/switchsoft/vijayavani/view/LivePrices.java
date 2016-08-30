package com.switchsoft.vijayavani.view;

import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.switchsoft.vijayavani.R;

public class LivePrices extends Fragment {
	
	public static LivePrices liveprices = null; 
	
	CountDownTimer feed_update_timer;

	private static final String NAMESPACE = "http://svbcgold.com/";
	private static final String URL = "http://svbcgold.com/LivePriceService.asmx";	
	private static final String SOAP_ACTION = "http://tempuri.org/getSVBCPrice";
	private static final String METHOD_NAME = "getSVBCPrice";

	String result = null;
	Object resultRequestSOAP = null;

	GetFeedAsynchronousTask feedCursor;

	TextView mCost_Spotgold_Textview, mCost_Spotsilver_Textview, mCost_Spotgold_NineOneSix_Textview, 
	mCost_Gold_Textview, mCost_Silver_Textview, mCost_Usd_Textview;

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

		View view = inflater.inflate(R.layout.livepricesview, container, false);

		mCost_Spotgold_Textview = (TextView)view.findViewById(R.id.cost_spotgold_textview);
		mCost_Spotsilver_Textview = (TextView)view.findViewById(R.id.cost_spotsilver_textview);
		mCost_Spotgold_NineOneSix_Textview = (TextView)view.findViewById(R.id.cost_spotgold_nineonesix_textview); 
		mCost_Gold_Textview = (TextView)view.findViewById(R.id.cost_gold_textview);
		mCost_Silver_Textview = (TextView)view.findViewById(R.id.cost_silver_textview);
		mCost_Usd_Textview = (TextView)view.findViewById(R.id.cost_usd_textview);


		//closing all the views depending on the data the views will be made visible
		//mContext_Relativelayout.setVisibility(View.GONE);

		return view;
	}

	// after onCreateview this method will call..so put all listeners in this class
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		liveprices = this;
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
	
	public void startConnection(){
		feedCursor = new GetFeedAsynchronousTask();
		feedCursor.execute();
		
	}
	
	public void stopConnection(){
		try {
			feed_update_timer.cancel();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
	/**
	 * Using Timer for going to next screen after a specified time
	 */
	private void feed_timer(long millis,long countdown){

		feed_update_timer = new CountDownTimer(millis, countdown) {
			public void onTick(long millisUntilFinished) {

			}
			//when count down completes
			public void onFinish() {
				if(isOnline()){
					feedCursor = new GetFeedAsynchronousTask();
					feedCursor.execute();
				} else {
					
				}

			}
		};
		feed_update_timer.start();
	}


	/**
	 * Returns the network state
	 */
	private boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	
	/**Using Asyn_task for displaying the data  */
	public class GetFeedAsynchronousTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... arg0) {
			try {
				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);

				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				androidHttpTransport.debug = true;

				androidHttpTransport.call(SOAP_ACTION, envelope);

				String requestDumpString = androidHttpTransport.requestDump;

				resultRequestSOAP = envelope.getResponse(); // Output received
				result = resultRequestSOAP.toString(); // Result string

			} catch (Exception e) {
				result = "";
			}
			return result;
		}

		@Override
		protected void onPreExecute() {

		}

		//Can use UI thread here
		@Override
		protected void onPostExecute(String results) {

			if(!results.equals("")){
				try {
					JSONObject jsonObject;

					jsonObject = new JSONObject(results);

					String spotGold999 = jsonObject.getString("SpotGold999");
					String spotSilver999 = jsonObject.getString("SpotSilver999");
					String spotGold916 = jsonObject.getString("SpotGold916");
					String goldcmx = jsonObject.getString("GoldCMX");
					String silvercmx = jsonObject.getString("SilverCMX");
					String usdinr = jsonObject.getString("USDInr");

					mCost_Spotgold_Textview.setText(spotGold999);
					mCost_Spotsilver_Textview.setText(spotSilver999);
					mCost_Spotgold_NineOneSix_Textview.setText(spotGold916);
					mCost_Gold_Textview.setText(goldcmx);
					mCost_Silver_Textview.setText(silvercmx);
					mCost_Usd_Textview.setText(usdinr);


					//colors
					String spotGold999_color = jsonObject.getString("colrGold999");
					String spotSilver999_color = jsonObject.getString("colrSilver999");
					String spotGold916_color = jsonObject.getString("colrGold916");
					String goldcmx_color = jsonObject.getString("colrGoldCMX");
					String silvercmx_color = jsonObject.getString("colrSilverCMX");
					String usdinr_color = jsonObject.getString("colrUSDInr");


					//changing the color
					if(spotGold999_color.equals("black")){
						mCost_Spotgold_Textview.setTextColor(getResources().getColor(R.color.live_black));
					} else if(spotGold999_color.equals("red")){
						mCost_Spotgold_Textview.setTextColor(getResources().getColor(R.color.red));
					} else {
						mCost_Spotgold_Textview.setTextColor(getResources().getColor(R.color.green));
					}

					if(spotSilver999_color.equals("black")){
						mCost_Spotsilver_Textview.setTextColor(getResources().getColor(R.color.live_black));
					} else if(spotGold999_color.equals("red")){
						mCost_Spotsilver_Textview.setTextColor(getResources().getColor(R.color.red));
					} else {
						mCost_Spotsilver_Textview.setTextColor(getResources().getColor(R.color.green));
					}

					if(spotGold916_color.equals("black")){
						mCost_Spotgold_NineOneSix_Textview.setTextColor(getResources().getColor(R.color.live_black));
					} else if(spotGold999_color.equals("red")){
						mCost_Spotgold_NineOneSix_Textview.setTextColor(getResources().getColor(R.color.red));
					} else {
						mCost_Spotgold_NineOneSix_Textview.setTextColor(getResources().getColor(R.color.green));
					}

					if(goldcmx_color.equals("black")){
						mCost_Gold_Textview.setTextColor(getResources().getColor(R.color.live_black));
					} else if(spotGold999_color.equals("red")){
						mCost_Gold_Textview.setTextColor(getResources().getColor(R.color.red));
					} else {
						mCost_Gold_Textview.setTextColor(getResources().getColor(R.color.green));
					}

					if(silvercmx_color.equals("black")){
						mCost_Silver_Textview.setTextColor(getResources().getColor(R.color.live_black));
					} else if(spotGold999_color.equals("red")){
						mCost_Silver_Textview.setTextColor(getResources().getColor(R.color.red));
					} else {
						mCost_Silver_Textview.setTextColor(getResources().getColor(R.color.green));
					}

					if(usdinr_color.equals("black")){
						mCost_Usd_Textview.setTextColor(getResources().getColor(R.color.live_black));
					} else if(spotGold999_color.equals("red")){
						mCost_Usd_Textview.setTextColor(getResources().getColor(R.color.red));
					} else {
						mCost_Usd_Textview.setTextColor(getResources().getColor(R.color.green));
					}

					feed_timer(2000,500);
				} catch (Exception e) {

				}


			}
		}
	}

}
