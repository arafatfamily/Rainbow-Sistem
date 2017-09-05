package com.bitsolution.pln;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.bitsolution.pln.Helpers.AppConfig;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayoutAndroid;
    CoordinatorLayout rootLayoutAndroid;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppConfig.JAGA_SERVICES(getBaseContext());

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(new CaterAdapter(this, AppConfig.gridViewStrings(), AppConfig.gridViewImages()));
        Log.d(TAG, "onCreate: " + AppConfig.isConnected(this)); //network checking

        initInstances();
    }

    private void initInstances() {
        rootLayoutAndroid = (CoordinatorLayout)findViewById(R.id.android_coordinator_layout);
        collapsingToolbarLayoutAndroid = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_android_layout);
        collapsingToolbarLayoutAndroid.setTitle("Rainbow Sistem");

        CaterAdapter adapter = new CaterAdapter(MainActivity.this, AppConfig.gridViewStrings(), AppConfig.gridViewImages());
        gridView = (GridView)findViewById(R.id.grid);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "You Clicked at " +AppConfig.gridViewStrings()[+ position], Toast.LENGTH_SHORT).show();

            }
        });
    }
}
