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
        Part currentPart = parts.get(position);
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resource, null);
        } else {
            view = convertView;
        }

        ImageView img = (ImageView) view.findViewById(R.id.image_view);
        int id = context.getResources().getIdentifier(currentPart.getImgName(), "drawable", context.getPackageName());
        img.setImageResource(id);

        TextView text = (TextView) view.findViewById(R.id.text_view);
        text.setText(currentPart.getName() + " (" + Integer.toString(currentPart.getAmount()) + ")");

        return view;
    }
}
