package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
    private View.OnClickListener editListener;
    private ArrayList<Boolean> isSaved = new ArrayList<>();
    private OnResult result;


    public addOptionAdapter(Context context, int question_id) {
        this.context = context;
        this.question_id = question_id;
    }

    public void addOption(){
        options.add(new Options("",0,question_id));
        isSaved.add(new Boolean(false));
        notifyDataSetChanged();
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

        holder.position = position;
        holder.editText.setText(options.get(position).getOption_content());
        final View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(holder.editText.getText().toString())){
                    Options options = new Options(holder.editText.getText().toString(), 0,question_id);
                    addOptionAdapter.this.options.remove(position);
                    addOptionAdapter.this.options.add(position,options);
                    addOptionAdapter.this.isSaved.remove(position);
                    addOptionAdapter.this.isSaved.add(position,new Boolean(true));
                    holder.editText.setEnabled(false);
                    holder.saveBtn.setText("Edit");
                    holder.saveBtn.setOnClickListener(editListener);
                }
                else
                    holder.editText.setError("Please Enter the Content");
            }
        };

        editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionAdapter.this.isSaved.remove(position);
                addOptionAdapter.this.isSaved.add(position,new Boolean(false));
                holder.editText.setEnabled(true);
                holder.saveBtn.setOnClickListener(saveListener);
                holder.saveBtn.setText("Save");
            }
        };
        if(!addOptionAdapter.this.isSaved.get(position)){
            holder.saveBtn.setOnClickListener(holder.saveListener);
            holder.editText.setEnabled(true);
            holder.saveBtn.setText("Save");
            if(TextUtils.isEmpty(holder.editText.getText().toString()))
                holder.editText.setError("Please Enter the Content");
            else
                holder.editText.setError(null);
        }else {
            holder.saveBtn.setOnClickListener(holder.editListener);
            holder.editText.setEnabled(false);
            holder.saveBtn.setText("Edit");
            holder.editText.setError(null);
        }

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private EditText editText;
        private Button saveBtn;
        private int position;
        private View.OnClickListener saveListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(editText.getText().toString())){
                    Options options = new Options(editText.getText().toString(), 0,question_id);
                    addOptionAdapter.this.options.remove(position);
                    addOptionAdapter.this.options.add(position,options);
                    addOptionAdapter.this.isSaved.remove(position);
                    addOptionAdapter.this.isSaved.add(position,new Boolean(true));
                    //if(result!=null)
                        result.finish(addOptionAdapter.this.options);
                    editText.setEnabled(false);
                    saveBtn.setText("Edit");
                    saveBtn.setOnClickListener(editListener);
                }
                else
                    editText.setError("Please Enter the Content");
            }
        },
        editListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOptionAdapter.this.isSaved.remove(position);
                addOptionAdapter.this.isSaved.add(position,new Boolean(false));
                editText.setEnabled(true);
                saveBtn.setOnClickListener(saveListener);
                saveBtn.setText("Save");
            }
        };


        public ViewHolder(View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.optionsEditText);
            saveBtn = itemView.findViewById(R.id.saveBtn);

        }
    }

    public void setResult(addOptionAdapter.OnResult result){
        this.result = result;
    }

    public interface OnResult{
        void finish(ArrayList<Options> options);
    }

}
