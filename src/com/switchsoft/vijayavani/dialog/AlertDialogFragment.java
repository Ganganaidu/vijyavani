package com.switchsoft.vijayavani.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.switchsoft.vijayavani.MainActivity;
import com.switchsoft.vijayavani.R;

public class AlertDialogFragment extends DialogFragment {

    public static AlertDialogFragment newInstance(String string) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", string);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater

		builder.setIcon(R.drawable.app_icon).setTitle(R.string.app_name).setMessage(title)

                .setPositiveButton(R.string.alert_dialog_yes,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ((MainActivity)getActivity()).doPositiveClick();
                        }
                    }
                )
                .setNegativeButton(R.string.alert_dialog_cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	((MainActivity)getActivity()).doNegativeClick();
                        }
                    });
                    
                return builder.create();
    }

}
