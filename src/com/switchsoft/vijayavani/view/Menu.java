package com.switchsoft.vijayavani.view;

import java.util.ArrayList;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.switchsoft.vijayavani.R;
import com.switchsoft.vijayavani.sql.SQLHelper;
import com.switchsoft.vijayavani.util.Animations;
import com.switchsoft.vijayavani.util.Constants;
import com.switchsoft.vijayavani.viewpager.ItemModelPagerAdapter_3;
import com.switchsoft.vijayavani.viewpager.ItemModelPagerAdapter_4;
import com.switchsoft.vijayavani.viewpager.ItemModelPagerAdapter_5;
import com.switchsoft.vijayavani.viewpager.SubCategoryPagerAdapter;

public class Menu extends Fragment implements OnClickListener{

	public static Menu menu = null;
	Animations animation_util;

	HashMap<String, Object> hm;
	private ArrayList<HashMap<String, Object>> main_category;
	public static ArrayList<HashMap<String, Object>> subcategory;
	public static ArrayList<HashMap<String, Object>> menu_items;
	public static ArrayList<HashMap<String, Object>> menu_items_level4;
	public static ArrayList<HashMap<String, Object>> menu_items_level5;

	SQLHelper sqlhelper;
	Constants constant;

	private int main_cat_id = 0;
	private int menu_sub_cat_id = 0;
	private int item_id = 0;
	private int item_id_4 = 0;

	public static boolean display_maincategory = false;
	public static boolean display_subcategory = false;
	public static boolean display_items = false;
	public static boolean display_itemslevel4 = false;
	public static boolean display_itemslevel5 = false;

	public static boolean segment1clicked = false;
	public static boolean segment2clicked = false;
	public static boolean segment3clicked = false;

	//Declaring the view
	TextView mGoahead_Textview;
	//segment on clicked button
	Button mSegmentmover_Button1, mSegmentmover_Button2, mSegmentmover_Button3;
	//segment text
	TextView mSegment_Textview1, mSegment_Textview2, mSegment_Textview3;
	TextView mName_Textview;
	//segment click button
	Button mSegment_Button1, mSegment_Button2, mSegment_Button3;
	//
	ViewPager mSubcategory_Viewpager, mItemmodel3_Viewpager, 
	mItemmodel4_Viewpager, mItemmodel5_Viewpager;

	SubCategoryPagerAdapter subcategoryAdapter;
	ItemModelPagerAdapter_3 itemmodel3Adapter;
	ItemModelPagerAdapter_4 itemmodel4Adapter;
	ItemModelPagerAdapter_5 itemmodel5Adapter;

	//Translate Animation
	int []size;
	final int animation_duration = 500;


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

		View view = inflater.inflate(R.layout.menu, container, false);

		mGoahead_Textview = (TextView)view.findViewById(R.id.goahead_textview);
		//clicked buttons
		mSegmentmover_Button1 = (Button)view.findViewById(R.id.segmentmover_button1);
		mSegmentmover_Button2 = (Button)view.findViewById(R.id.segmentmover_button2);
		mSegmentmover_Button3 = (Button)view.findViewById(R.id.segmentmover_button3);
		//Segment text view
		mSegment_Textview1 = (TextView)view.findViewById(R.id.segment_textview1);
		mSegment_Textview2 = (TextView)view.findViewById(R.id.segment_textview2);
		mSegment_Textview3 = (TextView)view.findViewById(R.id.segment_textview3);
		mName_Textview     = (TextView)view.findViewById(R.id.name_textview);
		//segment clicked handler
		mSegment_Button1 = (Button)view.findViewById(R.id.segment_button1);
		mSegment_Button2 = (Button)view.findViewById(R.id.segment_button2);
		mSegment_Button3 = (Button)view.findViewById(R.id.segment_button3);
		//normal views
		mSubcategory_Viewpager = (ViewPager)view.findViewById(R.id.subcategory_viewpager);
		mItemmodel3_Viewpager = (ViewPager)view.findViewById(R.id.itemmodel3_viewpager);
		mItemmodel4_Viewpager = (ViewPager)view.findViewById(R.id.itemmodel4_viewpager);
		mItemmodel5_Viewpager = (ViewPager)view.findViewById(R.id.itemmodel5_viewpager);

		mSubcategory_Viewpager.setVisibility(View.GONE);

		sqlhelper = new SQLHelper(getActivity());
		constant = new Constants();	

		//closing all the views depending on the data the views will be made visible
		//mContext_Relativelayout.setVisibility(View.GONE);

		return view;
	}

	// after onCreateview this method will call..so put all listeners in this class
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		menu = this;

		display_maincategory = true;

		animation_util = new Animations();

		mSegment_Button1.setOnClickListener(this);
		mSegment_Button2.setOnClickListener(this);
		mSegment_Button3.setOnClickListener(this);

		MenuDataAsynchronousTask testCursor = new MenuDataAsynchronousTask();
		testCursor.execute();	

		mSubcategory_Viewpager.setPageMargin(-110);
		mItemmodel3_Viewpager.setPageMargin(-110);
		mItemmodel4_Viewpager.setPageMargin(-110);
		mItemmodel5_Viewpager.setPageMargin(-110);

		mSubcategory_Viewpager.setHorizontalFadingEdgeEnabled(true);
		mItemmodel3_Viewpager.setHorizontalFadingEdgeEnabled(true);
		mItemmodel4_Viewpager.setHorizontalFadingEdgeEnabled(true);
		mItemmodel5_Viewpager.setHorizontalFadingEdgeEnabled(true);

		mSubcategory_Viewpager.setFadingEdgeLength(30);
		mItemmodel3_Viewpager.setFadingEdgeLength(30);
		mItemmodel4_Viewpager.setFadingEdgeLength(30);
		mItemmodel5_Viewpager.setFadingEdgeLength(30);

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
		case R.id.segment_button1:
			if(segment1clicked == false){
				mGoahead_Textview.setVisibility(View.GONE);
				mName_Textview.setVisibility(View.GONE);
				mSegmentmover_Button1.setBackgroundResource(R.drawable.segmented_clicked);
				mSegmentmover_Button2.setBackgroundResource(0);
				mSegmentmover_Button3.setBackgroundResource(0);

				size = new int[2];
				mSegmentmover_Button1.getLocationInWindow(size);

				/*System.out.println("start pos"+size[0]);
				System.out.println("End pos"+size[1]);
				System.out.println("width"+mSegmentmover_Button1.getWidth());*/

				if(segment3clicked == true){
					TranslateAnimation trans = new TranslateAnimation((mSegmentmover_Button1.getWidth() * 2), size[0], 0, 0);
					trans.setDuration(animation_duration);
					mSegmentmover_Button1.setAnimation(trans);

				} else if(segment2clicked == true){
					TranslateAnimation trans = new TranslateAnimation(mSegmentmover_Button1.getWidth(), size[0], 0, 0);
					trans.setDuration(animation_duration);
					mSegmentmover_Button1.setAnimation(trans);

				} else {
					//Do Nothing
				}

				segment1clicked = true;
				segment2clicked = false;
				segment3clicked = false;				

				getSubcategoryData(0);
			} else {
				mGoahead_Textview.setVisibility(View.VISIBLE);
				mSegmentmover_Button1.setBackgroundResource(0);
				mSegmentmover_Button2.setBackgroundResource(0);
				mSegmentmover_Button3.setBackgroundResource(0);
				segment1clicked = false;

				mSubcategory_Viewpager.setVisibility(View.GONE);
				mItemmodel3_Viewpager.setVisibility(View.GONE);
				mItemmodel4_Viewpager.setVisibility(View.GONE);
				mItemmodel5_Viewpager.setVisibility(View.GONE);

			}
			break;

		case R.id.segment_button2:
			if(segment2clicked == false){
				mGoahead_Textview.setVisibility(View.GONE);
				mName_Textview.setVisibility(View.GONE);

				mSegmentmover_Button1.setBackgroundResource(0);
				mSegmentmover_Button2.setBackgroundResource(R.drawable.segmented_clicked);
				mSegmentmover_Button3.setBackgroundResource(0);

				size = new int[2];
				mSegmentmover_Button2.getLocationInWindow(size);

				//Animation
				if(segment3clicked == true){
					TranslateAnimation trans = new TranslateAnimation(size[0], 0, 0, 0);
					trans.setDuration(animation_duration);
					trans.setFillAfter(true);
					mSegmentmover_Button2.setAnimation(trans);

				} else if(segment1clicked == true){
					TranslateAnimation trans = new TranslateAnimation(-size[0], 0, 0, 0);
					trans.setDuration(animation_duration);
					mSegmentmover_Button2.setAnimation(trans);

				} else {
					//Do Nothing
				}

				segment1clicked = false;
				segment2clicked = true;
				segment3clicked = false;

				getSubcategoryData(1);
			} else {
				mGoahead_Textview.setVisibility(View.VISIBLE);
				mSegmentmover_Button1.setBackgroundResource(0);
				mSegmentmover_Button2.setBackgroundResource(0);
				mSegmentmover_Button3.setBackgroundResource(0);
				segment2clicked = false;

				mSubcategory_Viewpager.setVisibility(View.GONE);
				mItemmodel3_Viewpager.setVisibility(View.GONE);
				mItemmodel4_Viewpager.setVisibility(View.GONE);
				mItemmodel5_Viewpager.setVisibility(View.GONE);

			}
			break;

		case R.id.segment_button3:
			if(segment3clicked == false){
				mGoahead_Textview.setVisibility(View.GONE);
				mName_Textview.setVisibility(View.GONE);
				
				mSegmentmover_Button1.setBackgroundResource(0);
				mSegmentmover_Button2.setBackgroundResource(0);
				mSegmentmover_Button3.setBackgroundResource(R.drawable.segmented_clicked);

				size = new int[2];
				mSegmentmover_Button3.getLocationInWindow(size);

				if(segment1clicked == true){
					TranslateAnimation trans = new TranslateAnimation(-size[0], 0, 0, 0);
					trans.setDuration(animation_duration);
					mSegmentmover_Button3.setAnimation(trans);

				} else if(segment2clicked == true){
					TranslateAnimation trans = new TranslateAnimation(-(size[0] / 2), 0, 0, 0);
					trans.setDuration(animation_duration);
					mSegmentmover_Button3.setAnimation(trans);

				} else {
					//Do Nothing
				}

				segment1clicked = false;
				segment2clicked = false;
				segment3clicked = true;

				getSubcategoryData(2);
			} else {
				mGoahead_Textview.setVisibility(View.VISIBLE);
				mSegmentmover_Button1.setBackgroundResource(0);
				mSegmentmover_Button2.setBackgroundResource(0);
				mSegmentmover_Button3.setBackgroundResource(0);
				segment3clicked = false;

				mSubcategory_Viewpager.setVisibility(View.GONE);
				mItemmodel3_Viewpager.setVisibility(View.GONE);
				mItemmodel4_Viewpager.setVisibility(View.GONE);
				mItemmodel5_Viewpager.setVisibility(View.GONE);

			}
			break;
		}
	}

	public void getSubcategoryData(int category){
		try {
			hideAdapters();

			String price = (String) main_category.get(category).get("Main_price");
			if(price.equals("")){			
				main_cat_id = (Integer) main_category.get(category).get("Main_cat_id");

				display_maincategory = false;
				//changing the view to sub category
				display_subcategory = true;

				MenuDataAsynchronousTask testCursor = new MenuDataAsynchronousTask();
				testCursor.execute();	
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	public void getItemsData3(int position){
		try {
			hideAdapters();
			menu_sub_cat_id = (Integer)subcategory.get(position).get("Sub_Cat_Id");

			display_subcategory = false;
			//changing the view to item level 1
			display_items = true;

			MenuDataAsynchronousTask testCursor = new MenuDataAsynchronousTask();
			testCursor.execute();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

	public void getItemsData4(int position){
		try {
			hideAdapters();
			item_id = (Integer)menu_items.get(position).get("item_id");

			display_items = false;
			//changing the view to item level 1
			display_itemslevel4 = true;

			MenuDataAsynchronousTask testCursor = new MenuDataAsynchronousTask();
			testCursor.execute();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

	public void getItemsData5(int position){
		try {
			hideAdapters();
			menu_sub_cat_id = (Integer)menu_items_level4.get(position).get("subitem_id4");

			display_itemslevel4 = false;
			//changing the view to item level 1
			display_itemslevel5 = true;

			MenuDataAsynchronousTask testCursor = new MenuDataAsynchronousTask();
			testCursor.execute();	
		} catch (Exception e) {
			// TODO: handle exception
		}
		

	}

	private void hideAdapters(){
		mSubcategory_Viewpager.setVisibility(View.GONE);
		mItemmodel3_Viewpager.setVisibility(View.GONE);
		mItemmodel4_Viewpager.setVisibility(View.GONE);
		mItemmodel5_Viewpager.setVisibility(View.GONE);
	}

	public void showViewPagerSubCategory(){
		mSubcategory_Viewpager.setVisibility(View.VISIBLE);
		mItemmodel3_Viewpager.setVisibility(View.GONE);
		mItemmodel4_Viewpager.setVisibility(View.GONE);
		mItemmodel5_Viewpager.setVisibility(View.GONE);

		display_subcategory = true;
		display_items = false;
	}

	public void showViewPager3(){
		mSubcategory_Viewpager.setVisibility(View.GONE);
		mItemmodel3_Viewpager.setVisibility(View.VISIBLE);
		mItemmodel4_Viewpager.setVisibility(View.GONE);
		mItemmodel5_Viewpager.setVisibility(View.GONE);

		display_items = true;
		display_itemslevel4 = false;
	}


	public void showViewPager4(){
		mSubcategory_Viewpager.setVisibility(View.GONE);
		mItemmodel3_Viewpager.setVisibility(View.GONE);
		mItemmodel4_Viewpager.setVisibility(View.VISIBLE);
		mItemmodel5_Viewpager.setVisibility(View.GONE);

		display_itemslevel4 = true;
		display_itemslevel5 = false;
	}

	/**Using Asyn_task for displaying the data  */
	private class MenuDataAsynchronousTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>{

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(Void... arg0) {

			if(display_maincategory == true){
				main_category = new ArrayList<HashMap<String, Object>>();

				getMainCategoryData();

			} else if(display_subcategory == true){
				subcategory  = new ArrayList<HashMap<String, Object>>();

				getsubcategoryData(main_cat_id);

			} else if(display_items == true){
				menu_items = new ArrayList<HashMap<String, Object>>();

				getItemsData(menu_sub_cat_id);

			} else if(display_itemslevel4 == true){
				menu_items_level4 = new ArrayList<HashMap<String, Object>>();

				getItemsData_level4(item_id);
			} else if(display_itemslevel5 == true){
				menu_items_level5 = new ArrayList<HashMap<String, Object>>();

				getItemsData_level5(item_id_4);
			}

			return main_category;
		}

		@Override
		protected void onPreExecute() {
			// display progress here
		}

		//Can use UI thread here
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> results) {
			if(display_maincategory == true){
				if(main_category.size() != 0){
					mSegment_Textview1.setText((String) main_category.get(0).get("Main_cat_name"));
					mSegment_Textview2.setText((String) main_category.get(1).get("Main_cat_name"));
					mSegment_Textview3.setText((String) main_category.get(2).get("Main_cat_name"));

				} 

			} else if(display_subcategory == true){
				if(subcategory.size() != 0){					
					FragmentManager fm = getFragmentManager();

					//** Instantiating FragmentPagerAdapter *//*
					subcategoryAdapter = new SubCategoryPagerAdapter(fm);

					//** Setting the pagerAdapter to the pager object *//*
					mSubcategory_Viewpager.setAdapter(subcategoryAdapter);
					//					subcategoryAdapter.notifyDataSetChanged();
					mSubcategory_Viewpager.setVisibility(View.VISIBLE);
					mSubcategory_Viewpager.startAnimation(animation_util.InFromRight());
				} 

			} else if(display_items == true){

				if(menu_items.size() != 0){
					FragmentManager fm = getFragmentManager();
					//** Instantiating FragmentPagerAdapter *//*
					itemmodel3Adapter = new ItemModelPagerAdapter_3(fm);

					//** Setting the pagerAdapter to the pager object *//*
					mItemmodel3_Viewpager.setAdapter(itemmodel3Adapter);
					mItemmodel3_Viewpager.setVisibility(View.VISIBLE);	
					mItemmodel3_Viewpager.startAnimation(animation_util.InFromRight());
				} 

			} else if(display_itemslevel4 == true){
				if(menu_items_level4.size() != 0){
					FragmentManager fm = getFragmentManager();
					//** Instantiating FragmentPagerAdapter *//*
					itemmodel4Adapter = new ItemModelPagerAdapter_4(fm);

					//** Setting the pagerAdapter to the pager object *//*
					mItemmodel4_Viewpager.setAdapter(itemmodel4Adapter);
					mItemmodel4_Viewpager.setVisibility(View.VISIBLE);	
					mItemmodel4_Viewpager.startAnimation(animation_util.InFromRight());
				} 
			} else if(display_itemslevel5 == true){
				if(menu_items_level5.size() != 0){
					FragmentManager fm = getFragmentManager();
					//** Instantiating FragmentPagerAdapter *//*
					itemmodel5Adapter = new ItemModelPagerAdapter_5(fm);

					//** Setting the pagerAdapter to the pager object *//*
					mItemmodel5_Viewpager.setAdapter(itemmodel5Adapter);
					mItemmodel5_Viewpager.setVisibility(View.VISIBLE);	
					mItemmodel5_Viewpager.startAnimation(animation_util.InFromRight());
				} 
			}
		}
	}

	private void getMainCategoryData(){

		Cursor cursor = getEvents("MAINCATEGORY");
		if(cursor != null){
			//while loop for fetching all the data
			while (cursor.moveToNext()) {

				HashMap<String, Object> hm = new HashMap<String, Object>();	
				hm.put("Main_cat_name",cursor.getString(cursor.getColumnIndex("Main_Cat_Name")));	
				hm.put("Main_isactive",cursor.getInt(cursor.getColumnIndex("Main_CatIsActive")));
				hm.put("Main_cat_id",cursor.getInt(cursor.getColumnIndex("Main_Cat_Id")));
				hm.put("Main_price",cursor.getString(cursor.getColumnIndex("Main_Price")));
				hm.put("Main_Description",cursor.getString(cursor.getColumnIndex("Main_CatDescription")));

				String category_name = cursor.getString(cursor.getColumnIndex("Category_Name"));
				hm.put("Category_name",category_name);

				if(category_name.equals("Menu")){
					main_category.add(hm);
				}
				category_name = "";
			}

		}

	}

	private void getsubcategoryData(int main_cat_id){

		//Query represents the position from where the data has to be fetched
		String 	qurey = "select * from "+"SUBCATEGORY"+" where "+
				"SUBCATEGORY"+"."+"MainCat_Id"+" = "+main_cat_id;

		Cursor cursor = getRawEvents(qurey);

		try {
			if(cursor != null){
				//				cursor.moveToFirst();
				while (cursor.moveToNext()) {

					hm = new HashMap<String, Object>();	

					hm.put("Sub_Cat_IsActive",cursor.getInt(cursor.getColumnIndex("Sub_Cat_IsActive")));
					hm.put("MainCat_Id",cursor.getInt(cursor.getColumnIndex("MainCat_Id")));
					hm.put("Sub_Cat_Id",cursor.getInt(cursor.getColumnIndex("Sub_Cat_Id")));
					hm.put("Sub_Cat_Name",cursor.getString(cursor.getColumnIndex("Sub_Cat_Name")));
					hm.put("Sub_cat_Price",cursor.getString(cursor.getColumnIndex("Sub_Cat_Price")));
					hm.put("Sub_Description",cursor.getString(cursor.getColumnIndex("Sub_Cat_Description")));

					String category_name = cursor.getString(cursor.getColumnIndex("Category_Name"));
					hm.put("Category_name",category_name);

					if(category_name.equals("Menu")){
						subcategory.add(hm);
					}

					category_name = "";

				}
			}

		}catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void getItemsData(int menu_sub_cat_id){

		String 	qurey = "select * from "+"ITEMS"+" where "+
				"ITEMS"+"."+"Item_Sub_Cat_Id"+" = \"" +  menu_sub_cat_id + "\"";



		Cursor cursor = getRawEvents(qurey);

		try {
			//while loop for fetching all the data
			while (cursor.moveToNext()) {

				hm = new HashMap<String, Object>();	

				hm.put("item_category",cursor.getString(cursor.getColumnIndex("Item_Category")));			
				hm.put("item_active",cursor.getInt(cursor.getColumnIndex("Item_IsActive")));
				hm.put("item_id",cursor.getInt(cursor.getColumnIndex("Item_Id")));
				hm.put("item_name",cursor.getString(cursor.getColumnIndex("Item_Name")));
				hm.put("Item_Price",cursor.getString(cursor.getColumnIndex("Item_Price")));
				hm.put("Item_Description",cursor.getString(cursor.getColumnIndex("Item_Description")));
				hm.put("Item_Image_Link",cursor.getString(cursor.getColumnIndex("Item_Image_Link")));

				menu_items.add(hm);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getItemsData_level4(int item_id){

		String 	qurey = "select * from "+"ITEMS_LEVEL4"+" where "+
				"ITEMS_LEVEL4"+"."+"Item_Id"+" = \"" +  item_id + "\"";

		Log.d("TAG", "qurey: "+qurey);

		Cursor cursor = getRawEvents(qurey);

		try {
			//while loop for fetching all the data
			while (cursor.moveToNext()) {

				hm = new HashMap<String, Object>();	

				hm.put("item_category",cursor.getString(cursor.getColumnIndex("Item_Category")));			
				hm.put("item_active",cursor.getInt(cursor.getColumnIndex("Item_IsActive")));
				hm.put("subitem_id4",cursor.getInt(cursor.getColumnIndex("SubItem_Id4")));
				hm.put("item_name",cursor.getString(cursor.getColumnIndex("Item_Name")));
				hm.put("Item_Price",cursor.getString(cursor.getColumnIndex("Item_Price")));
				hm.put("Item_Description",cursor.getString(cursor.getColumnIndex("Item_Description")));

				menu_items_level4.add(hm);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getItemsData_level5(int level4_item_id){

		//Query represents the position from where the data has to be fetched
		String 	qurey = "select * from "+"ITEMS_LEVEL5"+" where "+
				"ITEMS_LEVEL5"+"."+"SubItem_Id4"+" = \""+level4_item_id+"\"";



		Cursor cursor = getRawEvents(qurey);

		try {
			//while loop for fetching all the data
			while (cursor.moveToNext()) {

				hm = new HashMap<String, Object>();	

				hm.put("item_category",cursor.getString(cursor.getColumnIndex("Item_Category")));			
				hm.put("item_active",cursor.getInt(cursor.getColumnIndex("Item_IsActive")));
				hm.put("SubItem_Id5",cursor.getInt(cursor.getColumnIndex("SubItem_Id5")));
				hm.put("item_name",cursor.getString(cursor.getColumnIndex("Item_Name")));
				hm.put("Item_Price",cursor.getString(cursor.getColumnIndex("Item_Price")));
				hm.put("Item_Description",cursor.getString(cursor.getColumnIndex("Item_Description")));

				menu_items_level5.add(hm);

			}

		} catch (Exception e) {
			e.printStackTrace();
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
}