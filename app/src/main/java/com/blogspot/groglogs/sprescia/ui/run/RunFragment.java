package com.blogspot.groglogs.sprescia.ui.run;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.sprescia.R;
import com.blogspot.groglogs.sprescia.ui.menu.RunTopMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import lombok.Getter;


public class RunFragment extends Fragment {

    @Getter
    protected static RunAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_run, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        adapter = new RunAdapter(getActivity().getApplication(), recyclerView);
        recyclerView.setAdapter(adapter);

        //divider between items in view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter.loadAllItems();

        requireActivity().addMenuProvider(new RunTopMenu(requireContext(), adapter), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        requireActivity().invalidateOptionsMenu();

        return view;
    }

    //ensure floating button is not hidden behind bottom navigation bar
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //make sure the BottomNavigationView is available after the layout is drawn
        view.getViewTreeObserver().addOnPreDrawListener(() -> {
            //only access the BottomNavigationView once the layout has been drawn
            if (getActivity() != null) {
                BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);

                if (navView != null) {
                    //get the height of the BottomNavigationView
                    int navViewHeight = navView.getHeight();

                    FloatingActionButton fab = view.findViewById(R.id.fab);

                    if (fab != null) {
                        //adjust the FAB's position dynamically
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) fab.getLayoutParams();
                        params.bottomMargin = navViewHeight + (int) (16 * getResources().getDisplayMetrics().density);//16dp extra spacing
                        fab.setLayoutParams(params);
                    }
                }
            }
            //return true to allow drawing to proceed
            return true;
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(view1 -> {
                Toast.makeText(view1.getContext(), "Opening add item dialog", Toast.LENGTH_SHORT).show();

                adapter.showInsertDialog(getContext());
            });
        }
    }
}