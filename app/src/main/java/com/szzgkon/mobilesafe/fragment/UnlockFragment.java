package com.szzgkon.mobilesafe.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.szzgkon.mobilesafe.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnlockFragment extends Fragment {


    private TextView tv_unlock;
    private ListView list_view;
    private View view;

    public UnlockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.item_unlock_fragment, null);

        list_view = (ListView) view.findViewById(R.id.list_view);

        tv_unlock = (TextView) view.findViewById(R.id.tv_unlock);


        return view;
    }

}
