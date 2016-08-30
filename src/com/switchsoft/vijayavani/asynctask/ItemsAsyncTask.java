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
import com.switchsoft.vijayavani.model.ItemModel;
import com.switchsoft.vijayavani.util.Constants;

public class ItemsAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {
	
	ArrayList<String> dumyarray;
	ItemModel itemmodel;
	
	@Override
	protected ArrayList<String>  doInBackground(Void... arg0) {
		
		itemmodel = new ItemModel();
		//gets the items
		getItems();
		
 		return dumyarray;
	}
	
	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<String> results) {
		SplashScreenActivity.splashscreen.update_percentage("40");
		SplashScreenActivity.splashscreen.onItemComplete();
	}
	
	
	/**Downloading the background data from the server*/
	private void getItems(){

		JSONArray jArray_main;
		JSONObject jsonObject;

		try
		{
			URL maincatUrl = new URL(Constants.item_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				SplashScreenActivity.sqlhelper.deleteItemsData();

				jsonObject = new JSONObject(line);
				jArray_main  = jsonObject.getJSONArray("Get_ICatelog_Item_CategoryResult");			

				JSONObject item = new JSONObject();

				for (int i = 0; i < jArray_main.length(); i++) 
				{
					item = jArray_main.getJSONObject(i);

					itemmodel.item_category		= item.getString("Category");
					itemmodel.item_is_active		= item.getInt("IsActive");					
					itemmodel.item_item_desc		= item.getString("Item_Desc");
					itemmodel.item_item_id			= item.getInt("Item_Id");
					itemmodel.item_item_name 		= item.getString("Item_Name");
					itemmodel.item_item_price 		= item.getString("Item_Price");
					itemmodel.item_main_cat_id 	= item.getInt("Main_Cat_Id");
					itemmodel.item_sub_cat_id 		= item.getInt("Sub_Cat_Id");
					itemmodel.item_user_id 		= item.getInt("User_Id");
					itemmodel.item_image_link	    = item.getString("Item_Image");

					SplashScreenActivity.sqlhelper.insertItemsData(itemmodel);

				}

			}

		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

}
