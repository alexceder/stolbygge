package se.stolbygge.stolbygge;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainPartListAdapter extends ArrayAdapter<Part> {

    protected Context context;
    protected ArrayList<Part> parts;
    protected int resource;
    public int current;

    public MainPartListAdapter (Context context, int resource, ArrayList<Part> parts) {
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

        current = ((ARInstructionsActivity) getContext()).getCurrentTextured();

        if (current == position) {
            view.setBackgroundColor(Color.GREEN);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        ImageView partImage = (ImageView) view.findViewById(R.id.part_image);
        int imgId = context.getResources().getIdentifier(currentPart.getImgName(), "drawable", context.getPackageName());
        partImage.setImageResource(imgId);

        TextView text = (TextView) view.findViewById(R.id.part_text);
        text.setText("x" + Integer.toString(currentPart.getAmount()));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ARInstructionsActivity) context).onClickTexturedPosition(position);

                if (position == current) {
                    current = -1;
                    v.setBackgroundColor(Color.WHITE);
                } else {
                    current = position;
                    v.setBackgroundColor(Color.GREEN);
                }

                notifyDataSetChanged();
            }
        });

        return view;
    }
}
