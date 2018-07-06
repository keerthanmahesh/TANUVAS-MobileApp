package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Responses;
import com.mahesh.keerthan.tanvasfarmerapp.PickOptionDialog;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class optionsAdapter extends RecyclerView.Adapter<optionsAdapter.ViewHolder>{

    private ArrayList<Options> options;
    private ArrayList<Options> selectedOptions = new ArrayList<>();
    private Context context;
    private OnResult result;

    public optionsAdapter(ArrayList<Options> options, Context context) {
        this.options = options;
        this.context = context;
    }

    @NonNull
    @Override
    public optionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View view = inflater.inflate(R.layout.check_box_item,parent,false);
         return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final optionsAdapter.ViewHolder holder, final int position) {
        holder.textView.setText(options.get(position).getOption_content());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedOptions.add(options.get(position));
                    if(optionsAdapter.this.result!=null){
                        result.finish(selectedOptions);
                    }
                }else {
                    selectedOptions.remove(options.get(position));
                    if(optionsAdapter.this.result!=null){
                        result.finish(selectedOptions);
                    }
                }
            }
        });

    }

    public void setResult(optionsAdapter.OnResult result){
        this.result = result;
    }


    public interface OnResult{
        void finish(ArrayList<Options> selectedOptions);
    }


    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.othersquestionscheckboxtextview);
            checkBox = itemView.findViewById(R.id.othersquestionscheckbox);
        }
    }
}
