package com.switchsoft.vijayavani.model;

public class OffersModel {

	public String offer_description = ""; 
	public String offer_heading = "";
	public String offer_image = "";
	public String offer_image_link = "";
	public String offer_id = "";
	public String offer_status = "";
	public String offer_store_id = "";
	public String offer_time = "";
	public int offer_user_id = 0;
	public String offer_valid_from = "";
	public String offer_valid_to = "";

	public OffersModel()
	{
	}

	public OffersModel(String description, String heading, String image, String image_link, String offer_id,
			String status, String store_id, String time, int user_id, String valid_from, String valid_to)
	{

		this.offer_description = description;
		this.offer_heading = heading;
		this.offer_image = image;
		this.offer_image_link = image_link;
		this.offer_id = offer_id;
		this.offer_status = status;
		this.offer_store_id = store_id;
		this.offer_time = time;
		this.offer_user_id = user_id;
		this.offer_valid_from = valid_from;
		this.offer_valid_to = valid_to;

	}

}
