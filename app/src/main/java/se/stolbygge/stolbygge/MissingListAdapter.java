package se.stolbygge.stolbygge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MissingListAdapter extends PartListAdapter {

    public MissingListAdapter(Context context, int resource, ArrayList<Part> parts) {
        super(context, resource, parts);
        this.context = context;
        this.resource = resource;
        this.parts = parts;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Part currentPart = parts.get(position);
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        } else {
            view = convertView;
        }

        ImageView img = (ImageView) view.findViewById(R.id.missing_part_image);
        int id = context.getResources().getIdentifier(currentPart.getImgName(), "drawable", context.getPackageName());
        img.setImageResource(id);

        TextView text = (TextView) view.findViewById(R.id.missing_part_name);
        text.setText(currentPart.getName() + " (" + Integer.toString(currentPart.getAmount()) + ")");

        return view;
    }
}
