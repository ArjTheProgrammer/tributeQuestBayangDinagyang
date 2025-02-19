package com.silagan.tribalquestbayangdinagyang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Story2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_story2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void toSkip(View view) {
    }

    public void toPrev(View view) {
        Intent i = new Intent(this, Story1.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }

    public void toNext(View view) {
        Intent i = new Intent(this, Story3.class);
        startActivity(i);
        overridePendingTransition(0,0);
        finish();
    }
}