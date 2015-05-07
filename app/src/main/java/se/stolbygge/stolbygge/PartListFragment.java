package se.stolbygge.stolbygge;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class PartListFragment extends Fragment {

    private PartListAdapter adapter;
    private ArrayList<Part> parts;
    ListView partListView;

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

        partListView = (ListView) rootView.findViewById(R.id.list_view);

        ArrayList<Part> findableParts = Store.getInstance().getFindableParts();
        adapter = new PartListAdapter(this.getActivity(), R.layout.part_list_item, findableParts);

        partListView.setAdapter(adapter);

        return rootView;
    }

    public void onFound(final int position) {
        adapter.getItem(position).setFound(true);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                adapter.current = position+1;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }
}
