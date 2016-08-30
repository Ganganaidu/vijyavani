package com.switchsoft.vijayavani.asynctask;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.base64.Base64;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.switchsoft.vijayavani.SplashScreenActivity;
import com.switchsoft.vijayavani.model.OffersModel;
import com.switchsoft.vijayavani.util.Constants;

public class GetNewDataOnPush extends AsyncTask<Void, Void, Boolean>{

	private ArrayList<HashMap<String, Object>> timeStampList; 
	private boolean serverDownFlag = false;
	private boolean get_offers = false;
	private String timeStampDate = "";
	String saved_timestamp_date = "";

	OffersModel offermodel;

	@Override
	protected Boolean doInBackground(Void... arg0) {

		//Initializing the hash map array list 
		timeStampList = new ArrayList<HashMap<String, Object>>();	
		offermodel = new OffersModel();

		getTimeStampForNewData();

		if(serverDownFlag == false){

			for(int i = 0;i <= timeStampList.size()-1;i++){

				String category = (String) timeStampList.get(i).get("category_name");
				String date = (String) timeStampList.get(i).get("update_date");

				if(category.equals(Constants.timestamp_Offers)){
					get_timestamp_from_database(category);
					if(!date.equals(saved_timestamp_date)){
						get_offers = true;
						timeStampDate = date;
					} 

				}

			}

			if(get_offers){
				getOffers();
				SplashScreenActivity.sqlhelper.UpdateTimestampData(Constants.timestamp_Offers, timeStampDate);
			}
			return true;
		}
		else{
			return false;
		}

	}

	@Override
	protected void onPreExecute() {
		// display progress here
		//		mPercentage_Complete_Textview.setText("Fetching New Offers");
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(Boolean result) {
		if(!isCancelled()){
			if(result){
				SplashScreenActivity.splashscreen.openMainActivity();
			}else{

				Toast.makeText(SplashScreenActivity.splashscreen, "Unable to fetch data! Try Again Later",
						Toast.LENGTH_LONG).show();
				SplashScreenActivity.splashscreen.openMainActivity();
			}
		}
	}

	private void getTimeStampForNewData(){

		timeStampList = new ArrayList<HashMap<String, Object>>();	

		JSONArray jArray_main;

		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.timestamp_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(25000);
			tc1.setReadTimeout(25000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Update_ICatelog_UpdateResult");			

				JSONObject main_category = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{
					HashMap<String, Object> hm = new HashMap<String, Object>();	

					main_category  = jArray_main.getJSONObject(i);

					String image_link = main_category.getString("CategoryName");
					hm.put("category_name", image_link);

					String cat_name = main_category.getString("UpdateDate");
					hm.put("update_date", cat_name);

					timeStampList.add(hm);

				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			serverDownFlag = true;
		}

	}



	private void get_timestamp_from_database(String category){
		//Query represents the position from where the data has to be fetched
		String 	qurey = "select * from "+"TIMESTAMP"+" where "+
				"TIMESTAMP"+"."+"Category_Name"+" = \"" +  category + "\"";

		Cursor cursor = SplashScreenActivity.splashscreen.getRawEvents(qurey);

		try {
			if(cursor != null){
				cursor.moveToFirst();

				saved_timestamp_date = cursor.getString(cursor.getColumnIndex("Updated_Date"));		

				cursor.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}






	/**Downloading the background data from the server*/
	private void getOffers(){

		JSONArray jArray_main;

		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.offers_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				SplashScreenActivity.sqlhelper.deleteOffersData();		

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Get_ICatelog_OfferResult");			

				JSONObject offer = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{
					offer  = jArray_main.getJSONObject(i);

					Bitmap bitmap = DownloadImage(offer.getString("Image"));

					offermodel.offer_description	= offer.getString("Description");
					offermodel.offer_heading		= offer.getString("Heading");					
					offermodel.offer_image_link	= offer.getString("Image");
					offermodel.offer_id			= offer.getString("Offer_Id");
					offermodel.offer_status		= offer.getString("Status");
					offermodel.offer_store_id 	= offer.getString("Store_Id");
					offermodel.offer_time 		= offer.getString("Time");
					offermodel.offer_user_id 		= offer.getInt("User_Id");
					offermodel.offer_valid_from 	= offer.getString("Valid_From");
					offermodel.offer_valid_to 	= offer.getString("Valid_To");

					//encoding the image to string so that it can be saved into database
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
					byte[] image = baos.toByteArray();
					//					String bitmaptString = Base64.encode(image);
					String bitmaptString = Base64.encode(image);

					offermodel.offer_image		= bitmaptString;

					SplashScreenActivity.sqlhelper.insertOffersData(offermodel);
				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}


	/**Downloads the image and returns the bitmap*/
	private Bitmap DownloadImage(String  URL )
	{        
		Bitmap bitmap = null;
		InputStream  in = null;        
		try {
			in = OpenHttpConnection(URL );
			bitmap = BitmapFactory.decodeStream(in);
			in.close();
		} catch (IOException  e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bitmap;                
	}


	//For connecting to the server
	private InputStream  OpenHttpConnection(String  urlString) 
			throws IOException {
		InputStream  in = null;
		int response = -1;

		URL  url = new URL (urlString); 
		URLConnection  conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection ))                     
			throw new IOException ("Not an HTTP connection");

		try{
			HttpURLConnection  httpConn = (HttpURLConnection ) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			response = httpConn.getResponseCode();                 
			if (response == HttpURLConnection .HTTP_OK) {
				in = httpConn.getInputStream();                                 
			}                     
		}
		catch (Exception  ex)
		{
			throw new IOException ("Error connecting");            
		}
		return in;     
	}

}
