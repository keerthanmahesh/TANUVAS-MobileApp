package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mahesh.keerthan.tanvasfarmerapp.HomeActivity;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.Calendar;


public class AddFarmerFragment extends Fragment implements View.OnClickListener{

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private View view;
    private Button calender_button;
    private static int id1;
    @Nullable
    @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            setHasOptionsMenu(true);
            view = inflater.inflate(R.layout.add_farmer_fragment,container,false);
            calender_button = (Button) view.findViewById(R.id.DOBbuttonAddfarmer);
            calender_button.setOnClickListener(this);
            dateView = view.findViewById(R.id.DOBtextView);
            calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
             month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            getActivity().invalidateOptionsMenu();
            return view;
        }



    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.DOBbuttonAddfarmer){
            android.support.v4.app.DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getChildFragmentManager(), "DatePicker");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save ) {


        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem save_item = menu.findItem(R.id.action_save);
        save_item.setVisible(true);
        MenuItem settings_item = menu.findItem(R.id.action_settings);
        settings_item.setVisible(false);
    }


}



