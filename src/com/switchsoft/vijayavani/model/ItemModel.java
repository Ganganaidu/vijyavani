package com.switchsoft.vijayavani.model;

public class ItemModel {
	
	public String item_category = ""; 
	public int item_is_active = 0;
	public String item_item_desc = "";
	public int item_item_id = 0;	
	public String item_item_name = "";
	public String item_item_price = "";
	public int item_main_cat_id = 0;
	public int item_sub_cat_id = 0;
	public int item_user_id = 0;
	public String item_image = "";
	public String item_image_link = "";
	public String item_level = "";
	
	public ItemModel()
	{
	}
	
	public ItemModel(String category, int isactive, String description, int id,
			String name, String price, int main_cat_id, int sub_cat_id, int user_id, String image, 
			String itemLevel,String image_link)
	{
		
		this.item_category = category;
		this.item_is_active = isactive;
		this.item_item_desc = description;
		this.item_item_id = id;
		this.item_item_name = name;
		this.item_item_price = price;
		this.item_main_cat_id = main_cat_id;
		this.item_sub_cat_id = sub_cat_id;
		this.item_user_id = user_id;
		this.item_image = image;
		this.item_image_link = image_link;
		this.item_level = itemLevel;
		
	}

}
