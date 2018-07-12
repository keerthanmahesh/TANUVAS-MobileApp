package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateQuestionsFragment extends Fragment {

    private View view;
    private List<String> categories = new ArrayList<>();
    private List<String> profileModules = new ArrayList<>();
    private List<String> questionnaireModules = new ArrayList<>();
    private List<String> actions = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.update_questions_fragment,container,false);
        final Spinner categorySpinner = view.findViewById(R.id.categorySpinner),
                moduleSpinner = view.findViewById(R.id.moduleSpinner),
                actionSpinner = view.findViewById(R.id.actionSpinner);
        inflateLists();
        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,categories);
        ArrayAdapter<String> actionsAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,actions);
        categorySpinner.setAdapter(categoriesAdapter);
        actionSpinner.setAdapter(actionsAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    ArrayAdapter<String> modulesAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,profileModules);
                    moduleSpinner.setAdapter(modulesAdapter);
                }else {
                    ArrayAdapter<String> modulesAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,questionnaireModules);
                    moduleSpinner.setAdapter(modulesAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorySpinner.setSelection(0);

            }
        });
        return view;

    }

    private void inflateLists(){
        String[] categories = new String[]{"Profile","Questionnaire"};
        String[] profileModules = new String[]{"Land Holding","Others"};
        String[] questionnaireModules = new String[]{"Agriculture","Horticulture","LiveStock"};
        String[] actions = new String[]{"Add Question", "Edit Question", "Delete Question"};
        this.categories.addAll(Arrays.asList(categories));
        this.profileModules.addAll(Arrays.asList(profileModules));
        this.questionnaireModules.addAll(Arrays.asList(questionnaireModules));
        this.actions.addAll(Arrays.asList(actions));
    }
}
