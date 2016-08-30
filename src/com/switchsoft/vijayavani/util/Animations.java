package com.switchsoft.vijayavani.util;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/** All the animations are tested keeping the image as the center of the view
 * Animation moves to the specified length irrespective of the size of the screen */

public class Animations {

	
	private int AnimationDuration = 800; //in milliseconds
	private int FadeAnimationDuration = 1000; //in milliseconds

	//view comes from top
	public Animation InFromTop(){

		Animation infromTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromTop.setDuration(AnimationDuration);
		infromTop.setFillAfter(true);

		return infromTop;		
	} 

	//view goes back to top
	public Animation OutToTop(){
		Animation outtoTop = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f);
		outtoTop.setDuration(AnimationDuration);
		outtoTop.setFillAfter(true);

		return outtoTop;
	}

	//view comes from bottom
	public Animation InFromBottom(){

		Animation infromBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromBottom.setDuration(AnimationDuration);
		infromBottom.setFillAfter(true);

		return infromBottom;		
	} 

	//view goes back to Bottom
	public Animation OutToBottom(){
		Animation outtoBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
		outtoBottom.setDuration(AnimationDuration);
		outtoBottom.setFillAfter(true);

		return outtoBottom;
	}

	//view comes from Left
	public Animation InFromLeft(){

		Animation infromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromLeft.setDuration(AnimationDuration);
		infromLeft.setFillAfter(true);

		return infromLeft;		
	} 

	//view goes back to left
	public Animation OutToLeft(){

		Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(AnimationDuration);
		outtoLeft.setFillAfter(true);

		return outtoLeft;		
	} 

	//view comes from Right
	public Animation InFromRight(){

		Animation infromRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromRight.setDuration(AnimationDuration);
		infromRight.setFillAfter(false);

		return infromRight;		
	} 

	//view goes back to left
	public Animation OutToRight(){

		Animation outtoRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(AnimationDuration);
		outtoRight.setFillAfter(true);

		return outtoRight;		
	} 

	//view comes from top left corner
	public Animation InFromTopLeft(){

		Animation infromTopLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromTopLeft.setDuration(AnimationDuration);
		infromTopLeft.setFillAfter(true);

		return infromTopLeft;		
	} 

	//view goes to top left corner
	public Animation OutToTopLeft(){

		Animation outtoTopLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f);
		outtoTopLeft.setDuration(AnimationDuration);
		outtoTopLeft.setFillAfter(true);

		return outtoTopLeft;		
	} 

	//view comes from top right corner
	public Animation InFromTopRight(){

		Animation infromBottomRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f);
		infromBottomRight.setDuration(AnimationDuration);
		infromBottomRight.setFillAfter(true);

		return infromBottomRight;		
	}

	//view goes to bottom left corner
	public Animation OutToTopRight(){

		Animation outtoBottomRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
		outtoBottomRight.setDuration(AnimationDuration);
		outtoBottomRight.setFillAfter(true);

		return outtoBottomRight;		
	} 

	//view comes from bottom right corner
	public Animation InFromBottomRight(){

		Animation infromBottomRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromBottomRight.setDuration(AnimationDuration);
		infromBottomRight.setFillAfter(true);

		return infromBottomRight;		
	}

	//view goes to bottom right corner
	public Animation OutToBottomRight(){

		Animation outtoBottomRight = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
		outtoBottomRight.setDuration(AnimationDuration);
		outtoBottomRight.setFillAfter(true);

		return outtoBottomRight;		
	} 

	//view comes from bottom left corner
	public Animation InFromBottomLeft(){

		Animation infromBottomLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		infromBottomLeft.setDuration(AnimationDuration);
		infromBottomLeft.setFillAfter(true);

		return infromBottomLeft;		
	}

	//view goes to bottom right corner
	public Animation OutToBottomLeft(){

		Animation outtoBottomLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT , 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
		outtoBottomLeft.setDuration(AnimationDuration);
		outtoBottomLeft.setFillAfter(true);

		return outtoBottomLeft;		
	} 

	public Animation FadeIn(){

		AlphaAnimation fadeIn = new AlphaAnimation(0,1);
		fadeIn.setDuration(FadeAnimationDuration);

		return fadeIn;			
	}

	public Animation FadeOut(){

		AlphaAnimation fadeOut = new AlphaAnimation(1,0);
		fadeOut.setDuration(FadeAnimationDuration);

		return fadeOut;			
	}
}
