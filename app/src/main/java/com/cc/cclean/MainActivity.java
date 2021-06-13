/*
 * Copyright 2020 Hunter J Drum
 */

package com.cc.cclean;


/*
 * Copyright 2020 Hunter J Drum
 */


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;

import com.fxn.stash.Stash;

import java.io.File;
import java.text.DecimalFormat;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ConstraintSet constraintSet = new ConstraintSet();
    static boolean running = false;
    SharedPreferences prefs;

    LinearLayout fileListView;
    ScrollView fileScrollView;
    ProgressBar scanPBar;
    TextView progressText;
    ConstraintLayout layout;
    TextView statusText,textView5,textView6,textView4,textView8;
    ImageView completed,completed1,completed2,completed3,imageView19,imageView20,imageView21,imageView22;

    @SuppressLint("LogConditional")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stash.init(getApplicationContext());


        fileListView = findViewById(R.id.fileListView);
        fileScrollView = findViewById(R.id.fileScrollView);
        scanPBar = findViewById(R.id.scanProgress);
        progressText = findViewById(R.id.ScanTextView);
        statusText = findViewById(R.id.statusTextView);
        completed=findViewById(R.id.completed);
        completed1=findViewById(R.id.completed1);
        completed2=findViewById(R.id.completed2);
        completed3=findViewById(R.id.completed3);
        layout = findViewById(R.id.main_layout);
        imageView19=findViewById(R.id.imageView19);
        imageView20=findViewById(R.id.imageView20);
        imageView21=findViewById(R.id.imageView21);
        imageView22=findViewById(R.id.imageView22);
        textView5=findViewById(R.id.textView5);
        textView6=findViewById(R.id.textView6);
        textView4=findViewById(R.id.textView4);
        textView8=findViewById(R.id.textView8);
        test();

        constraintSet.clone(layout);
    }


    public void test()  {
        new Thread(()-> scan(true)).start();


    }
    public void animateBtn() {

        constraintSet.clear(R.id.statusTextView,ConstraintSet.BOTTOM);
        constraintSet.setMargin(R.id.statusTextView,ConstraintSet.TOP,50);
        constraintSet.applyTo(layout);
    }

    /**
     * Searches entire device, adds all files to a list, then a for each loop filters
     * out files for deletion. Repeats the process as long as it keeps finding files to clean,
     * unless nothing is found to begin with
     */
    @SuppressLint("SetTextI18n")
    private void scan(boolean delete) {
        Looper.prepare();
        running = true;
        reset();

        File path = Environment.getExternalStorageDirectory();
        Log.d(String.valueOf(path), "scan: ");

        // scanner setup
        FileScanner fs = new FileScanner(path);
        fs.setEmptyDir(true);
        fs.setAutoWhite(false);
        fs.setDelete(delete);
        fs.setCorpse(true);
        fs.setGUI(this);

        // filters
        fs.setUpFilters((true), (false), (false));
        // failed scan
        if (path.listFiles() == null) { // is this needed? yes.
            TextView textView = printTextView("Scan failed. Allow Storage Permissions from Settings. ", Color.RED);
            runOnUiThread(() -> fileListView.addView(textView));
        }

        runOnUiThread(() -> {
            statusText.setText(getString(R.string.status_running));
        });

        // start scanning
             long kilobytesTotal1 = fs.startScan(true,false);
             long kilobytesTotal2 = fs.startScan(false,true);
             long kilobytesTotal=kilobytesTotal1+kilobytesTotal2;
        // crappy but working fix for percentage never reaching 100

        runOnUiThread(() -> {
            if(path.listFiles()!=null){
                scanPBar.setProgress(scanPBar.getMax());
                progressText.setText("100%");}
        });

        // kilobytes found/freed text
        runOnUiThread(() -> {
            if (delete) {
                statusText.setText(getString(R.string.freed) + " " + convertSize(kilobytesTotal));

            } else {
                statusText.setText(getString(R.string.found) + " " + convertSize(kilobytesTotal));
            }

            if(path.listFiles()==null){
                completed.setImageResource(R.drawable.error);
                completed1.setImageResource(R.drawable.error);
                completed2.setImageResource(R.drawable.error);
                completed3.setImageResource(R.drawable.error);

            }
        });

        fileScrollView.post(() -> fileScrollView.fullScroll(ScrollView.FOCUS_DOWN));

        running = false;
        Looper.loop();
    }


    /**
     * Convenience method to quickly create a textview
     * @param text - text of textview
     * @return - created textview
     */
    private synchronized TextView printTextView(String text, int color) {
        TextView textView = new TextView(MainActivity.this);
        textView.setTextColor(color);
        textView.setText(text);
        textView.setPadding(10,10,10,10);
        return textView;
    }

    private String convertSize(long length) {
        final DecimalFormat format = new DecimalFormat("#.##");
        final long MiB = 1024 * 1024;
        final long KiB = 1024;

        if (length > MiB) {
            return format.format(length / MiB) + " MB";
        }
        if (length > KiB) {
            return format.format(length / KiB) + " KB";
        }
        return format.format(length) + " B";
    }

    /**
     * Increments amount removed, then creates a text view to add to the scroll view.
     * If there is any error while deleting, turns text view of path red
     * @param file file to delete
     */
    synchronized TextView displayPath(File file) {
        // creating and adding a text view to the scroll view with path to file
        TextView textView = printTextView(file.getAbsolutePath(), getResources().getColor(R.color.textcolor));

        // adding to scroll view

        runOnUiThread(() -> fileListView.addView(textView));


        // scroll to bottom
        fileScrollView.post(() -> fileScrollView.fullScroll(ScrollView.FOCUS_DOWN));


        return textView;
    }

    /**
     * Removes all views present in fileListView (linear view), and sets found and removed
     * files to 0
     */
    private synchronized void reset() {
        runOnUiThread(() -> {
            fileListView.removeAllViews();
            scanPBar.setProgress(0);
            scanPBar.setMax(1);
        });
    }

    /**
     * Handles the whether the user grants permission. Launches new fragment asking the user to give file permission.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] != PackageManager.PERMISSION_GRANTED)
            Toast.makeText(this,"Allow Storage permissions from settings ",Toast.LENGTH_LONG).show();
    }

}
