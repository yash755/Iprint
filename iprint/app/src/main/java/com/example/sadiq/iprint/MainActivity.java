package com.example.sadiq.iprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button enter;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button enter = (Button) findViewById(R.id.enter);
        final Button register = (Button) findViewById(R.id.register);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
                     public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, login.class);
                MainActivity.this.startActivity(loginIntent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, register.class);
                MainActivity.this.startActivity(registerIntent);
            }
        });

    }
}
