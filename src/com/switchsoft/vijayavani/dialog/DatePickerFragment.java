package com.switchsoft.vijayavani.dialog;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment
implements DatePickerDialog.OnDateSetListener {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		/*if(ViewData.view_fromdate == true){
			ViewData.view_fromdate = false;
			ViewData.viewdata.fromDate(year, month, day);
			
		} else if(ViewData.view_todate == true){
			ViewData.view_todate = false;
			ViewData.viewdata.todate(year, month, day);
			
		} else {
			EnterDataView.enterdata.update_date(year, month, day);
		}*/
		
//		DashboardView.dashboardview.selected_date(year, month+1, day);
	}
}

