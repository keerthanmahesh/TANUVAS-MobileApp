package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mahesh.keerthan.tanvasfarmerapp.R;

public class InputFarmersDialog extends DialogFragment{

    private EditText input;
    private Button doneButton;

    Callback callback;
    public static interface Callback{
        public void Done(int farmers);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.input_farmers_dialog_fragment,null);
        builder.setView(view).setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    callback = (Callback) getTargetFragment();
                }catch (ClassCastException e){

                    throw new ClassCastException(getActivity().toString()
                            + " must implement NoticeDialogListener");
                }

                callback.Done(Integer.parseInt(input.getText().toString()));
                Log.v("ADebugTag","Value:"+input.getText().toString());

            }
        }).setTitle("Enter Number Of Farmers");
        input = view.findViewById(R.id.input);
        return builder.create();
    }


    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.input_farmers_dialog_fragment,container);
        input = view.findViewById(R.id.input);
        doneButton = view.findViewById(R.id.doneBtn);
        getDialog().setTitle("Enter Number of Farmers");
        View.OnClickListener doneBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback callback = null;
                try
                {
                    callback = (Callback) getTargetFragment();
                }
                catch (ClassCastException e)
                {
                    Log.e(this.getClass().getSimpleName(), "Callback of this class must be implemented by target fragment!", e);
                    throw e;
                }
                if(callback!=null){
                    callback.Done(Integer.parseInt(input.getText().toString()));
                }
            }
        };
        doneButton.setOnClickListener(doneBtnListener);
        return view;
    }*/

    public InputFarmersDialog() {
    }

}
