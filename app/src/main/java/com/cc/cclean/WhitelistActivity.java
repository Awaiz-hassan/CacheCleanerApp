/*
 * Copyright 2020 Hunter J Drum
 */

package com.cc.cclean;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.fxn.stash.Stash;

import java.io.File;
import java.util.List;

public class WhitelistActivity extends AppCompatActivity {

    ListView listView;
    BaseAdapter adapter;
    private static List<String> whiteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelist);
        listView = findViewById(R.id.whitelistView);

        adapter = new ArrayAdapter<>(this, R.layout.custom_textview, getWhiteList());
        listView.setAdapter(adapter);
    }

    /**
     * Clears the whitelist, then sets it up again without loading saved one from stash
     * @param view the view that is clicked
     */
    public final void emptyWhitelist(View view) {

        new AlertDialog.Builder(WhitelistActivity.this,R.style.MyAlertDialogTheme)
                .setTitle(R.string.reset_whitelist)
                .setMessage(R.string.are_you_reset_whitelist)
                .setPositiveButton(R.string.reset, (dialog, whichButton) -> {
                    whiteList.clear();
                    Stash.put("whiteList", whiteList);
                    refreshListView();
                })
                .setNegativeButton(R.string.cancel, (dialog, whichButton) -> { }).show();
    }

    public void addRecommended(View view) {
        File externalDir = Environment.getExternalStorageDirectory();

        if (!whiteList.contains(new File(externalDir, "Music").getPath())) {
            whiteList.add(new File(externalDir, "Music").getPath());
            whiteList.add(new File(externalDir, "Podcasts").getPath());
            whiteList.add(new File(externalDir, "Ringtones").getPath());
            whiteList.add(new File(externalDir, "Alarms").getPath());
            whiteList.add(new File(externalDir, "Notifications").getPath());
            whiteList.add(new File(externalDir, "Pictures").getPath());
            whiteList.add(new File(externalDir, "Movies").getPath());
            whiteList.add(new File(externalDir, "Download").getPath());
            whiteList.add(new File(externalDir, "DCIM").getPath());
            whiteList.add(new File(externalDir, "Documents").getPath());
            Stash.put("whiteList", whiteList);
            refreshListView();

        } else
            Toast.makeText(this, "Already added",
                    Toast.LENGTH_LONG).show();
    }

    /**
     * Creates a dialog asking for a file/folder name to add to the whitelist
     * @param view the view that is clicked
     */
    public final void addToWhiteList(View view) {

        final EditText input = new EditText(WhitelistActivity.this);

        new AlertDialog.Builder(WhitelistActivity.this,R.style.MyAlertDialogTheme)
                .setTitle(R.string.add_to_whitelist)
                .setMessage(R.string.enter_file_name)
                .setView(input)
                .setPositiveButton(R.string.add, (dialog, whichButton) -> {
                    whiteList.add(String.valueOf(input.getText()));
                    Stash.put("whiteList", whiteList);
                    refreshListView();
                })
                .setNegativeButton(R.string.cancel, (dialog, whichButton) -> { }).show();
    }

    public void refreshListView() {
        runOnUiThread(() -> {
            adapter.notifyDataSetChanged();
            listView.invalidateViews();
            listView.refreshDrawableState();
        });
    }

    public static synchronized List<String> getWhiteList() {
        if (whiteList == null)
            whiteList = Stash.getArrayList("whiteList", String.class);
        return whiteList;
    }
}
