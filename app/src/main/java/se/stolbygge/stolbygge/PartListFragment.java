package se.stolbygge.stolbygge;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class PartListFragment extends Fragment {

    View rootView;
    ARPartsActivity activity;
    private PartListAdapter adapter;
    private ArrayList<Part> parts;
    ListView partListView;
    Button button_next;

    public PartListFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_part_list, container, false);

        activity = (ARPartsActivity) getActivity();

        partListView = (ListView) rootView.findViewById(R.id.list_view);
        button_next = (Button) rootView.findViewById(R.id.button_next);

        ArrayList<Part> findableParts = Store.getInstance().getFindableParts();
        adapter = new PartListAdapter(this.getActivity(), R.layout.part_list_item, findableParts);

        partListView.setAdapter(adapter);


        init();

        return rootView;
    }

    /*
        Dynamically sets height of listview depending on screen size of device.
     */
    private void init(){
        final LinearLayout l_b = (LinearLayout) rootView.findViewById(R.id.linearlayout_button);
        l_b.post(new Runnable(){
            public void run(){
                int button_height = l_b.getHeight() * 2;

                //Get display height
                DisplayMetrics displaymetrics = new DisplayMetrics();
                ((Activity) rootView.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int display_height = displaymetrics.heightPixels;
                int display_width = displaymetrics.widthPixels;

                int equals = display_height - button_height;

                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) partListView.getLayoutParams();
                lp.height = equals;
                partListView.setLayoutParams(lp);
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onCreateARInstructionsView();
            }
        });
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
