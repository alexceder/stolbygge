package se.stolbygge.stolbygge;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ARInstructionsFragment extends Fragment {

    View rootView;
    int currentStep = 1;
    ARInstructionsActivity activity;
    ArrayList<Step> steps;
    ArrayList<Part> parts;
    ARInstructionsPartListAdapter adapter;
    TextView stepText;

    public ARInstructionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_arinstructions, container, false);

        init();

        return rootView;
    }

    private void init() {

        activity = (ARInstructionsActivity) getActivity();

        //init next step button
        Button nextButton = (Button) rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentStep++;
                onStepClick(currentStep);
            }
        });

        steps = Store.getInstance().getSteps();

        LinearLayout progressListView = (LinearLayout) rootView.findViewById(R.id.listview_progresslist);

        // Create a list of navigation buttons
        for (int i = 0; i < steps.size(); i++) {

            int color = (i < currentStep) ? getResources().getColor(R.color.bgc_current) : getResources().getColor(R.color.bgc_progressbar_unvisited);
            final Button button = new Button(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            button.setId(i);
            button.setText("Steg " + Integer.toString(i + 1));
            button.setLayoutParams(params);
            button.setBackgroundColor(color);

            // Clicking on one of the buttons redirect to that step
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //scrollTo(button.getId());
                }
            });
            progressListView.addView(button);
        }

        stepText = (TextView) rootView.findViewById(R.id.step_text);
        stepText.setText(Integer.toString(currentStep));

        // Create list of parts belonging to the step, start with position 0
        createPartList(0);
    }

    // Create the list of parts needed for the specific step
    private void createPartList(int position) {

        ArrayList<Part> stepParts = steps.get(position).getParts();
        adapter = new ARInstructionsPartListAdapter(getActivity(), R.layout.instructions_part_list_item, stepParts);
        ListView partList = (ListView) rootView.findViewById(R.id.listview_stepparts);
        partList.setAdapter(adapter);
    }

    // Used when clicking on next-button and on the navigation bar
    // goes to the next step in instructions
    private void onStepClick(int position) {

        if (position != 6) { //TODO set to step size
            currentStep = position;
            activity.setStepView(currentStep);
            stepText.setText(Integer.toString(currentStep));

            // TODO update part list
        }
    }

}
