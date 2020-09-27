package com.rival.algoview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.rival.algoview.PathFinding.PathFindingMainActivity;
import com.rival.algoview.Sorting.SortingMainActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        findViewById(R.id.sorting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SortingMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        findViewById(R.id.path_finding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PathFindingMainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        findViewById(R.id.github_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/svkrclg/AlgoView/"));
                startActivity(intent);
            }
        });
        findViewById(R.id.linkedin_logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.linkedin.com/in/saurav-kumar-3b97b6119/"));
                startActivity(intent);
            }
        });
    }

}
