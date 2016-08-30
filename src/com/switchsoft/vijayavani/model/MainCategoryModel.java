package com.switchsoft.vijayavani.model;

public class MainCategoryModel {
		
	public String main_cat_name = "";
	public String category_name = "";
	public String main_cat_description = "";
	public int main_cat_isactive = 0;
	public int main_cat_id = 0;
	public int user_id = 0;
	public String main_price = "";
	
	public MainCategoryModel()
	{
	}
	
	public MainCategoryModel(String name, String category_name, String description, int isactive, int id, int user_id, String price)
	{
		
		this.main_cat_name = name;
		this.category_name = category_name;
		this.main_cat_description = description;
		this.main_cat_isactive = isactive;
		this.main_cat_id = id;
		this.user_id = user_id;
		this.main_price = price;
	}

}
