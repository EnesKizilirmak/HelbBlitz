package com.example.helbblitz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public abstract class BaseFragment extends Fragment {

    // -------------- Declaration des elements --------------------------- //
    protected View parentView;
    protected TextView state_textView_v2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        state_textView_v2 = parentView.findViewById(R.id.api_state_v2);
        return parentView;
    }

}
