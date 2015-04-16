package se.stolbygge.stolbygge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class StepListAdapter extends ArrayAdapter<Step> {

    private Context context;
    private ArrayList<Step> steps;
    int resource;

    public StepListAdapter(Context context, int resource, ArrayList<Step> steps) {
        super(context, resource, steps);
        this.context = context;
        this.resource = resource;
        this.steps = steps;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Step getItem(int position) {
        return steps.get(position);
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View view;
        final Step currentStep = steps.get(position);
        ArrayList<Part> parts = currentStep.getParts();

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(resource,null);

        } else {
            view = convertView;
        }

        int parentHeight = parent.getHeight();

        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        int imgId = context.getResources().getIdentifier(currentStep.getImgName(), "drawable", context.getPackageName());
        img.setImageResource(imgId);

        TextView textView = (TextView) view.findViewById(R.id.textView);
        String text = "";
        for(int i = 0; i < parts.size(); i++) {
            text = text + "\n" + currentStep.getParts().get(i).getName() + " (" + Integer.toString(currentStep.getParts().get(i).getAmount()) + ")";
        }
        textView.setText(text);
        view.setMinimumHeight(parentHeight);

        // Check button for item view
        final Button checkBtn = (Button) view.findViewById(R.id.item_checkbox);
        int color = (currentStep.isChecked()) ? context.getResources().getColor(R.color.step_done) : context.getResources().getColor(R.color.step_left);
        checkBtn.setBackgroundColor(color);

        // Check button is used as a checkbox. When checked it changes color and directs to the next step.
        // When unchecked it only changes color.
        checkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentStep.isChecked()) {
                    currentStep.setChecked(false);
                } else {
                    currentStep.setChecked(true);
                }
                int color = (currentStep.isChecked()) ? context.getResources().getColor(R.color.step_done) : context.getResources().getColor(R.color.step_left);
                checkBtn.setBackgroundColor(color);

                RelativeLayout relativeLayout = (RelativeLayout) parent.getParent();
                Button btn = (Button) relativeLayout.findViewById(currentStep.getStepNr()-1);
                btn.setBackgroundColor(color);

                // If checked, scroll to next step
                if(currentStep.isChecked()) {

                    InstructionsActivity activity = (InstructionsActivity) getContext();
                    activity.scrollTo(currentStep.getStepNr());
                }
            }
        });
        return view;
    }
}
