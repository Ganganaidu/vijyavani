package com.switchsoft.vijayavani.model;

public class StoreLocatorModel {
	
	public String email_id = "";
	public String latitude = "";
	public String longitude = "";
	public String location = "";
	public String phone_no = "";
	public String street_address = "";
	public int store_id = 0;
	public int userid = 0;
	public String store_name = "";
	
	
	public StoreLocatorModel()
	{
	}
	
	public StoreLocatorModel(String email, String latitude, String logitude, String location,
			String phone_no, String street_address, int store_id, int userid, String storename){
		
		this.email_id = email;
		this.latitude = latitude;
		this.longitude = logitude;
		this.location = location;
		this.phone_no = phone_no;
		this.street_address = street_address;
		this.store_id = store_id;
		this.userid = userid;
		this.store_name = storename;
		
	}
	
	
}
