package com.switchsoft.vijayavani.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class Constants {

	public static String Cache_Filename = "Vijayavani";
	
	public static String catalodid = "7";
	//Amazon link
	public static String BASE_URL  = "http://thisisswitch.com:8084/icatservice/service.svc";
	/**
	 * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
	 */
	public static final String SERVER_URL = "http://thisisswitch.com:8084/GCMAPP/index.aspx";

	/**
	 * Google API project id registered to use GCM.
	 */
	public static final String SENDER_ID = "679856486158";

	public static final int CONNECT_TIMEOUT = 30000;
	public static final int SOCKET_TIMEOUT = 20000;

	//Amazon web server
	public static String server_url 		= BASE_URL+"/getAllData/"+catalodid+"/android";
	
//	http://thisisswitch.com:8084/icatservice/service.svc/GetStoreLocator/7
	//http://thisisswitch.com:8084/icatservice/service.svc/GetSubCategory/7

	public static String background_url 	= BASE_URL+"/GetBackgrounds/"+catalodid+"/android";
	public static String main_category_url = BASE_URL+"/GetMainCategory/"+catalodid;
	public static String sub_category__url = BASE_URL+"/GetSubCategory/"+catalodid;
	public static String item_url 			= BASE_URL+"/GetItemCategory/"+catalodid;
	public static String itemlevel4_url 	= BASE_URL+"/GetItemCategorylevel4/"+catalodid;
	public static String itemlevel5_url    = BASE_URL+"/GetItemCategorylevel5/"+catalodid;


	public static String offers_url 		= BASE_URL+"/GetOffer/"+catalodid+"/android";
	public static String storelocator_url 	= BASE_URL+"/GetStoreLocator/"+catalodid;

	public static String timestamp_url     = BASE_URL+"/GetIcatelogUpdate/"+catalodid;

	public static String loyalty_details = BASE_URL+"/GetLoyalityPointDetails/"+catalodid;

	public static String feedback_url      = BASE_URL+"?NewFeedback?Cust_Name={Cust_Name}&Cust_Mobile={Cust_Mobile}&Cust_Email={Cust_Email}&Comment={Comment}&Services={Services}&Ambience={Ambience}&StylistAndBeautyTherapist={StylistAndBeautyTherapist}}&User_id={User_id}";

	public static String getLoyaltyTypeDetails = BASE_URL+"/GetLoyalityPointDetails/"+catalodid;

	public static String login_url     = BASE_URL+"/LoyalityLoginCheck?Customer_Email=";

	public String sign_up = "/CustomerReg?Customer_Name={Customer_Name}&Customer_Email={Customer_Email}&Customer_Phone={Customer_Phone}&User_Id={User_Id}&Customer_Pwd={Customer_Pwd}&Customer_Address={Customer_Address}&Customer_Area={Customer_Area}&Customer_City={Customer_City}&Customer_State={Customer_State}";

	public String login_check = "/LoyalityLoginCheck?Customer_Email={Customer_Email}&Customer_Pwd={Customer_Pwd}&User_Id={User_Id}";

	public String loyalty_check = "/InsertCustomerPoints?Customer_Id={Customer_Id}&User_Id={User_Id}&Store_Id={Store_Id}&Loyality_Id={Loyality_Id}&Date={Date}&Time={Time}";

	public static String get_points = "/GetmyPoints?Customer_Id=";

	public String mian_category = "Main";


	//Time stamp related names
	public static String timestamp_Offers = "Offers";
	public static String timestamp_Events = "Events";
	public static String timestamp_MainCategory = "MainCategory";
	public static String timestamp_SubCategory = "SubCategory";
	public static String timestamp_Items = "ItemCategory";
	public static String timestamp_storelocator = "StoreLocater";
	
	/**Parsing data */
	public String parseData(String url){

		String line = null;
		try{
			URL schemaURL = new URL(url);
			URLConnection urlConn = schemaURL.openConnection();
			urlConn.setReadTimeout(CONNECT_TIMEOUT);
			urlConn.setConnectTimeout(SOCKET_TIMEOUT);
			BufferedReader in = new BufferedReader(new InputStreamReader( urlConn.getInputStream()));
			if((line = in.readLine()) != null) {
			}
		}
		catch (Exception e) {
			Log.d("COnstatnt","Exception::"+e);
		}
		return line;
	}
	
	public static String GetServerUrl(String regId,String deviceType,String userId){


		return "http://thisisswitch.com:8084/Insert_NEWICatelogdb_push_DeviceId?Device_Id="+regId+"&Device_Type="+deviceType+"&User_Id="+userId;
	}
}
