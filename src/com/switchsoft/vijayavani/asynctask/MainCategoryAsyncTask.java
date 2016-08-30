package com.switchsoft.vijayavani.asynctask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.switchsoft.vijayavani.SplashScreenActivity;
import com.switchsoft.vijayavani.model.MainCategoryModel;
import com.switchsoft.vijayavani.util.Constants;

public class MainCategoryAsyncTask extends AsyncTask<Void, Void, ArrayList<String>>  {
	
	ArrayList<String> dumyarray;
	MainCategoryModel maincategorymodel;
	
 	@Override
	protected ArrayList<String>  doInBackground(Void... arg0) {
		
 		maincategorymodel = new MainCategoryModel();
 		
 		//gets the main category data and saves in database
 		getMain_category();
 		
 		return dumyarray;
	}
	
	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<String> results) {
		SplashScreenActivity.splashscreen.update_percentage("20");
		SplashScreenActivity.splashscreen.onMainCategoryComplete();
		
	}
	
	/**Downloading the background data from the server*/
	private void getMain_category(){

		JSONArray jArray_main;
		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.main_category_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				SplashScreenActivity.sqlhelper.deleteMainCategoryData();

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Get_ICatelog_Main_CategoryResult");			

				JSONObject main_category = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{
					main_category  = jArray_main.getJSONObject(i);

					maincategorymodel.main_cat_name		= main_category.getString("Cat_Name");					
					maincategorymodel.category_name		= main_category.getString("Category_Name");
					maincategorymodel.main_cat_description	= main_category.getString("Description");
					maincategorymodel.main_cat_isactive	= main_category.getInt("IsActive");
					maincategorymodel.main_cat_id 			= main_category.getInt("MainCat_Id");
					maincategorymodel.user_id 				= main_category.getInt("User_Id");
					maincategorymodel.main_price			= main_category.getString("Price");

					SplashScreenActivity.sqlhelper.insertMainCategoryData(maincategorymodel);
				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}
