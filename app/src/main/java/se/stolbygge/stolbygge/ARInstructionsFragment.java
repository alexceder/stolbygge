package se.stolbygge.stolbygge;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ARInstructionsFragment extends Fragment {

    /**
     * The current step, 0-indexed.
     */
    private int currentStep;

    /**
     * All the steps. Used when updating the adapter for current step.
     */
    private ArrayList<Step> steps;

    /**
     * Abstracts the current steps parts.
     */
    private ARInstructionsPartListAdapter adapter;

    /**
     * The current steps heading view.
     */
    private TextView stepHeadingView;

    private ImageView stepImageView;

    private LinearLayout progressListView;

    private boolean paused;

    Fragment thisFragment;

    ARInstructionsActivity activity;

    public ARInstructionsFragment() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_ar_instructions, container, false);

        // Get steps
        steps = Store.getInstance().getSteps();

        // We start at step 0
        currentStep = 0;

        // Create list of parts belonging to the step, i.e. 0.
        createPartList(rootView, currentStep);

        // Store this in a variable so we dont have to findViewById every click.
        stepHeadingView = (TextView) rootView.findViewById(R.id.step_text);

        // Store the imageView so we can easily change the step corner picture
        stepImageView = (ImageView) rootView.findViewById(R.id.chairView);

        // Next step button
        Button nextButton = (Button) rootView.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new NextButtonListener());

        // Pause animation button
        Button pauseToggleButton = (Button) rootView.findViewById(R.id.pause_toggle_button);
        pauseToggleButton.setOnClickListener(new ToggleAnimationButtonListener());

        // Progress bar (list)
        progressListView = (LinearLayout) rootView.findViewById(R.id.listview_progresslist);

        // Create a list of navigation buttons (for progress bar)
        for (int i = 0; i < steps.size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);

            Button button = new Button(getActivity());
            button.setId(i);
            button.setText(getString(R.string.step) + " " + Integer.toString(i + 1));
            button.setLayoutParams(params);
            button.setOnClickListener(new ProgressButtonListener());

            progressListView.addView(button);
        }

        thisFragment = this;

        activity = (ARInstructionsActivity) getActivity();

        // Run this method.
        // This might be a bit uncessesary -- but we keep all the "current nuisance" in one place.
        updateToCurrentStep();

        ImageButton button_questionmark = (ImageButton) rootView.findViewById(R.id.button_questionmark);
        button_questionmark.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ImageView help_overlay = (ImageView)getActivity().findViewById(R.id.overlay_assemble);
                if(help_overlay.getVisibility() == View.GONE)
                    help_overlay.setVisibility(View.VISIBLE);
                else
                    help_overlay.setVisibility((View.GONE));
            }
        });

        return rootView;
    }


    /**
     * Create the list of parts needed for the specific step
     */
    private void createPartList(View rootView, int position) {
        ArrayList<Part> parts = steps.get(position).getParts();
        adapter = new ARInstructionsPartListAdapter(
                getActivity(), R.layout.instructions_part_list_item, parts);

        // Hook up the view with the adapter.
        ListView partList = (ListView) rootView.findViewById(R.id.listview_stepparts);
        partList.setAdapter(adapter);
    }

    /**
     * Update view to current step's list of parts adapter and updates the heading.
     */
    private void updateToCurrentStep() {
        // Change heading
        stepHeadingView.setText(
                getString(R.string.step) + " " + Integer.toString(steps.get(currentStep).getStepNr()));

        // Highlight progressbar
        // This could be done as per next section if this was a list adapter
        // however this is easier -- but uglier.
        for (int i = 0; i < progressListView.getChildCount(); ++i) {
            progressListView.getChildAt(i).setBackgroundColor(
                    getResources().getColor(R.color.bgc_current));
        }
        progressListView.findViewById(currentStep).setBackgroundColor(
                getResources().getColor(R.color.bgc_progressbar_unvisited));

        //grab the id for the current step
        int stepCornerPic = getActivity().getResources().getIdentifier("hela_stolen_steg" + (currentStep + 1), "drawable", getActivity().getPackageName());

        // Do not change picture if the step is only related to the screws
        if (currentStep < 4)
            stepImageView.setImageResource(stepCornerPic);
        else
            stepImageView.setImageResource(R.drawable.hela_stolen_steg4);

        // Update step parts list adapter
        ArrayList<Part> currentStepParts = steps.get(currentStep).getParts();
        adapter.setParts(currentStepParts);
        adapter.notifyDataSetChanged();
    }

    private class ProgressButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO: Clicking on one of the buttons should scroll to that step
            // Not sure this is still valid? Do we ever scroll?
            //scrollTo(button.getId());

            // Don't think we need to show the previous step in the progress bar.
            // Then we need some kind of DONE button in addition to the NEXT
            // button so that the user may finish the step.
            // One idea is to keep a queue of visited steps so you can see which
            // path the user has taken.. but.. overkill/unnecessary?
            //progressListView.findViewById(currentStep).setBackgroundColor(Color.RED);

            // This is nicer -- but we don't want to repeat ourselves
            //v.setBackgroundColor(Color.GREEN);

            int next = v.getId();
            ((ARInstructionsActivity) getActivity()).setStep(currentStep, next);
            currentStep = next;

            updateToCurrentStep();
        }
    }

    private class NextButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (currentStep + 1 < steps.size()) {
                ((ARInstructionsActivity) getActivity()).setStep(currentStep++, currentStep);
                updateToCurrentStep();
            } else {
                // This may very well just act as a placeholder. Just wanted some kind of
                // feedback for when you click next.
                // An alternative could be to hide the next button when you come to the last step
                // and always show some kind of DONE button or something.
                new AlertDialog.Builder(getActivity())
                        .setTitle("You've made it!")
                        .setMessage("Please give us five stars!")
                        .setNegativeButton("No,thanks", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        })
                        .setPositiveButton("Yes, I would like that!", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                activity.openRating();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        }
    }

    private class ToggleAnimationButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            ((ARInstructionsActivity) getActivity()).togglePauseStepAnimation(currentStep);

            // TODO: Should probably add a current state to this. But I cba right now.
            Button btn = (Button) v;
            if (getString(R.string.show_step_animation).equals(btn.getText())) {
                btn.setText(getString(R.string.show_step_animation_pause));
            } else {
                btn.setText(getString(R.string.show_step_animation));
            }
        }
    }
}
