package com.hike.wa.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toolbar;


import androidx.annotation.Nullable;

import com.hike.wa.R;

// A small FAQ section
public class FAQ extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_page);
        Toolbar myToolbar = findViewById(R.id.faqToolbar);


        setActionBar(myToolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Home");
    }
}
