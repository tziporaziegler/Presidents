package com.example.student1.zieglerpresidents;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements OnPresidentSelectedListener{

    private RecyclerView recyclerView;
    private FragmentManager manager;
    private PresidentListFragment listFragment;
    private PresidentDetailFragment detailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();
        listFragment = (PresidentListFragment) manager.findFragmentById(R.id.listFragment);
        detailFragment = (PresidentDetailFragment) manager.findFragmentById(R.id.detailFragment);
    }

    @Override
    public void onSelect(President[] array, int position) {
        if(detailFragment != null) {
            detailFragment.showPresidentDetail(array, position);
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("PRESIDENTS", array);
            intent.putExtra("POSITION", position);
            this.startActivity(intent);
        }
    }
}
