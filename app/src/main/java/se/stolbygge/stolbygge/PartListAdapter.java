package se.stolbygge.stolbygge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PartListAdapter extends ArrayAdapter<Part> {

    protected Context context;
    protected ArrayList<Part> parts;
    protected int resource;
    public int current = 0;

    public PartListAdapter(Context context, int resource, ArrayList<Part> parts) {
        super(context, resource, parts);
        this.context = context;
        this.resource = resource;
        this.parts = parts;
    }

    @Override
    public int getCount() {
        return parts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Part getItem(int position) {
        return parts.get(position);
    }

    public void setParts(ArrayList<Part> parts) {
        this.parts = parts;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final Part currentPart = parts.get(position);
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        } else {
            view = convertView;
        }

        if (position == current) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.bgc_current));
        } else if (currentPart.isFound()) {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.bgc_done));
        } else {
            view.setBackgroundColor(getContext().getResources().getColor(R.color.bgc_AR_view_unvisited));
        }

        ImageView partImage = (ImageView) view.findViewById(R.id.part_image);
        int imgId = context.getResources().getIdentifier(currentPart.getImgName(), "drawable", context.getPackageName());
        partImage.setImageResource(imgId);

        TextView text = (TextView) view.findViewById(R.id.part_text);
        text.setText("x" + Integer.toString(currentPart.getAmount()));

        final ImageView checkboxImage = (ImageView) view.findViewById(R.id.part_checkbox);
        String imgSource = (currentPart.isFound()) ? "checkbox_checked" : "checkbox_unchecked";
        imgId = context.getResources().getIdentifier(imgSource, "drawable", context.getPackageName());
        checkboxImage.setImageResource(imgId);

        final ARPartsActivity activity = (ARPartsActivity) context;

        checkboxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current = (currentPart.isFound()) ? position : position+1;

                currentPart.setFound(!currentPart.isFound());
                String imgSource = (currentPart.isFound()) ? "checkbox_checked" : "checkbox_unchecked";
                int imgId = context.getResources().getIdentifier(imgSource, "drawable", context.getPackageName());
                checkboxImage.setImageResource(imgId);

                activity.onClickPosition(current);
                notifyDataSetChanged();
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = position;
                activity.onClickPosition(current);
                notifyDataSetChanged();
            }
        });

        return view;
    }
}
