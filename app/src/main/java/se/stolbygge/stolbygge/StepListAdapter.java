package se.stolbygge.stolbygge;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);

        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;

        Log.d("agil ***" , "height " + Integer.toString(screenHeight) + " * " + Integer.toString(screenWidth));

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

        TextView text = (TextView) view.findViewById(R.id.textView);
        text.setText(currentStep.getName());

        view.setMinimumHeight(screenHeight);

        return view;
    }
}

