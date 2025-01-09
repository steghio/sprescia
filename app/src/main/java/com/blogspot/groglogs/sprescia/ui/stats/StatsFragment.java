package com.blogspot.groglogs.sprescia.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.sprescia.ui.fragment.AbstractFragment;

public class StatsFragment extends AbstractFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return buildView(inflater, container, false);
    }
}