package com.switchsoft.vijayavani;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.switchsoft.vijayavani.dialog.AlertDialogFragment;
import com.switchsoft.vijayavani.dialog.DialogFragment_SingleButton;
import com.switchsoft.vijayavani.util.Animations;
import com.switchsoft.vijayavani.view.LivePrices;
import com.switchsoft.vijayavani.view.Menu;
import com.switchsoft.vijayavani.view.Offers;

public class MainActivity extends FragmentActivity implements OnClickListener {


	//Declaring the views
	private TextView mHeading_Textview;
	private ImageView mMenu_Selected_Imageview, mOffers_Selected_Imageview,
	mFeedback_Selected_Imageview, mLocatestore_Selected_Imageview;
	private Button mMenu_Button, mOffers_Button, mFeedback_Button, mLocatestore_Button;
	private RelativeLayout mLoyalty_Relativelayout;
	private FrameLayout mMenu_Framlayout, mOffers_Framlayout, mLoyalty_Framlayout,
	mLivePrices_Framlayout, mLocatestore_Framlayout;

	Animations animations;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//removes the top status bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
		.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);

		//Initializing the views
		mHeading_Textview = (TextView)findViewById(R.id.heading_textview);
		//selected images
		mMenu_Selected_Imageview = (ImageView)findViewById(R.id.menu_selected_imageview);
		mOffers_Selected_Imageview = (ImageView)findViewById(R.id.offers_selected_imageview);
		mFeedback_Selected_Imageview = (ImageView)findViewById(R.id.feedback_selected_imageview);
		mLocatestore_Selected_Imageview = (ImageView)findViewById(R.id.locatestore_selected_imageview);
		//clicked button
		mMenu_Button = (Button)findViewById(R.id.menu_button);
		mOffers_Button = (Button)findViewById(R.id.offers_button);
		mFeedback_Button = (Button)findViewById(R.id.feedback_button);
		mLocatestore_Button = (Button)findViewById(R.id.locatestore_button);
		mLoyalty_Relativelayout = (RelativeLayout)findViewById(R.id.loyalty_relativelayout);
		//Framelayout
		mMenu_Framlayout = (FrameLayout)findViewById(R.id.menu_framlayout);
		mOffers_Framlayout = (FrameLayout)findViewById(R.id.offers_framlayout);
		mLoyalty_Framlayout = (FrameLayout)findViewById(R.id.loyalty_framlayout);
		mLivePrices_Framlayout = (FrameLayout)findViewById(R.id.liveprices_framlayout);
		mLocatestore_Framlayout = (FrameLayout)findViewById(R.id.map_framlayout);

		//Initializing the on click listeners for the views
		mMenu_Button.setOnClickListener(this);
		mOffers_Button.setOnClickListener(this);
		mFeedback_Button.setOnClickListener(this);
		mLocatestore_Button.setOnClickListener(this);
		mLoyalty_Relativelayout.setOnClickListener(this);

		animations = new Animations();

		mMenu_Selected_Imageview.setImageResource(R.drawable.botombar_selected);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.menu_button:
			mMenu_Selected_Imageview.setImageResource(R.drawable.botombar_selected);
			mOffers_Selected_Imageview.setImageResource(0);
			mFeedback_Selected_Imageview.setImageResource(0);
			mLocatestore_Selected_Imageview.setImageResource(0);

			mMenu_Framlayout.setVisibility(View.VISIBLE);
			mOffers_Framlayout.setVisibility(View.GONE);
			mLoyalty_Framlayout.setVisibility(View.GONE);
			mLivePrices_Framlayout.setVisibility(View.GONE);
			mLocatestore_Framlayout.setVisibility(View.GONE);

			mHeading_Textview.setText(R.string.menu);
			mHeading_Textview.startAnimation(animations.InFromTop());
			LivePrices.liveprices.stopConnection();
			break;

		case R.id.offers_button:
			mMenu_Selected_Imageview.setImageResource(0);
			mOffers_Selected_Imageview.setImageResource(R.drawable.botombar_selected);
			mFeedback_Selected_Imageview.setImageResource(0);
			mLocatestore_Selected_Imageview.setImageResource(0);

			mMenu_Framlayout.setVisibility(View.GONE);
			mOffers_Framlayout.setVisibility(View.VISIBLE);
			mLoyalty_Framlayout.setVisibility(View.GONE);
			mLivePrices_Framlayout.setVisibility(View.GONE);
			mLocatestore_Framlayout.setVisibility(View.GONE);

			mHeading_Textview.setText(R.string.offers);
			mHeading_Textview.startAnimation(animations.InFromTop());
			LivePrices.liveprices.stopConnection();
			break;

		case R.id.feedback_button:
			mMenu_Selected_Imageview.setImageResource(0);
			mOffers_Selected_Imageview.setImageResource(0);
			mFeedback_Selected_Imageview.setImageResource(R.drawable.botombar_selected);
			mLocatestore_Selected_Imageview.setImageResource(0);

			mMenu_Framlayout.setVisibility(View.GONE);
			mOffers_Framlayout.setVisibility(View.GONE);
			mLoyalty_Framlayout.setVisibility(View.GONE);
			mLivePrices_Framlayout.setVisibility(View.VISIBLE);
			mLocatestore_Framlayout.setVisibility(View.GONE);

			mHeading_Textview.setText(R.string.liveprices);
			mHeading_Textview.startAnimation(animations.InFromTop());

			LivePrices.liveprices.startConnection();
			break;

		case R.id.locatestore_button:
			mMenu_Selected_Imageview.setImageResource(0);
			mOffers_Selected_Imageview.setImageResource(0);
			mFeedback_Selected_Imageview.setImageResource(0);
			mLocatestore_Selected_Imageview.setImageResource(R.drawable.botombar_selected);

			mMenu_Framlayout.setVisibility(View.GONE);
			mOffers_Framlayout.setVisibility(View.GONE);
			mLoyalty_Framlayout.setVisibility(View.GONE);
			mLivePrices_Framlayout.setVisibility(View.GONE);
			mLocatestore_Framlayout.setVisibility(View.VISIBLE);

			mHeading_Textview.setText(R.string.locatestore);
			mHeading_Textview.startAnimation(animations.InFromTop());
			LivePrices.liveprices.stopConnection();
			break;

		case R.id.loyalty_relativelayout:
			mMenu_Selected_Imageview.setImageResource(0);
			mOffers_Selected_Imageview.setImageResource(0);
			mFeedback_Selected_Imageview.setImageResource(0);
			mLocatestore_Selected_Imageview.setImageResource(0);

			mMenu_Framlayout.setVisibility(View.GONE);
			mOffers_Framlayout.setVisibility(View.GONE);
			mLoyalty_Framlayout.setVisibility(View.VISIBLE);
			mLivePrices_Framlayout.setVisibility(View.GONE);
			mLocatestore_Framlayout.setVisibility(View.GONE);

			mHeading_Textview.setText(R.string.loyalty);
			mHeading_Textview.startAnimation(animations.InFromTop());
			LivePrices.liveprices.stopConnection();
			break;

		default:
			break;
		}

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

	/**When back key is pressed this method is called*/
	@Override
	public void onBackPressed() {	
		if(Offers.infull_screen == true){
			Offers.offers_fragment.closeFullscreen();
		} else if(Offers.inDetails_display == true){
			Offers.offers_fragment.closeDetails();
		} else if(Menu.display_items == true){
			Menu.menu.showViewPagerSubCategory();			
		} else if(Menu.display_itemslevel4 == true){
			Menu.menu.showViewPager3();
		} else if(Menu.display_itemslevel5 == true){
			Menu.menu.showViewPager4();
		} else {
			wantToExit();	
		}

	}

	private void wantToExit(){

		showDialog(getString(R.string.exit));

	}


	//** Displaying the alert dialog
	//*
	public void showDialog(String message) {
		AlertDialogFragment newFragment = AlertDialogFragment.newInstance(
				message);
		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	public void showDialog_singlebutton(String message) {
		DialogFragment_SingleButton newFragment = DialogFragment_SingleButton.newInstance(
				message);
		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	//Call back values for the dialog
	//positive
	public void doPositiveClick() {
		// Do stuff here.
		Log.i("FragmentAlertDialog", "Positive click!");
		finish();
	}

	//negative
	public void doNegativeClick() {
		// Do stuff here.
		Log.i("FragmentAlertDialog", "Negative click!");
	}

}
