package com.switchsoft.vijayavani.model;

public class Loyalty_Types {
	
	public int loyalty_id = 0;
	public String loyalty_type = "";
	public int store_id = 0;
	public int user_id = 0;
	
	
	public Loyalty_Types()
	{
	}
	
	public Loyalty_Types(int loyalid, String loyaltype, int storeid, int userid){
		
		this.loyalty_id = loyalid;
		this.loyalty_type = loyaltype;
		this.store_id = storeid;
		this.user_id = userid;
		
	}
	
	
}
