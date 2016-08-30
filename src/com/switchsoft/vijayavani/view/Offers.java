package com.switchsoft.vijayavani.view;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.kobjects.base64.Base64;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.switchsoft.vijayavani.R;
import com.switchsoft.vijayavani.adapter.Offers_Adapter;
import com.switchsoft.vijayavani.sql.SQLHelper;

public class Offers extends Fragment implements OnClickListener{
	
	public static Offers offers_fragment = null;

	private ListView mOffer_Listview;
	private ProgressBar mOffer_Progressbar;
	private TextView mNo_Offer_Textview;
	//Details view
	private ScrollView mOffer_Detail_Scrollview;
	private TextView mOffer_Name_Textview, mOffer_Valid_from_Textview, mOffer_Valid_to_Textview, mOffer_Description_Textview;
	private ImageView mOffer_Image_Imageview, mFull_Offerimage_imageview;

	HashMap<String, Object> hm;
	ArrayList<HashMap<String, Object>> offers;

	SQLHelper sqlHelper;
	
	int Offer_id = 0;
	String offer_name = "";
	String offer_description = "";
	String offer_validfrom = "";
	String offer_validto = "";
	Bitmap offer_image;
	
	public static boolean inDetails_display = false;
	public static boolean infull_screen = false;

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

		View view = inflater.inflate(R.layout.offers, container, false);

		mOffer_Progressbar = (ProgressBar)view.findViewById(R.id.offer_progressbar);
		mOffer_Listview = (ListView)view.findViewById(R.id.offer_listview);
		mNo_Offer_Textview = (TextView)view.findViewById(R.id.no_offer_textview);
		//Detail view
		mOffer_Detail_Scrollview = (ScrollView)view.findViewById(R.id.offer_detail_scrollview);
		mOffer_Name_Textview = (TextView)view.findViewById(R.id.offer_name_textview);
		mOffer_Valid_from_Textview = (TextView)view.findViewById(R.id.offer_valid_from_textview);
		mOffer_Valid_to_Textview = (TextView)view.findViewById(R.id.offer_valid_to_textview);
		mOffer_Description_Textview = (TextView)view.findViewById(R.id.offer_description_textview);
		mOffer_Image_Imageview = (ImageView)view.findViewById(R.id.offer_image_imageview);
		mFull_Offerimage_imageview = (ImageView)view.findViewById(R.id.full_offerimage_imageview);

		return view;
	}

	// after onCreateview this method will call..so put all listeners in this class
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		offers_fragment = this;

		sqlHelper = new SQLHelper(getActivity());
		
		mOffer_Image_Imageview.setOnClickListener(this);

		OffersDataAsynchronousTask testCursor = new OffersDataAsynchronousTask();
		testCursor.execute();
		
		mOffer_Listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				inDetails_display = true;
				
				mOffer_Listview.setVisibility(View.GONE);
				mOffer_Detail_Scrollview.setVisibility(View.VISIBLE);
				Offer_id = (Integer) offers.get(arg2).get("Offer_id");
				
				OffersDetailsAsynchronousTask detailsCursor = new OffersDetailsAsynchronousTask();
				detailsCursor.execute();	
				
			}
		});

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
		if(sqlHelper != null){
			sqlHelper.close();
		}
	}

	//Your created method
	public void onBackPressed()
	{
		//Handle any cleanup you don't always want done in the normal lifecycle
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.offer_image_imageview:
			infull_screen = true;
			mOffer_Detail_Scrollview.setVisibility(View.GONE);
			mFull_Offerimage_imageview.setVisibility(View.VISIBLE);
			mFull_Offerimage_imageview.setImageBitmap(offer_image);
			break;
		}
	}

	/**Using Asyn_task for displaying the data  */
	private class OffersDataAsynchronousTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>{

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(Void... arg0) {
			offers = new ArrayList<HashMap<String, Object>>();

			getOffersData();		

			return offers;
		}

		@Override
		protected void onPreExecute() {
			// display progress here
		}


		//Can use UI thread here
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> results) {

			Offers_Adapter events_Adapter = new Offers_Adapter(getActivity(), results);
			mOffer_Listview.setAdapter(events_Adapter);

			mOffer_Progressbar.setVisibility(View.GONE);

			if(results.size() == 0){
				mNo_Offer_Textview.setVisibility(View.VISIBLE);
			}
		}
	}
	
	/**Using Asyn_task for displaying the data  */
	private class OffersDetailsAsynchronousTask extends AsyncTask<Void, Void, ArrayList<HashMap<String, Object>>>{

		@Override
		protected ArrayList<HashMap<String, Object>> doInBackground(Void... arg0) {
			ArrayList<HashMap<String, Object>> offers = new ArrayList<HashMap<String, Object>>();

			getOfferDetailData();
			return offers;
		}

		@Override
		protected void onPreExecute() {
			// display progress here
			mOffer_Progressbar.setVisibility(View.VISIBLE);
		}


		//Can use UI thread here
		@Override
		protected void onPostExecute(ArrayList<HashMap<String, Object>> results) {

			mOffer_Name_Textview.setText(": "+offer_name);
			mOffer_Valid_from_Textview.setText(": "+offer_validfrom);
			mOffer_Valid_to_Textview.setText(": "+offer_validto);
			mOffer_Description_Textview.setText(": "+offer_description);
			mOffer_Image_Imageview.setImageBitmap(offer_image);					

			mOffer_Progressbar.setVisibility(View.GONE);

		}
	}

	private void getOffersData(){

		offers = new ArrayList<HashMap<String, Object>>();

		Cursor cursor = getEvents("OFFERS");

		//while loop for fetching all the data
		while (cursor.moveToNext()) {

			hm = new HashMap<String, Object>();	

			hm.put("Offer_id",cursor.getInt(cursor.getColumnIndex("Offer_id")));
			hm.put("Description",cursor.getString(cursor.getColumnIndex("Offer_Description")));
			hm.put("Heading",cursor.getString(cursor.getColumnIndex("Offer_Heading")));
			hm.put("Status",cursor.getString(cursor.getColumnIndex("Offer_status")));
			hm.put("Time",cursor.getString(cursor.getColumnIndex("Offer_Time")));
			hm.put("Offer_Valid_from",cursor.getString(cursor.getColumnIndex("Offer_Valid_from")));
			hm.put("Offer_Valid_to",cursor.getString(cursor.getColumnIndex("Offer_Valid_to")));

			String photo = cursor.getString(cursor.getColumnIndex("Offer_Image"));
			//Converting it into byte[] format
			byte[] imageAsBytes = Base64.decode(photo);		
			ByteArrayInputStream imageStream = new ByteArrayInputStream(imageAsBytes);
			//Setting options for the image quality
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inPurgeable = true;
			bmpFactoryOptions.inTempStorage = new byte[16*1024];
			bmpFactoryOptions.inSampleSize = 2;
			//Decoding the image to be a bitmap
			Bitmap converted_image = BitmapFactory.decodeStream(imageStream, null, bmpFactoryOptions);

			hm.put("Converted_Image", converted_image);

			offers.add(hm);

			imageAsBytes = null;
			imageStream = null;
			converted_image = null;

		}
		cursor.close();
	}
	
	private void getOfferDetailData(){

		try {			

			String 	qurey = "select * from "+"OFFERS"+" where "+
					"OFFERS"+"."+"Offer_id"+" = "+Offer_id;

			Cursor cursor = getRawEvents(qurey);

			cursor.moveToFirst();

			offer_description = cursor.getString(cursor.getColumnIndex("Offer_Description"));
			offer_name = cursor.getString(cursor.getColumnIndex("Offer_Heading"));
			offer_validfrom = cursor.getString(cursor.getColumnIndex("Offer_Valid_from"));
			offer_validto = cursor.getString(cursor.getColumnIndex("Offer_Valid_to"));

			String photo = cursor.getString(cursor.getColumnIndex("Offer_Image"));
			//Converting it into byte[] format
			byte[] imageAsBytes = Base64.decode(photo);		
			ByteArrayInputStream imageStream = new ByteArrayInputStream(imageAsBytes);
			//Setting options for the image quality
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inPurgeable = true;
			bmpFactoryOptions.inTempStorage = new byte[16*1024];
			bmpFactoryOptions.inSampleSize = 1;
			//Decoding the image to be a bitmap
			offer_image = BitmapFactory.decodeStream(imageStream, null, bmpFactoryOptions);

			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**This method will get the details based on table
	 * With the sequence as the criteria 
	 * Returns the data in ascending order of sequence*/
	private Cursor getEvents(String table) {
		SQLiteDatabase db = (sqlHelper).getReadableDatabase();
		Cursor cursor = db.query(table, null, null, null, null, null, null);

		return cursor;
	}
	
	/**This method will get the details from database based on Query */
	private Cursor getRawEvents(String sql) {
		SQLiteDatabase db = (sqlHelper).getReadableDatabase();
		Cursor cursor = db.rawQuery(sql, null);

		return cursor;
	}
	
	public void closeDetails(){
		inDetails_display = false;
		mOffer_Detail_Scrollview.setVisibility(View.GONE);
		mOffer_Listview.setVisibility(View.VISIBLE);		
	}
	
	public void closeFullscreen(){
		infull_screen = false;
		mFull_Offerimage_imageview.setVisibility(View.GONE);
		mOffer_Detail_Scrollview.setVisibility(View.VISIBLE);		
	}

}
