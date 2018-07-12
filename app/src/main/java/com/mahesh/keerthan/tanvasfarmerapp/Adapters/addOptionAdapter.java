package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.ArrayList;
import java.util.UUID;

public class addOptionAdapter extends RecyclerView.Adapter<addOptionAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Options> options = new ArrayList<>();
    private int question_id;


    public addOptionAdapter(Context context, int question_id) {
        this.context = context;
        this.question_id = question_id;
    }

    public void addOption(){
        options.add(new Options("",0,question_id));
        this.notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.options_text_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.editText.setText(options.get(position).getOption_content());
        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(holder.editText.getText().toString())){
                    Options options = new Options(holder.editText.getText().toString(), 0,question_id);
                    addOptionAdapter.this.options.add(position,options);
                }
                else
                    holder.editText.setError("Please Enter the Content");
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText editText;
        private Button saveBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.optionsEditText);
            saveBtn = itemView.findViewById(R.id.saveBtn);
        }
    }

}
