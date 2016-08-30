package com.switchsoft.vijayavani.view;

import android.app.Activity;

import com.switchsoft.vijayavani.map.view.ActivityHostFragment;
import com.switchsoft.vijayavani.map.view.MyMapActivity;

public class Map extends ActivityHostFragment {
    
    @Override
    protected Class<? extends Activity> getActivityClass() {
        return MyMapActivity.class;
    }

}
