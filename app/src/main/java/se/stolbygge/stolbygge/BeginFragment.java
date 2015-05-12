package se.stolbygge.stolbygge;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class BeginFragment extends Fragment {

    View rootView;
    BeginActivity activity;
    TextView welcometext;
    Button button_productlist;
    Button button_assemble;
    Button button_evaluate;

    public BeginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_begin, container, false);

        activity = (BeginActivity) getActivity();

        welcometext = (TextView) rootView.findViewById(R.id.welcomeText);
        welcometext.setText("Welcome to the assembly aid to Kritter!");
        welcometext.setTextSize(20.0f);

        button_productlist = (Button) rootView.findViewById(R.id.button_productlist);
        button_assemble = (Button) rootView.findViewById(R.id.button_assemble);
        button_evaluate = (Button) rootView.findViewById(R.id.button_evaluate);

        init();

        return rootView;
    }


    private void init(){

        //TODO: Add the functions for the buttons

        // TODO: BÖRJA HÄR!

        button_productlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               activity.onCreateProductList();
            }
        });

        button_assemble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onCreateARInstructionsView();
            }
        });

    }

}