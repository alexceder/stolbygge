package se.stolbygge.stolbygge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class StepListAdapter extends ArrayAdapter<Step> {

    private Context context;
    private ArrayList<Step> steps;

    public StepListAdapter(Context context, int resource, ArrayList<Step> steps) {
        super(context, resource, steps);
        this.context = context;
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

        Step currentStep = steps.get(position);
        View view;

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.step_list_item,null);

        } else {
            view = convertView;
        }

        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        int id = context.getResources().getIdentifier(currentStep.getImgName(), "drawable", context.getPackageName());
        img.setImageResource(id);

        ArrayList<Part> parts = currentStep.getParts();

        TextView text = (TextView) view.findViewById(R.id.textView);
        String texten = currentStep.getName() + "\n";

        for(int i = 0; i < parts.size(); i++) {

            texten = texten + "\n" + currentStep.getParts().get(i).getName() + " (" + Integer.toString(currentStep.getParts().get(i).getAmount()) + ")";

        }
        text.setText(texten);

        return view;
    }
}

