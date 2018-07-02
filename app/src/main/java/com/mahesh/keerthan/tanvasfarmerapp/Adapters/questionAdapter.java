package com.mahesh.keerthan.tanvasfarmerapp.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.CustomLinearLayoutManager;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.ViewHolder> {

    private ArrayList<QuestionClass> questions;
    private Context context;
    private ItemClickListener itemClickListener;
    private ArrayList<Options> options = new ArrayList<>();
    private optionsAdapter adapter;
    private ViewHolder holderMain;
    private RecyclerView.RecycledViewPool viewPool;

    public questionAdapter(ArrayList<QuestionClass> questions, Context context) {
        this.questions = questions;
        this.context = context;
        viewPool = new RecyclerView.RecycledViewPool();
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
        holderMain = holder;
        holder.questionTV.setText(questions.get(position).getQuestion_content());
        holder.contentQuestionTV.setText(questions.get(position).getQuestion_content());
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.unfold(false);
                holder.optionsList.setRecycledViewPool(viewPool);

                holder.optionsList.setLayoutManager(new CustomLinearLayoutManager(context));
                new fetchOptions().execute(questions.get(position).getQuestion_id());
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

        private questionAdapter adapter;
        private TextView questionTV,contentQuestionTV;
        private TextView answersTV;
        private ImageView verifiedIcon;
        private FoldingCell foldingCell;
        private RecyclerView optionsList;

        public ViewHolder(View itemView) {
            super(itemView);
            questionTV = itemView.findViewById(R.id.othersquestionstextview);
            answersTV = itemView.findViewById(R.id.othersquestionstextviewhidden);
            verifiedIcon = itemView.findViewById(R.id.othersquestionsverified);
            foldingCell = itemView.findViewById(R.id.othersquestionfoldingcell);
            contentQuestionTV = itemView.findViewById(R.id.othersquestionsunfoldedstate);
            optionsList = itemView.findViewById(R.id.otherscheckboxlist);
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

    private class fetchOptions extends AsyncTask<Integer,Void,JSONArray>{

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context,"Loading...","We appreciate your patience");
        }

        @Override
        protected JSONArray doInBackground(Integer... integers) {
            String question_id = integers[0].toString();
            OkHttpClient client = new OkHttpClient();
            try{
                JSONArray array = new JSONArray(APICall.GET(client, RequestBuilder.buildURL("fetchoptions.php",new String[]{"question_id"},new String[]{question_id})));
                for(int i =0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    Options options1 = new Options(object.getString("option_content"),object.getInt("option_id"),object.getInt("question_id"));
                    options.add(options1);
                }

                return array;
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            progressDialog.dismiss();
            Integer size = options.size();
            Log.d("Count",options.get(options.size()-1).getOption_content());
            RecyclerView.Adapter adapter1 = new optionsAdapter(options,context);
            holderMain.optionsList.setAdapter(adapter1);
            adapter1.notifyItemRangeInserted(0,options.size());
        }
    }
}
