package com.training.ojolusertraining.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.training.ojolusertraining.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProsesFragment extends Fragment{

    public ProsesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_proses, container, false);

        return view;
    }

}
