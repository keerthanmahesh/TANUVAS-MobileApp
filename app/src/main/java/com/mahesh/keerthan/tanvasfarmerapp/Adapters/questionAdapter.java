package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Responses;
import com.mahesh.keerthan.tanvasfarmerapp.PickOptionDialog;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.ViewHolder> {

    private ArrayList<QuestionClass> questions;
    private Context context;
    private ItemClickListener itemClickListener;
    private ArrayList<Options> options = new ArrayList<>();
    private ArrayList<Responses> responses = new ArrayList<>();
    private OnResult result;

    private ViewHolder mHolder;
    public questionAdapter(ArrayList<QuestionClass> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }

    public void setResponses(ArrayList<Responses> responses) {
        this.responses = responses;
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.question_cell,parent,false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final QuestionClass selectedQuestion = questions.get(position);

        if(responses.size() <= position){
            Responses response = new Responses();
            response.setQuestion(selectedQuestion);
            responses.add(position,response);
            holder.answersTV.setText("");
            holder.verifiedIcon.setAlpha(0f);
        }else if(responses.get(position).getOptions().size() == 0){
            holder.verifiedIcon.setAlpha(0f);
            holder.answersTV.setText("");
        }
        else{
            Log.d("should",responses.get(position).getOptions().size()+"");
            holder.answersTV.setText("");
            for (int i = 0;i < responses.get(position).getOptions().size() - 1; i++){
                String temp = holder.answersTV.getText().toString();
                holder.answersTV.setText(temp + responses.get(position).getOptions().get(i).getOption_content() + ", ");
            }
            String temp = holder.answersTV.getText().toString();
            holder.answersTV.setText(temp + responses.get(position).getOptions().get(responses.get(position).getOptions().size()-1).getOption_content());
            holder.verifiedIcon.setAlpha(1f);
        }
        int temp = position+1;
        holder.questionTV.setText("Q"+temp+": "+questions.get(position).getQuestion_content());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickOptionDialog dialog;
                dialog = new PickOptionDialog(context);
                dialog.setSelectedQuestion(selectedQuestion);
                dialog.setResult(new PickOptionDialog.OnResult() {
                    @Override
                    public void finish(ArrayList<Options> answers) {
                        if(answers.size()>0){
                            responses.remove(position);
                            Responses response = new Responses();
                            response.setQuestion(selectedQuestion);
                            response.clearOptions();
                            response.addMultipleOption(answers);
                            responses.add(position,response);
                            if(result!=null){
                                result.finish(responses);
                            }
                            holder.answersTV.setText("");
                            for (int i = 0;i < answers.size() - 1; i++){
                                String temp = holder.answersTV.getText().toString();
                                holder.answersTV.setText(temp + answers.get(i).getOption_content() + ", ");
                            }
                            String temp = holder.answersTV.getText().toString();
                            holder.answersTV.setText(temp + answers.get(answers.size()-1).getOption_content());
                            holder.verifiedIcon.setAlpha(1f);
                        }else {
                            responses.remove(position);
                            Responses response = new Responses();
                            response.setQuestion(selectedQuestion);
                            response.clearOptions();
                            responses.add(position,response);
                            if(result!=null){
                                result.finish(responses);
                            }
                            holder.answersTV.setText("");
                            holder.verifiedIcon.setAlpha(0f);
                        }
                    }
                });
                dialog.show();
                //fetchOptions options = new fetchOptions();
                //options.execute(selectedQuestion.getQuestion_id());

            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }


    public QuestionClass getQuestion(int id) {
        return questions.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView questionTV;
        private TextView answersTV;
        private ImageView verifiedIcon;
        private CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            questionTV = itemView.findViewById(R.id.othersquestionstextview);
            answersTV = itemView.findViewById(R.id.othersquestionstextviewhidden);
            verifiedIcon = itemView.findViewById(R.id.othersquestionsverified);
            cardView = itemView.findViewById(R.id.othersquestioncardview);
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

    public void setResult(questionAdapter.OnResult result){
        this.result = result;
    }

    public interface OnResult{
        void finish(ArrayList<Responses> responses);
    }
}
