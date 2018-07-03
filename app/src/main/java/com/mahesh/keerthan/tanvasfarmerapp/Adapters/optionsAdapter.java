package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class optionsAdapter extends RecyclerView.Adapter<optionsAdapter.ViewHolder>{

    private ArrayList<Options> options;
    private Context context;

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
    public void onBindViewHolder(@NonNull optionsAdapter.ViewHolder holder, int position) {
        holder.textView.setText(options.get(position).getOption_content());

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
