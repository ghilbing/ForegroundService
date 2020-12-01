package com.hilbing.foregroundservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private EditText etInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etInput = findViewById(R.id.et_input);
    }

    public void startService(View view) {
        String input = etInput.getText().toString();
        Intent serviceIntent = new Intent(this, MyService.class);
        serviceIntent.putExtra("inputExtra", input);
        startService(serviceIntent);
    }

    public void stopService(View view) {
        Intent serviceIntent = new Intent(this, MyServiceCounter.class);
        stopService(serviceIntent);

    }
}