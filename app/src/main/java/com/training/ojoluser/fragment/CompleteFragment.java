package com.training.ojoluser.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.training.ojoluser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteFragment extends Fragment {


    public CompleteFragment() {
        // Required empty public constructor
    }
//ghghghghg

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

}
