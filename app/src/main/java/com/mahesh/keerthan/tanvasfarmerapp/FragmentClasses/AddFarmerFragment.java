package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.Calendar;


public class AddFarmerFragment extends Fragment implements CalendarDatePickerDialogFragment.OnDateSetListener, View.OnClickListener{

    private View view;
    private Button calender_button;
    @Nullable
    @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.add_farmer_fragment,container,false);
            calender_button = (Button) view.findViewById(R.id.DOBbuttonAddfarmer);
            calender_button.setOnClickListener(this);
            return view;
        }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onClick(View v) {
        
    }
}



