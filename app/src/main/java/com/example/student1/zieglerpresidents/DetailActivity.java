package com.example.student1.zieglerpresidents;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    //Code > Override Methods = generate whatever methods want to overrride OR ctrl + O
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        PresidentPagerAdapter adapter = new PresidentPagerAdapter(PresidentAdapter.presidents);
        viewPager.setAdapter(adapter);
    }
}
