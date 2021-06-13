package com.cc.cclean;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class SystemApps extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<AppInfo> installedApps;
    private AppsManager appManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_system_apps, container, false);
        installedApps = new ArrayList<AppInfo>();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        shareButton = (FloatingActionButton) findViewById(R.id.sharebutton);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        appManager = new AppsManager(getActivity());
        installedApps = appManager.getApps(1);

        // Initialize a new adapter for RecyclerView
        mAdapter = new InstalledAppsAdapter(
                getActivity().getApplicationContext(),
                installedApps
        );

        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}