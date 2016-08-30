package com.switchsoft.vijayavani.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.switchsoft.vijayavani.model.ItemModel;
import com.switchsoft.vijayavani.model.ItemModel4;
import com.switchsoft.vijayavani.model.ItemModel5;
import com.switchsoft.vijayavani.model.Loyalty_Types;
import com.switchsoft.vijayavani.model.MainCategoryModel;
import com.switchsoft.vijayavani.model.OffersModel;
import com.switchsoft.vijayavani.model.StoreLocatorModel;
import com.switchsoft.vijayavani.model.SubCategoryModel;

public class SQLHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Vijayavani.db";
	private static final int DATABASE_VERSION = 1;

	public SQLHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/** Helper to the database, manages versions and creation */
	public SQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String event_table = "CREATE TABLE EVENT (Event_Image varchar(10000) , Event_Image_Link varchar(160) UNIQUE, " +
				"Event_id integer , Event_Description varchar(1000) , Event_Heading varchar(500) , Event_status integer , " +
				"Event_Time varchar(160) , Event_User_Id integer , Event_Date varchar(160))";

		String offers_table = "CREATE TABLE OFFERS (Offer_Description varchar(1000) , Offer_Heading varchar(500) , " +
				"Offer_Image varchar(10000) , Offer_Image_Link varchar(160) UNIQUE, Offer_id integer ,  " +
				"Offer_status varchar(160) , Offer_Strore_id varchar(160) , Offer_Time varchar(500) , Offer_User_Id integer , " +
				"Offer_Valid_from varchar(500) , Offer_Valid_to varchar(500))";

		String storelocator_table = "CREATE TABLE STORELOCATOR (Email_id varchar(500) , Latitude varchar(160) , " +
				"Longitude varchar(160) , Location varchar(500) , Phone_no varchar(160) , Street_Address varchar(2000) , " +
				"Store_id integer UNIQUE , Store_User_Id integer , Store_Name varchar(160))";

		String mainCategory_table = "CREATE TABLE MAINCATEGORY (Main_Cat_Name varchar(160) , Category_Name varchar(160) , Main_CatDescription varchar(2000) , Main_CatIsActive integer , " +
				"Main_Cat_Id integer  , User_Id integer , Main_Price varchar(160))";

		String subCategory_table = "CREATE TABLE SUBCATEGORY (Category_Name varchar(160), Sub_Cat_IsActive integer , MainCat_Id integer , " +
				"Sub_Cat_Description varchar(2000) , Sub_Cat_Id integer , Sub_Cat_Name varchar(160) , User_Id integer , Sub_Cat_Price varchar(160))";

		String items_table = "CREATE TABLE ITEMS (Item_Category varchar(160) , Item_IsActive integer  , Item_Description varchar(2000)  ," +
				" Item_Id integer , Item_Name varchar(160) , Item_Price varchar(160)  , Item_Main_Cat_Id integer , " +
				"Item_Sub_Cat_Id integer , Item_User_Id integer , Item_Image varchar(15000) , Item_Image_Link varchar(160))";
		
		String items_table_level4 = "CREATE TABLE ITEMS_LEVEL4 (Item_Category varchar(160) , Item_IsActive integer  , Item_Description varchar(2000)  ," +
				" Item_Id integer , Item_Image varchar(15000) , Item_Name varchar(160) , Item_Price varchar(160)  , Item_Main_Cat_Id integer , " +
				"SubItem_Id4 integer , Item_Sub_Cat_Id integer , Item_User_Id integer, Item_Image_Link varchar(160))";
		
		String items_table_level5 = "CREATE TABLE ITEMS_LEVEL5 (Item_Category varchar(160) , Item_IsActive integer  , Item_Description varchar(2000)  ," +
				" Item_Id integer , Item_Image varchar(15000) , Item_Name varchar(160) , Item_Price varchar(160)  , Item_Main_Cat_Id integer , " +
				"SubItem_Id4 integer , SubItem_Id5 integer , Item_Sub_Cat_Id integer , Item_User_Id integer, Item_Image_Link varchar(160))";

		String timestamp = "CREATE TABLE TIMESTAMP (Category_Name varchar(160) , Updated_Date varchar(160) )";
		
		String loyalty_userdetails = "CREATE TABLE LOYALTY_TYPES (LOYALTY_ID integer, LOYALTY_TYPE varchar(160), STOREID integer , " +
				"USERID integer)";

		db.execSQL(event_table);
		db.execSQL(offers_table);
		db.execSQL(storelocator_table);
		db.execSQL(mainCategory_table);
		db.execSQL(subCategory_table);
		db.execSQL(items_table);
		db.execSQL(items_table_level4);
		db.execSQL(items_table_level5);
		db.execSQL(timestamp);
		db.execSQL(loyalty_userdetails);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		if (oldVersion >= newVersion)
			return;

		if (oldVersion == 1) {
			Log.d("New Version", "Datas can be upgraded");
		}

		Log.d("Sample Data", "onUpgrade	: " + newVersion);
	}
	
	public void insertStorelocatorData(StoreLocatorModel storelocation) {
		try{
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Email_id",storelocation.email_id);
			values.put("Latitude",storelocation.latitude);
			values.put("Longitude",storelocation.longitude);
			values.put("Location",storelocation.location);
			values.put("Phone_no",storelocation.phone_no);
			values.put("Street_Address",storelocation.street_address);
			values.put("Store_id",storelocation.store_id);
			values.put("Store_User_Id",storelocation.userid);
			values.put("Store_Name", storelocation.store_name);

			db.insert("STORELOCATOR", null, values);

		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void insertOffersData(OffersModel offermodel){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Offer_Description",offermodel.offer_description);
			values.put("Offer_Heading",offermodel.offer_heading);
			values.put("Offer_Image",offermodel.offer_image);
			values.put("Offer_Image_Link",offermodel.offer_image_link);
			values.put("Offer_id",offermodel.offer_id);
			values.put("Offer_status",offermodel.offer_status);
			values.put("Offer_Strore_id",offermodel.offer_store_id);
			values.put("Offer_Time",offermodel.offer_time);
			values.put("Offer_User_Id",offermodel.offer_user_id);
			values.put("Offer_Valid_from",offermodel.offer_valid_from);
			values.put("Offer_Valid_to",offermodel.offer_valid_to);


			db.insert("OFFERS", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void insertItemsData(ItemModel itemmodel){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Item_Category",itemmodel.item_category);
			values.put("Item_IsActive",itemmodel.item_is_active);
			values.put("Item_Description",itemmodel.item_item_desc);
			values.put("Item_Id",itemmodel.item_item_id);			
			values.put("Item_Name",itemmodel.item_item_name);
			values.put("Item_Price",itemmodel.item_item_price);
			values.put("Item_Main_Cat_Id",itemmodel.item_main_cat_id);
			values.put("Item_Sub_Cat_Id",itemmodel.item_sub_cat_id);
			values.put("Item_User_Id",itemmodel.item_user_id);
			values.put("Item_Image",itemmodel.item_image);
			values.put("Item_Image_Link",itemmodel.item_image_link);
			

			db.insert("ITEMS", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void insertItemsData_level4(ItemModel4 itemmodel){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Item_Category",itemmodel.item_category);
			values.put("Item_IsActive",itemmodel.item_is_active);
			values.put("Item_Description",itemmodel.item_item_desc);
			values.put("Item_Id",itemmodel.item_item_id);			
			values.put("Item_Name",itemmodel.item_item_name);
			values.put("Item_Price",itemmodel.item_item_price);
			values.put("Item_Main_Cat_Id",itemmodel.item_main_cat_id);
			values.put("SubItem_Id4",itemmodel.item_sub_item_id);
			values.put("Item_Sub_Cat_Id",itemmodel.item_sub_cat_id);
			values.put("Item_User_Id",itemmodel.item_user_id);
			values.put("Item_Image",itemmodel.item_image);
			values.put("Item_Image_Link",itemmodel.item_image_link);
			

			db.insert("ITEMS_LEVEL4", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void insertItemsData_level5(ItemModel5 itemmodel){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Item_Category",itemmodel.item_category);
			values.put("Item_IsActive",itemmodel.item_is_active);
			values.put("Item_Description",itemmodel.item_item_desc);
			values.put("Item_Id",itemmodel.item_item_id);			
			values.put("Item_Name",itemmodel.item_item_name);
			values.put("Item_Price",itemmodel.item_item_price);
			values.put("Item_Main_Cat_Id",itemmodel.item_main_cat_id);
			values.put("SubItem_Id4",itemmodel.item_sub_item_id4);
			values.put("SubItem_Id5",itemmodel.item_sub_item_id5);
			values.put("Item_Sub_Cat_Id",itemmodel.item_sub_cat_id);
			values.put("Item_User_Id",itemmodel.item_user_id);
			values.put("Item_Image",itemmodel.item_image);
			values.put("Item_Image_Link",itemmodel.item_image_link);
			

			db.insert("ITEMS_LEVEL5", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void insertMainCategoryData(MainCategoryModel maincategorymodel){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Main_Cat_Name",maincategorymodel.main_cat_name);
			values.put("Category_Name",maincategorymodel.category_name);
			values.put("Main_CatDescription",maincategorymodel.main_cat_description);
			values.put("Main_CatIsActive",maincategorymodel.main_cat_isactive);
			values.put("Main_Cat_Id",maincategorymodel.main_cat_id);
			values.put("User_Id",maincategorymodel.user_id);
			values.put("Main_Price", maincategorymodel.main_price);

			db.insert("MAINCATEGORY", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void insertSubCategoryData(SubCategoryModel subcategorymodel){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Category_Name",subcategorymodel.category_name);
			values.put("Sub_Cat_IsActive",subcategorymodel.sub_cat_isactive);
			values.put("MainCat_Id",subcategorymodel.main_cat_id);
			values.put("Sub_Cat_Description",subcategorymodel.sub_cat_description);
			values.put("Sub_Cat_Id",subcategorymodel.sub_cat_id);			
			values.put("Sub_Cat_Name",subcategorymodel.sub_cat_name);
			values.put("User_Id",subcategorymodel.user_id);
			values.put("Sub_Cat_Price", subcategorymodel.sub_cat_price);

			db.insert("SUBCATEGORY", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void insertTimestampData(String category_name, String date){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("Category_Name",category_name);
			values.put("Updated_Date",date);

			db.insert("TIMESTAMP", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void UpdateTimestampData(String category_name, String date){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();
			String catName="Category_Name = '"+category_name+"'";
			values.put("Updated_Date",date);

			db.update("TIMESTAMP", values, catName, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	public void insertLoyalty_Types(Loyalty_Types loyal_user){

		try {
			SQLiteDatabase db = getWritableDatabase();
			ContentValues values = new ContentValues();

			values.put("LOYALTY_ID", loyal_user.loyalty_id);
			values.put("LOYALTY_TYPE", loyal_user.loyalty_type);
			values.put("STOREID", loyal_user.store_id);
			values.put("USERID",loyal_user.user_id);

			db.insert("LOYALTY_TYPES", null, values);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void deleteLoyaltyData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("LOYALTY_TYPES", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteTimestampData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("TIMESTAMP", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteEventData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("EVENT", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteOffersData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("OFFERS", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteStoreLocatorData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("STORELOCATOR", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteMainCategoryData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("MAINCATEGORY", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteSubCategoryData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("SUBCATEGORY", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteItemsData(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("ITEMS", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void deleteItemsData_Level4(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("ITEMS_LEVEL4", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void deleteItemsData_Level5(){

		try{
			SQLiteDatabase db = getWritableDatabase();

			db.delete("ITEMS_LEVEL5", null, null);

		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}


}
