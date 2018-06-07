package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.Calendar;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        populateSetDate(year, month+1, dayOfMonth);
        populateSetAge(year,month+1,dayOfMonth);
    }

    public void populateSetDate(int year, int month, int day) {
        TextView dob = (TextView) getActivity().findViewById(R.id.DOBtextView);
        dob.setText(day+"/"+month+"/"+year);
    }
    public void populateSetAge( int year, int month , int day){
        TextView ageTextView = (TextView) getActivity().findViewById(R.id.ageTextView);
        Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH);
        int age = currentYear - year ;
        if(currentMonth>month && age!=0){
            age--;
        }
        else if (currentMonth == month){
            int currentDate = c.get(Calendar.DAY_OF_MONTH);
            if(currentDate>day)
                age-- ;
        }
        ageTextView.setText(age+"");
    }

}
