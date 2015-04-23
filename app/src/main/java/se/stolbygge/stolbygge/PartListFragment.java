package se.stolbygge.stolbygge;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class PartListFragment extends Fragment {

    private PartListAdapter adapter;
    private ArrayList<Part> parts;

    public PartListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.part_list, container, false);

        ListView partListView = (ListView) rootView.findViewById(R.id.list_view);

        parts = new ArrayList<>();
        adapter = new PartListAdapter(this.getActivity(), R.layout.part_list_item, parts);

        parts.add(new Part("Insexskruv", "1", "insexskruv", 6));
        parts.add(new Part("Insexnyckel", "1", "insexnyckel", 1));
        parts.add(new Part("Plugg", "1", "plugg", 2));
        parts.add(new Part("Vänster benpar", "1", "vanster_benpar", 1));
        parts.add(new Part("Höger benpar", "1", "hoger_benpar", 1));
        parts.add(new Part("Sitts", "1", "sitts", 1));
        parts.add(new Part("Ryggstöd", "1", "ryggstod", 1));
        parts.add(new Part("Ryggstödsdekoration", "1", "ryggstodsdekoration", 1));

        adapter.setParts(parts);
        partListView.setAdapter(adapter);

        return rootView;
    }
}
