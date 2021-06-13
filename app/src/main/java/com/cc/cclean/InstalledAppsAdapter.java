package com.cc.cclean;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InstalledAppsAdapter extends RecyclerView.Adapter<InstalledAppsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AppInfo> mDataSet;

    public InstalledAppsAdapter(Context context, ArrayList<AppInfo> list) {
        mContext = context;
        mDataSet = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewLabel;
        public TextView mTextViewPackage;
        public ImageView mImageViewIcon;
        public CheckBox mAppSelect;
        public RelativeLayout mItem;

        public ViewHolder(View v) {
            super(v);
            // Get the widgets reference from custom layout
            mTextViewLabel = (TextView) v.findViewById(R.id.Apk_Name);
            mTextViewPackage = (TextView) v.findViewById(R.id.Apk_Package_Name);
            mImageViewIcon = (ImageView) v.findViewById(R.id.packageImage);
            mItem = (RelativeLayout) v.findViewById(R.id.item);
        }

    }

    @Override
    public InstalledAppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.text_row_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // Get the current package name
        final String packageName = mDataSet.get(position).getAppPackage();

        // Get the current app icon
        Drawable icon = mDataSet.get(position).getAppIcon();

        // Get the current app label
        String label = mDataSet.get(position).getAppName();

        // Set the current app label
        holder.mTextViewLabel.setText(label);

        // Set the current app package name
        holder.mTextViewPackage.setText(packageName);

        // Set the current app icon
        holder.mImageViewIcon.setImageDrawable(icon);


        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);



            }
        });


    }

    @Override
    public int getItemCount() {
        // Count the installed apps
        return mDataSet.size();
    }

}