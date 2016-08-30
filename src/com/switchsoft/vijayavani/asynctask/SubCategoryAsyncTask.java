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
import com.switchsoft.vijayavani.model.SubCategoryModel;
import com.switchsoft.vijayavani.util.Constants;

public class SubCategoryAsyncTask  extends AsyncTask<Void, Void, ArrayList<String>> {
	
	ArrayList<String> dumyarray;
	SubCategoryModel subcategorymodel;
	
	@Override
	protected ArrayList<String>  doInBackground(Void... arg0) {
		
		subcategorymodel = new SubCategoryModel();
		
		//gets the sub category data
		getSub_category();
		
 		return dumyarray;
	}
	
	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<String> results) {
		SplashScreenActivity.splashscreen.update_percentage("30");
		SplashScreenActivity.splashscreen.onSubCategoryComplete();
		
	}
	
	
	/**Downloading the background data from the server*/
	private void getSub_category(){

		JSONArray jArray_main;
		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.sub_category__url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				SplashScreenActivity.sqlhelper.deleteSubCategoryData();

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Get_ICatelog_Sub_CategoryResult");			

				JSONObject sub_category = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{
					sub_category = jArray_main.getJSONObject(i);

					subcategorymodel.category_name		= sub_category.getString("Category");
					subcategorymodel.sub_cat_isactive	= sub_category.getInt("IsActive");					
					subcategorymodel.main_cat_id		= sub_category.getInt("MainCat_Id");
					subcategorymodel.sub_cat_description = sub_category.getString("Sub_Cat_Desc");
					subcategorymodel.sub_cat_id			= sub_category.getInt("Sub_Cat_Id");
					subcategorymodel.sub_cat_name		= sub_category.getString("Sub_Cat_Name");
					subcategorymodel.user_id			= sub_category.getInt("User_Id");
					subcategorymodel.sub_cat_price 		= sub_category.getString("Price");

					SplashScreenActivity.sqlhelper.insertSubCategoryData(subcategorymodel);

				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
}
