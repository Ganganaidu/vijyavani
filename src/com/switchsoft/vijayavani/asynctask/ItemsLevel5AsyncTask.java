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
import com.switchsoft.vijayavani.model.ItemModel5;
import com.switchsoft.vijayavani.util.Constants;

public class ItemsLevel5AsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

	ArrayList<String> dumyarray;
	ItemModel5 itemmodel;

	@Override
	protected ArrayList<String>  doInBackground(Void... arg0) {

		itemmodel = new ItemModel5();
		//Gets the item data
		getItemslevel5();
		return dumyarray;
	}

	@Override
	protected void onPreExecute() {
		// display progress here
	}


	//Can use UI thread here
	@Override
	protected void onPostExecute(ArrayList<String> results) {
		SplashScreenActivity.splashscreen.update_percentage("60");
		SplashScreenActivity.splashscreen.onItem5Complete();

	}

	/**Downloading the items from server*/
	private void getItemslevel5(){

		try{

			URL maincatUrl = new URL(Constants.itemlevel5_url);
			URLConnection tc1 = maincatUrl.openConnection(); 
			tc1.setConnectTimeout(10000);

			BufferedReader input = new BufferedReader(new InputStreamReader(tc1.getInputStream()));
			String line;

			while ((line = input.readLine()) != null) {

				JSONObject jsonObject = new JSONObject(line);
				JSONArray jArray_main  = jsonObject.getJSONArray("Get_ICatelog_Item_Level5Result");			
				JSONObject item = new JSONObject();

				int level_length = jArray_main.length();
				if(level_length != 0){

					SplashScreenActivity.sqlhelper.deleteItemsData_Level5();

					for (int i = 0; i < level_length; i++) { //check

						item  = jArray_main.getJSONObject(i);

						itemmodel.item_category = item.getString("Category");

						itemmodel.item_is_active	= item.getInt("IsActive");
						itemmodel.item_item_desc	= item.getString("Item_Desc");
						itemmodel.item_item_id		= item.getInt("Item_Id");
						itemmodel.item_image_link	= item.getString("Item_Image");
						itemmodel.item_item_name	= item.getString("Item_Name");
						itemmodel.item_item_price 	= item.getString("Item_Price");
						itemmodel.item_main_cat_id	= item.getInt("Main_Cat_Id");
						itemmodel.item_sub_item_id4	= item.getInt("SubItem_Id4");
						itemmodel.item_sub_item_id5	= item.getInt("SubItem_Id5");
						itemmodel.item_sub_cat_id	= item.getInt("Sub_Cat_Id");
						itemmodel.item_user_id		= item.getInt("User_Id");

						SplashScreenActivity.sqlhelper.insertItemsData_level5(itemmodel);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
