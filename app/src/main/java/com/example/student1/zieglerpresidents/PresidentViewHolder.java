package com.example.student1.zieglerpresidents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PresidentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.name)TextView name;

    public PresidentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(President president) {
        name.setText(president.getPresident());
    }
}
