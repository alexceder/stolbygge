package se.stolbygge.stolbygge;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class RatingFragment extends Fragment {

    View rootView;
    ARInstructionsActivity activity;
    Fragment thisFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_rating, container, false);
        activity = (ARInstructionsActivity) getActivity();
        thisFragment = this;

        Button cancelBtn = (Button) rootView.findViewById(R.id.cancel_button);
        Button submitBtn = (Button) rootView.findViewById(R.id.submit_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.closeRating(thisFragment);
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.closeRating(thisFragment);
            }
        });
        return rootView;
    }
}
