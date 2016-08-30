package com.switchsoft.vijayavani.model;

public class SubCategoryModel {

	public String category_name = "";
	public int sub_cat_isactive = 0;
	public int main_cat_id = 0;
	public String sub_cat_description = "";
	public int sub_cat_id = 0;
	public String sub_cat_name = "";
	public int user_id = 0;
	public String sub_cat_price = "";
	
	public SubCategoryModel()
	{
	}
	
	public SubCategoryModel(String category_name, int isactive, int main_cat_id, String description, int sub_cat_id
			, String sub_cat_name, int user_id, String price)
	{
		
		this.category_name = category_name;
		this.sub_cat_isactive = isactive;
		this.main_cat_id = main_cat_id;
		this.sub_cat_description = description;
		this.sub_cat_id = sub_cat_id;
		this.sub_cat_name = sub_cat_name;
		this.user_id = user_id;
		this.sub_cat_price = price;
		
	}
	
	
}
