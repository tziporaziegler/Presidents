package com.example.student1.zieglerpresidents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    //Code > Override Methods = generate whatever methods want to overrride OR ctrl + O
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        PresidentDetailFragment fragment = (PresidentDetailFragment) getSupportFragmentManager().findFragmentById(R.id.detailFragment);

        President presidents[] = (President[]) getIntent().getSerializableExtra("PRESIDENTS");
        int position = getIntent().getIntExtra("POSITION", 0);

        fragment.showPresidentDetail(presidents, position);
    }
}
