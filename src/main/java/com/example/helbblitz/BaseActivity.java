package com.example.helbblitz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public abstract class BaseActivity extends AppCompatActivity {

    // -------------- Declaration des elements --------------------------- //

    protected View parentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentView = getLayoutInflater().inflate(R.layout.activity_welcome, null);
        setContentView(parentView);
    }

}
