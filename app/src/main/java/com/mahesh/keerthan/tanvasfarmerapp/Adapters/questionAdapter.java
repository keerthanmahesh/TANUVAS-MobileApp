package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.ViewHolder> {

    private ArrayList<QuestionClass> questions;
    private LayoutInflater mInflater;
    private ItemClickListener itemClickListener;

    public questionAdapter(ArrayList<QuestionClass> questions, LayoutInflater mInflater) {
        this.questions = questions;
        this.mInflater = mInflater;
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.question_cell,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionTV.setText(questions.get(position).getQuestion_content());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }


    public QuestionClass getQuestion(int id) {
        return questions.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private questionAdapter adapter;
        private TextView questionTV;
        private TextView answersTV;
        private ImageView verifiedIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            questionTV = itemView.findViewById(R.id.othersquestionstextview);
            answersTV = itemView.findViewById(R.id.othersquestionstextviewhidden);
            verifiedIcon = itemView.findViewById(R.id.othersquestionsverified);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
