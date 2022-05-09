package com.crash.crash_lib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CrashTipActivity extends AppCompatActivity {

    public static void openNewPage(Context context, String tips) {
        Intent intent = new Intent(context, CrashTipActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("errorv1", tips);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        Intent intent = getIntent();
        String errorv1 = intent.getStringExtra("errorv1");

        TextView crashInfo = findViewById(R.id.crashInfo);
        crashInfo.setText(errorv1);
    }
}