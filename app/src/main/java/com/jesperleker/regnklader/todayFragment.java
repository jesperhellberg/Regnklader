package com.jesperleker.regnklader;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jesperhellberg on 2015-10-14.
 */
public class todayFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        Bundle args = getArguments();
        if(args != null) {
            String day = args.get("day").toString();
            TextView textView = (TextView) view.findViewById(R.id.fragmentHeader);
            textView.setText(day);
        }
        return view;
    }

}
