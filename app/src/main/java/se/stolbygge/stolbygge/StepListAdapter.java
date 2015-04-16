package se.stolbygge.stolbygge;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import se.stolbygge.stolbygge.InstructionsActivity;

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


        //DisplayMetrics metrics = new DisplayMetrics();
        //WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //windowManager.getDefaultDisplay().getMetrics(metrics);


        //int screenHeight = metrics.heightPixels;
        //int screenWidth = metrics.widthPixels;

        //Log.d("agil ***" , "height " + Integer.toString(screenHeight) + " * " + Integer.toString(screenWidth));

        int parentHeight = parent.getHeight();
        int parentWidth = parent.getWidth();
        Log.d("*** height/2",Integer.toString(parentHeight/2));

        //Log.d("*** parent ", Integer.toString(parentHeight));
        //Log.d("*** parent/getcount ", Integer.toString(parentHeight/getCount()));
        final Step currentStep = steps.get(position);
        View view;

        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.step_list_item,null);

        } else {
            view = convertView;
        }

        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setMinimumHeight(parentHeight / 2);
        //img.setMaxWidth(parentWidth/2);
        int imgId = context.getResources().getIdentifier(currentStep.getImgName(), "drawable", context.getPackageName());
        img.setImageResource(imgId);

        ArrayList<Part> parts = currentStep.getParts();

        TextView text = (TextView) view.findViewById(R.id.textView);
        String texten = "";//currentStep.getName() + "\n";

        for(int i = 0; i < parts.size(); i++) {

            texten = texten + "\n" + currentStep.getParts().get(i).getName() + " (" + Integer.toString(currentStep.getParts().get(i).getAmount()) + ")";

        }

        text.setText(texten);

        view.setMinimumHeight(parentHeight);

        final Button checkBtn = (Button) view.findViewById(R.id.item_checkbox);
        int color = (currentStep.isChecked()) ? context.getResources().getColor(R.color.step_done) : context.getResources().getColor(R.color.step_left);
        checkBtn.setBackgroundColor(color);

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

                InstructionsActivity c = (InstructionsActivity) getContext();
                c.scrollTo(currentStep.getStepNr());
            }
        });
        return view;
    }
}

