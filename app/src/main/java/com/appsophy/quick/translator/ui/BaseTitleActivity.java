package com.appsophy.quick.translator.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsophy.quick.translator.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseTitleActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView title;
    private ImageView titleLeft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());

        title = findViewById(R.id.title);
        titleLeft = findViewById(R.id.title_left);

        title.setText(title());
        titleLeft.setOnClickListener(this);
    }

    protected abstract int layoutId();

    protected abstract int title();

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_left) {
            onBackPressed();
        }
    }
}
