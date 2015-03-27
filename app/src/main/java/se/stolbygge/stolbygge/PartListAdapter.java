package se.stolbygge.stolbygge;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class PartListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Part> parts;

    public PartListAdapter(Context context) {
        this.context = context;
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

        View view;
        if(convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.part_list_item,null);
            ImageView img = (ImageView) view.findViewById(R.id.imageView);

            String s = parts.get(position).getImgName();
            Uri uri = Uri.parse("android.resource://se.stolbygge.stolbygge/drawable/" + parts.get(position).getImgName());
            img.setImageURI(uri);
            TextView text = (TextView) view.findViewById(R.id.textView);
            text.setText(parts.get(position).getName() + " (" + Integer.toString(parts.get(position).getAmount()) + ")");
        } else {
            view = convertView;
        }

        return view;
    }
}
