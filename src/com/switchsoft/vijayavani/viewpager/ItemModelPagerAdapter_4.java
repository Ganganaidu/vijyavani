package com.switchsoft.vijayavani.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.switchsoft.vijayavani.R;
import com.switchsoft.vijayavani.view.Menu;

public class ItemModelPagerAdapter_4 extends FragmentPagerAdapter{

	static int PAGE_COUNT = 0;

	/** Constructor of the class */
	public ItemModelPagerAdapter_4(FragmentManager fm) {
		super(fm);
	}

	/** This method will be invoked when a page is requested to create */
	@Override
	public Fragment getItem(int arg0) {


		ItemModelFragment_4	myFragment = new ItemModelFragment_4();	
		Bundle data = new Bundle();
		data.putInt("current_page", arg0+1);
		myFragment.setArguments(data);
		return myFragment;	

	}

	/** Returns the number of pages */
	@Override
	public int getCount() {		
		PAGE_COUNT = Menu.menu_items_level4.size();
		return PAGE_COUNT;
	}
}

class ItemModelFragment_4 extends Fragment{

	int mCurrentPage;
	TextView menu_textview, price_textview;
	ImageView mMainbox_Imageview;
	RelativeLayout mPrice_Relativelayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/** Getting the arguments to the Bundle object */
		Bundle data = getArguments();

		/** Getting integer data of the key current_page from the bundle */
		mCurrentPage = data.getInt("current_page", 0);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		View v = inflater.inflate(R.layout.menu_viewpager, container,false);

		//Initializing the layout view
		menu_textview = (TextView)v.findViewById(R.id.menu_textview);
		mMainbox_Imageview = (ImageView)v.findViewById(R.id.mainbox_imageview);
		mPrice_Relativelayout = (RelativeLayout)v.findViewById(R.id.price_relativelayout);
		price_textview = (TextView)v.findViewById(R.id.price_textview);

		try {
			menu_textview.setText((String) Menu.menu_items_level4.get(mCurrentPage - 1).get("item_name"));

			String cost = (String) Menu.menu_items_level4.get(mCurrentPage - 1).get("Item_Price");
			String description = (String) Menu.menu_items_level4.get(mCurrentPage - 1).get("Item_Description");

			if(!cost.equals("")){
				mPrice_Relativelayout.setVisibility(View.VISIBLE);
				price_textview.setText(cost);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		mMainbox_Imageview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String cost = (String) Menu.menu_items_level4.get(mCurrentPage - 1).get("Item_Price");
				if(cost.equals("")){
					Menu.menu.getItemsData5(mCurrentPage - 1);
				}
			}
		});

		return v;

	}

}
